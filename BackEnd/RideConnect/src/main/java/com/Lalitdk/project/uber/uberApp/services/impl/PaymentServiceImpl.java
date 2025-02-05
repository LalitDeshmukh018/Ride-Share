package com.Lalitdk.project.uber.uberApp.services.impl;

import com.Lalitdk.project.uber.uberApp.entities.Payment;
import com.Lalitdk.project.uber.uberApp.entities.Ride;
import com.Lalitdk.project.uber.uberApp.entities.enums.PaymentStatus;
import com.Lalitdk.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.Lalitdk.project.uber.uberApp.repositories.PaymentRepository;
import com.Lalitdk.project.uber.uberApp.services.PaymentService;
import com.Lalitdk.project.uber.uberApp.strategies.PaymentStrategyManager;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride ) {
        Payment payment = paymentRepository.findByRide(ride)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment is not found for the ride :" +ride.getId() ));
        // here will have the various strategies for creating the payment 
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);

    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
        .ride(ride)
        .paymentMethod(ride.getPaymentMethod())
        .amount(ride.getFare())
        .paymentStatus(PaymentStatus.PENDING)
        .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
       payment.setPaymentStatus(status);
       paymentRepository.save(payment);
    }
}
