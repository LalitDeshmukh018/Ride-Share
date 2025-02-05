package com.Lalitdk.project.uber.uberApp.services;

import com.Lalitdk.project.uber.uberApp.entities.Ride;
import com.Lalitdk.project.uber.uberApp.entities.User;
import com.Lalitdk.project.uber.uberApp.entities.Wallet;
import com.Lalitdk.project.uber.uberApp.entities.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionalId 
                            , Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet (User user, Double amount, String transactionalId
                                     , Ride ride, TransactionMethod transactionMethod);

    void withdrawAllMyMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);
}
