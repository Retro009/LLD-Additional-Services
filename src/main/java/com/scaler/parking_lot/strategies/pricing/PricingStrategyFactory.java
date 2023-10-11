package com.scaler.parking_lot.strategies.pricing;

import com.scaler.parking_lot.respositories.SlabRepository;

import java.util.Calendar;
import java.util.Date;

public class PricingStrategyFactory {

    private SlabRepository slabRepository;

    public PricingStrategyFactory(SlabRepository slabRepository) {
        this.slabRepository = slabRepository;
    }

    public PricingStrategy getPricingStrategy(Date exitTime){
        return null;
    }
}
