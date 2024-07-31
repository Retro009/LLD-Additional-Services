package com.scaler.parking_lot.controllers;

import com.scaler.parking_lot.dtos.GenerateInvoiceRequestDto;
import com.scaler.parking_lot.dtos.GenerateInvoiceResponseDto;
import com.scaler.parking_lot.dtos.ResponseStatus;
import com.scaler.parking_lot.exceptions.InvalidGateException;
import com.scaler.parking_lot.exceptions.TicketNotFoundException;
import com.scaler.parking_lot.services.InvoiceService;

public class InvoiceController {

        private InvoiceService invoiceService;

        public InvoiceController(InvoiceService invoiceService) {
            this.invoiceService = invoiceService;
        }

        public GenerateInvoiceResponseDto createInvoice(GenerateInvoiceRequestDto requestDto){
            GenerateInvoiceResponseDto responseDto = new GenerateInvoiceResponseDto();
            try{
                responseDto.setInvoice(invoiceService.createInvoice(requestDto.getTicketId(), requestDto.getGateId()));
                responseDto.setStatus(ResponseStatus.SUCCESS);
            }catch (TicketNotFoundException | InvalidGateException e){
                responseDto.setStatus(ResponseStatus.FAILURE);
            }
            return responseDto;
        }
}
