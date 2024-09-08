package com.scaler.parking_lot.services;

import com.scaler.parking_lot.exceptions.*;
import com.scaler.parking_lot.models.*;
import com.scaler.parking_lot.respositories.*;
import com.scaler.parking_lot.strategies.assignment.SpotAssignmentStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TicketServiceImpl implements TicketService{
    private GateRepository gateRepository;
    private VehicleRepository vehicleRepository;
    private SpotAssignmentStrategy spotAssignmentStrategy;
    private ParkingLotRepository parkingLotRepository;
    private TicketRepository ticketRepository;

    public TicketServiceImpl(GateRepository gateRepository, VehicleRepository vehicleRepository, SpotAssignmentStrategy spotAssignmentStrategy, ParkingLotRepository parkingLotRepository, TicketRepository ticketRepository) {
        this.gateRepository = gateRepository;
        this.vehicleRepository = vehicleRepository;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.parkingLotRepository = parkingLotRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket generateTicket(long gateId, String registrationNumber, String vehicleType, List<String> additionalServicesList) throws InvalidGateException, InvalidParkingLotException, ParkingSpotNotAvailableException, UnsupportedAdditionalService, AdditionalServiceNotSupportedByVehicle {
        Gate gate = this.gateRepository.findById(gateId).orElseThrow(()-> new InvalidGateException("Invalid gate id"));
        if(gate.getType().equals(GateType.EXIT))
            throw new InvalidGateException("Cant create ticket at Exit gate!!");
        Vehicle vehicle;
        Optional<Vehicle> optionalVehicle = vehicleRepository.getVehicleByRegistrationNumber(registrationNumber);
        if (optionalVehicle.isEmpty()) {
            vehicle = new Vehicle();
            vehicle.setRegistrationNumber(registrationNumber);
            vehicle.setVehicleType(VehicleType.valueOf(vehicleType));
            vehicle = vehicleRepository.save(vehicle);
        } else {
            vehicle = optionalVehicle.get();
        }

        ParkingLot parkingLot = parkingLotRepository.getParkingLotByGateId(gateId).orElseThrow(()-> new InvalidParkingLotException("Invalid parking lot id"));
        ParkingSpot parkingSpot = spotAssignmentStrategy.assignSpot(parkingLot, VehicleType.valueOf(vehicleType)).orElseThrow(()-> new ParkingSpotNotAvailableException("No parking spot available"));

        List<AdditionalService> additionalServices = new ArrayList<>();
        if(additionalServicesList != null) {
            for (String additionalServiceStr : additionalServicesList) {
                AdditionalService additionalService;
                try {
                    additionalService = AdditionalService.valueOf(additionalServiceStr);
                } catch (IllegalArgumentException e) {
                    throw new UnsupportedAdditionalService("Invalid additional service. plz check the notice board.. ");
                }
                if (!additionalService.getSupportedVehicleTypes().contains(VehicleType.valueOf(vehicleType))) {
                    throw new AdditionalServiceNotSupportedByVehicle("Invalid vehicle type for additional service");
                }
                additionalServices.add(additionalService);
            }
        }

        Ticket ticket = new Ticket();
        ticket.setVehicle(vehicle);
        ticket.setEntryTime(new Date());
        ticket.setParkingSpot(parkingSpot);
        ticket.setGate(gate);
        ticket.setParkingAttendant(gate.getParkingAttendant());
        ticket.setAdditionalServices(additionalServices);
        return this.ticketRepository.save(ticket);
    }

}
