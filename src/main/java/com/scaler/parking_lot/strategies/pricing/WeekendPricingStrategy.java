package com.scaler.parking_lot.strategies.pricing;

import com.scaler.parking_lot.models.VehicleType;
import com.scaler.parking_lot.respositories.SlabRepository;

import java.util.Date;

public class WeekendPricingStrategy implements PricingStrategy{

    private SlabRepository slabRepository;

    public WeekendPricingStrategy(SlabRepository slabRepository) {
        this.slabRepository = slabRepository;
    }

    @Override
    public double calculateAmount(Date entryTime, Date exitTime, VehicleType vehicleType) {
        return 0;
    }
}
