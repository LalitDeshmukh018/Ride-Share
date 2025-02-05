package com.Lalitdk.project.uber.uberApp.strategies;

import com.Lalitdk.project.uber.uberApp.entities.Driver;
import com.Lalitdk.project.uber.uberApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDriver(RideRequest rideRequest);
}
