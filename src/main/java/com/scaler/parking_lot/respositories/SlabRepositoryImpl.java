package com.scaler.parking_lot.respositories;

import com.scaler.parking_lot.models.Slab;
import com.scaler.parking_lot.models.VehicleType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SlabRepositoryImpl implements SlabRepository{
    private List<Slab> slabs = new ArrayList<>();
    private static long idCounter = 0;

    @Override
    public List<Slab> getSortedSlabsByVehicleType(VehicleType vehicleType) {
        return slabs.stream().filter(slab -> slab.getVehicleType().equals(vehicleType)).sorted(Comparator.comparingInt(Slab::getStartHour)).toList();
    }

    @Override
    public Slab save(Slab slab) {
        if(slab.getId()==0)
            slab.setId(++idCounter);
        slabs.add(slab);
        return slab;
    }
}
