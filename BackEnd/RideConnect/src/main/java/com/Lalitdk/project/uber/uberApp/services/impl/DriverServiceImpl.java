package com.Lalitdk.project.uber.uberApp.services.impl;

import com.Lalitdk.project.uber.uberApp.entities.User;
import com.Lalitdk.project.uber.uberApp.repositories.DriverRepository;
import com.Lalitdk.project.uber.uberApp.dto.DriverDto;
import com.Lalitdk.project.uber.uberApp.dto.RideDto;
import com.Lalitdk.project.uber.uberApp.dto.RiderDto;
import com.Lalitdk.project.uber.uberApp.entities.Driver;
import com.Lalitdk.project.uber.uberApp.entities.Ride;
import com.Lalitdk.project.uber.uberApp.entities.RideRequest;
import com.Lalitdk.project.uber.uberApp.entities.enums.RideRequestStatus;
import com.Lalitdk.project.uber.uberApp.entities.enums.RideStatus;
import com.Lalitdk.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.Lalitdk.project.uber.uberApp.services.DriverService;
import com.Lalitdk.project.uber.uberApp.services.PaymentService;
import com.Lalitdk.project.uber.uberApp.services.RatingService;
import com.Lalitdk.project.uber.uberApp.services.RideRequestService;
import com.Lalitdk.project.uber.uberApp.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;
    

    @Override
    @Transactional

    public RideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)) {
            throw new RuntimeException("RideRequest cannot be accepted, status is " + rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getAvailable()) {
            throw new RuntimeException("Driver cannot accept ride due to unavailability");
        }

        currentDriver.setAvailable(false);
        Driver savedDriver = driverRepository.save(currentDriver);

          updateDriverAvailability(currentDriver, false );


        Ride ride = rideService.createNewRide(rideRequest, savedDriver);
        return modelMapper.map(ride, RideDto.class);
    }


   // for the cancel purpose we aes upose dto do not only that
    // for cancellation the ride only driver can cancel the ride
    // including various logic ie can cancel only when the ride has not been started
    // if ride get started then we cant cancel it we can just end it 


    @Override
    public RideDto cancelRide(Long rideId) {

        // all we need is to get the ride first, check whether this
        // driver hold this ride

        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        // we can only cancel the ride if it is confirm
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new  RuntimeException("Ride cannot be cancelled, invalid status: " + ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.CANCELLED);
        updateDriverAvailability(driver, true);

        return modelMapper.map(ride,RideDto.class);


    }


    @Override
    public RideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot start a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)) {
            throw new RuntimeException("Ride status is not CONFIRMED hence cannot be started, status: "+ride.getRideStatus());
        }

        if(!otp.equals(ride.getOtp())) {
            throw new RuntimeException("Otp is not valid, otp: "+otp);
        }

        ride.setEndedAt(LocalDateTime.now());

        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        // here after starting the ride i also need to create the payment object 
        //associated to that ride

        ratingService.createNewRating(savedRide);
        paymentService.createNewPayment(savedRide);


       return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
         
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start a ride as he not accepted it earlier");
        }

        if (!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new  RuntimeException("Ride status is not ONGOING hence cannot started status " + ride.getRideStatus());  
        }

         Ride savedRide = rideService.updateRideStatus(ride, RideStatus.ENDED);

        updateDriverAvailability(driver, true);
        paymentService.processPayment(ride);

        return modelMapper.map(savedRide , RideDto.class);

    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver  = getCurrentDriver();

        if (!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is not the owner of this ride");
        }

        if (!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride has not been ended yet so can't rating , status :" +ride.getRideStatus());
        }

        return ratingService.rateRider(ride,rating);
        
    
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public org.springframework.data.domain.Page<RideDto> getAllMyRides(PageRequest pageRequest) {
        Driver driver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(driver, pageRequest).map(
            ride -> modelMapper.map(ride, RideDto.class)
        );

    }

    @Override
    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("Driver not associate with user with id" + user.getId()));
    }



    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
       driver.setAvailable(null);
       return driverRepository.save(driver);

}


    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}

