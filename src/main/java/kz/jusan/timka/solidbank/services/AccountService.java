package kz.jusan.timka.solidbank.services;


import kz.jusan.timka.solidbank.dto.AccountDto;
import kz.jusan.timka.solidbank.dto.CreationAccountDto;
import kz.jusan.timka.solidbank.dto.TransferDto;
import kz.jusan.timka.solidbank.entities.accounts.AccountWithdraw;

import java.util.List;


public interface AccountService {
    boolean create(CreationAccountDto creationAccountDto, String username);

    void deleteById(String fullAccountId, String username);

    public void deposit(String accountFullId, double amount, Long clientId);

    AccountDto getClientAccount(String accountID, String username);

    AccountWithdraw getClientWithdrawAccount(Long clientID, String accountID);

    List<AccountDto> getClientAccounts(String username);

    void withdraw(double amount, String accountFullID, Long ClientID);

    void transfer(String senderAccId, TransferDto transferDto, String username);
}