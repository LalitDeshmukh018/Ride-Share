package com.Lalitdk.project.uber.uberApp.repositories;

import com.Lalitdk.project.uber.uberApp.entities.Rider;
import com.Lalitdk.project.uber.uberApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<Rider> findByUser(User user);
}
