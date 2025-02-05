package com.Lalitdk.project.uber.uberApp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (indexes ={
    @Index (name = "idx_rating_rider" ,columnList = "rider_Id"),
    @Index (name = "idx_rating_driver" , columnList = "driver_Id")
})
public class Rating {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //onr ride can have only one rating hence one to one maping 
    @OneToOne
    private Ride ride;

    //as one rider can have multiple rating hence many to one rating
    @ManyToOne
    private Rider rider;

    @ManyToOne 
    private Driver driver;

    private Integer driverRating; //driver rating

    private Integer riderRating; // rider rating

    public Ride orElseThrow(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    }
}
