package com.Lalitdk.project.uber.uberApp.strategies.impl;

import com.Lalitdk.project.uber.uberApp.entities.Driver;
import com.Lalitdk.project.uber.uberApp.entities.Payment;
import com.Lalitdk.project.uber.uberApp.entities.Rider;
import com.Lalitdk.project.uber.uberApp.entities.enums.PaymentStatus;
import com.Lalitdk.project.uber.uberApp.entities.enums.TransactionMethod;
import com.Lalitdk.project.uber.uberApp.repositories.PaymentRepository;
import com.Lalitdk.project.uber.uberApp.services.WalletService;
import com.Lalitdk.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// here money will deduct from the account of rider
// Rider - 232
// driver had 500
// cost of ride is 100
// and and the commission of the paltform is 30
// if rider pay by wallet then rider (232 -100) 
// and driver will got 500 + (100-30) = 570

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {


    private final WalletService walletService;
    private final PaymentRepository paymentRepository;
    @Override
    public void processPayment(Payment payment) {
    
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),payment.getAmount(),null,
        payment.getRide(), TransactionMethod.RIDE );

        double driverCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),driverCut,null,
        payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

        
    }
}
