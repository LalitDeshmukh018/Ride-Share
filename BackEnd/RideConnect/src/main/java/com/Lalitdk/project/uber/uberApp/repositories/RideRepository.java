package com.Lalitdk.project.uber.uberApp.repositories;

import com.Lalitdk.project.uber.uberApp.entities.Ride;
import com.Lalitdk.project.uber.uberApp.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Lalitdk.project.uber.uberApp.entities.Driver;


@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

    Page<Ride> findByDriver(Driver driver  , Pageable pageRequest);
}
