package com.scaler.parking_lot.strategies.pricing;

import com.scaler.parking_lot.models.VehicleType;

import java.util.Calendar;
import java.util.Date;

public class WeekdayPricingStrategy implements PricingStrategy{

    @Override
    public double calculateAmount(Date entryTime, Date exitTime, VehicleType vehicleType) {

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(entryTime);
        cal2.setTime(exitTime);

        int duration = 0;

        while (cal1.before(cal2)) {
            cal1.add(Calendar.HOUR, 1);
            duration++;
        }

        return duration * 10;
    }
}
