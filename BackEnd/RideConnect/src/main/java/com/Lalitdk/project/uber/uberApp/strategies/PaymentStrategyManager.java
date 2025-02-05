package com.Lalitdk.project.uber.uberApp.strategies;

import org.springframework.stereotype.Component;

import com.Lalitdk.project.uber.uberApp.entities.enums.PaymentMethod;
import com.Lalitdk.project.uber.uberApp.strategies.impl.CashPaymentStrategy;
import com.Lalitdk.project.uber.uberApp.strategies.impl.WalletPaymentStrategy;

import lombok.RequiredArgsConstructor;
 
@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final CashPaymentStrategy cashPaymentStrategy;
    private final  WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
       return switch (paymentMethod) {
            case WALLET ->  walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;

            default -> throw new RuntimeException("Invalid payment method");
           
        };
    }

}
