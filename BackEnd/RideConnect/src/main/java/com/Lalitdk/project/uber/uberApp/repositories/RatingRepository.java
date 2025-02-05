package com.Lalitdk.project.uber.uberApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Lalitdk.project.uber.uberApp.entities.Driver;
import com.Lalitdk.project.uber.uberApp.entities.Rider;
import java.util.List;
import java.util.Optional;

import com.Lalitdk.project.uber.uberApp.entities.Rating;
import com.Lalitdk.project.uber.uberApp.entities.Ride;

public interface RatingRepository extends JpaRepository<Rating ,Long> {

    List<Rating> findByRider(Rider rider);
    List<Rating> findByDriver(Driver driver);
    Optional<Rating> findByRide(Ride ride);
    
    
}
