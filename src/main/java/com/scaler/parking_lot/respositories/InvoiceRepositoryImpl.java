package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceRepositoryImpl implements InvoiceRepository{
    private List<Invoice> invoices = new ArrayList<>();
    private static long idCounter = 0;
    @Override
    public Invoice save(Invoice invoice) {
        if(invoice.getId()==0)
            invoice.setId(++idCounter);
        return invoice;
    }
}
