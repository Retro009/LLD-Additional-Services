package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.InvalidGateException;
import com.scaler.parking_lot.exceptions.TicketNotFoundException;
import com.scaler.parking_lot.models.AdditionalService;
import com.scaler.parking_lot.models.Gate;
import com.scaler.parking_lot.models.Invoice;
import com.scaler.parking_lot.models.Ticket;
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
        Date exitDate = new Date();

        PricingStrategy pricingStrategy = strategyFactory.getPricingStrategy(exitDate);
        double amount = pricingStrategy.calculateAmount(ticket.getEntryTime(), exitDate, ticket.getVehicle().getVehicleType());
        List<AdditionalService> additionalServices = ticket.getAdditionalServices();
        for(AdditionalService service: additionalServices)
            amount += service.getCost();

        Invoice invoice = new Invoice();
        invoice.setTicket(ticket);
        invoice.setGate(gate);
        invoice.setParkingAttendant(gate.getParkingAttendant());
        invoice.setExitTime(exitDate);
        invoice.setAmount(amount);

        return invoice;
    }
}
