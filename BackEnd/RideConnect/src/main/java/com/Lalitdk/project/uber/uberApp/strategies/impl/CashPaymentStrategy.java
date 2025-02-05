package com.Lalitdk.project.uber.uberApp.strategies.impl;

import com.Lalitdk.project.uber.uberApp.entities.Driver;
import com.Lalitdk.project.uber.uberApp.entities.Payment;
import com.Lalitdk.project.uber.uberApp.entities.enums.PaymentStatus;
import com.Lalitdk.project.uber.uberApp.entities.enums.TransactionMethod;
import com.Lalitdk.project.uber.uberApp.repositories.PaymentRepository;
import com.Lalitdk.project.uber.uberApp.services.WalletService;
import com.Lalitdk.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository; 



    @Override
    public void processPayment(Payment payment) {
        // here as  on COD the rider gives all the money to driver hence will deduct the
        // money from the wallet of driver as our commission

        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount()*PLATFORM_COMMISSION;
        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission,null ,
         payment.getRide() , TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

    }
}
