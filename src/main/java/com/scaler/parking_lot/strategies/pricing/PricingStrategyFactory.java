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
        Calendar cal = Calendar.getInstance();
        cal.setTime(exitTime);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if(day == 1 || day == 7)
            return new WeekendPricingStrategy(slabRepository);
        return new WeekdayPricingStrategy();
    }
}
