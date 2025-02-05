package com.Lalitdk.project.uber.uberApp.services;

import com.Lalitdk.project.uber.uberApp.entities.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
