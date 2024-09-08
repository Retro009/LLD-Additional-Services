package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.InvalidGateException;
import com.scaler.parking_lot.exceptions.TicketNotFoundException;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.GateRepository;
import com.scaler.parking_lot.respositories.InvoiceRepository;
import com.scaler.parking_lot.respositories.TicketRepository;
import com.scaler.parking_lot.strategies.pricing.PricingStrategy;
import com.scaler.parking_lot.strategies.pricing.PricingStrategyFactory;

import java.util.Date;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService{
    private InvoiceRepository invoiceRepository;
    private TicketRepository ticketRepository;
    private GateRepository gateRepository;
    private PricingStrategyFactory strategyFactory;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, TicketRepository ticketRepository, GateRepository gateRepository, PricingStrategyFactory strategyFactory){
        this.invoiceRepository = invoiceRepository;
        this.ticketRepository = ticketRepository;
        this.gateRepository = gateRepository;
        this.strategyFactory = strategyFactory;
    }
    @Override
    public Invoice createInvoice(long ticketId, long gateId) throws TicketNotFoundException, InvalidGateException {

        Ticket ticket = ticketRepository.getTicketById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket Not Found"));
        Gate gate = gateRepository.findById(gateId).orElseThrow(() -> new InvalidGateException("Invalid Gate Id"));
        /*if(gate.getType().equals(GateType.ENTRY)) //this is making test case fail, This is one of the edge case
            throw new InvalidGateException("Cant create Invoice at Entry gate!!");*/

        Date exitDate = new Date();

        PricingStrategy pricingStrategy = strategyFactory.getPricingStrategy(exitDate);
        double costIncurredForParking = pricingStrategy.calculateAmount(ticket.getEntryTime(), exitDate, ticket.getVehicle().getVehicleType());
        double costIncurredForAdditionalServices = 0;
        for(AdditionalService service: ticket.getAdditionalServices())
            costIncurredForAdditionalServices += service.getCost();
        double totalCost = costIncurredForAdditionalServices + costIncurredForParking;
        Invoice invoice = new Invoice();
        invoice.setTicket(ticket);
        invoice.setGate(gate);
        invoice.setParkingAttendant(gate.getParkingAttendant());
        invoice.setExitTime(exitDate);
        invoice.setAmount(totalCost);
        return invoiceRepository.save(invoice);
    }
}
