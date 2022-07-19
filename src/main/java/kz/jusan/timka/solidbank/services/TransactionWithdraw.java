package kz.jusan.timka.solidbank.services;


import kz.jusan.timka.solidbank.entities.Transaction;
import kz.jusan.timka.solidbank.entities.User;
import kz.jusan.timka.solidbank.repositories.TransactionRepository;
import kz.jusan.timka.solidbank.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionWithdraw {
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;



    @Transactional
    // позволяет снимать деньги со счета(кроме Fixed аккаунтов)
    public void execute(String accountFullId, double amount, String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found by " + username));
        accountService.withdraw(amount, accountFullId, user.getId()); // если снимают больше чем есть на счету то не записывать это в транзакцию
        Transaction transaction = new Transaction(new Date().toString(), "withdrawal", accountFullId, amount, user.getId());
        transactionRepository.save(transaction);
        //   transactionDAO.addTransaction(transaction.getDate(), transaction.getTypeOfOperation(), transaction.getAccountNumber(), transaction.getAmount());

    }


}