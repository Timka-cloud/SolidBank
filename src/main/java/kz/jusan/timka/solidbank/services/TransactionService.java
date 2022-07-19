package kz.jusan.timka.solidbank.services;

import kz.jusan.timka.solidbank.dto.AccountDto;
import kz.jusan.timka.solidbank.dto.TransactionDto;
import kz.jusan.timka.solidbank.entities.Transaction;
import kz.jusan.timka.solidbank.entities.User;
import kz.jusan.timka.solidbank.exceptions.AccountNotFound;
import kz.jusan.timka.solidbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final AccountService accountService;


    public List<TransactionDto> findByAccountFullId(String accountFullId, String username) {
        User user = userService.findByUsername(username);
        AccountDto clientAccount = accountService.getClientAccount(accountFullId, username);
        if(clientAccount == null) {
            throw new AccountNotFound("Account not found by " + accountFullId);
        }
        List<Transaction> list = transactionRepository.findAllByAccountNumberAndClientId(accountFullId, user.getId());
        return list.stream().map((t) -> new TransactionDto(t.getDate(), t.getAccountNumber(), t.getAmount(), t.getTypeOfOperation())).collect(Collectors.toList());
    }


}