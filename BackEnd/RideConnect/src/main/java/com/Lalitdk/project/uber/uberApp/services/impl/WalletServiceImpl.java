package com.Lalitdk.project.uber.uberApp.services.impl;

import com.Lalitdk.project.uber.uberApp.entities.Ride;
import com.Lalitdk.project.uber.uberApp.entities.User;
import com.Lalitdk.project.uber.uberApp.entities.Wallet;
import com.Lalitdk.project.uber.uberApp.entities.WalletTransaction;
import com.Lalitdk.project.uber.uberApp.entities.enums.TransactionMethod;
import com.Lalitdk.project.uber.uberApp.entities.enums.TransactionType;
import com.Lalitdk.project.uber.uberApp.exceptions.ResourceNotFoundException;
import com.Lalitdk.project.uber.uberApp.repositories.WalletRepository;
import com.Lalitdk.project.uber.uberApp.services.WalletService;
import com.Lalitdk.project.uber.uberApp.services.WalletTransactionService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionalId , Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);

        // since to look all the transaction will implemnt transaction
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionalId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

               // walletTransactionService.createNewWalletTransaction(walletTransaction);
                wallet.getTransactions().add(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    @Transactional

    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionalId , Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = walletRepository.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("Wallet not fount for user with id" + user.getId()));
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
        .transactionId(transactionalId)
        .ride(ride)
        .wallet(wallet)
        .transactionType(TransactionType.DEBIT)
        .transactionMethod(transactionMethod)
        .amount(amount)
        .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);


        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id : " + walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("Wallet not found for user with id " + user.getId()));
    }

   
}
