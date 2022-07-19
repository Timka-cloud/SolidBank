package kz.jusan.timka.solidbank.services;



import kz.jusan.timka.solidbank.entities.Transaction;
import kz.jusan.timka.solidbank.entities.User;
import kz.jusan.timka.solidbank.repositories.TransactionRepository;
import kz.jusan.timka.solidbank.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionDeposit {
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;


    @Transactional
    // позволяет пополнять счет аккаунта
    public void execute(String accountFullId, double amount, String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by " + username));
        accountService.deposit(accountFullId, amount, user.getId());
        Transaction transaction = new Transaction(new Date().toString(), "refill", accountFullId, amount, user.getId());
        transactionRepository.save(transaction);
        //transactionDAO.addTransaction(transaction.getDate(), transaction.getTypeOfOperation(), transaction.getAccountNumber(), transaction.getAmount());
    }


}