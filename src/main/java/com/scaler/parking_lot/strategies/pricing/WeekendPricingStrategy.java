package com.scaler.parking_lot.strategies.pricing;

import com.scaler.parking_lot.models.Slab;
import com.scaler.parking_lot.models.VehicleType;
import com.scaler.parking_lot.respositories.SlabRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeekendPricingStrategy implements PricingStrategy{

    private SlabRepository slabRepository;

    public WeekendPricingStrategy(SlabRepository slabRepository) {
        this.slabRepository = slabRepository;
    }

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
        double amount = 0;

        List<Slab> slabs = slabRepository.getSortedSlabsByVehicleType(vehicleType);

        for(Slab slab:slabs){
            if(duration > slab.getEndHour() && slab.getEndHour() != -1)
                amount += slab.getPrice() * (slab.getEndHour()- slab.getStartHour());
            else if(slab.getEndHour() == -1 || duration <= slab.getEndHour()){
                amount += (duration - slab.getStartHour())* slab.getPrice();
                break;
            }
        }

        return amount;
    }
}
