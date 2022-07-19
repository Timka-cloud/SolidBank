package kz.jusan.timka.solidbank.services;

import kz.jusan.timka.solidbank.dto.AccountDto;
import kz.jusan.timka.solidbank.dto.CreationAccountDto;
import kz.jusan.timka.solidbank.dto.TransferDto;
import kz.jusan.timka.solidbank.entities.Transaction;
import kz.jusan.timka.solidbank.entities.User;
import kz.jusan.timka.solidbank.entities.accounts.Account;
import kz.jusan.timka.solidbank.entities.accounts.AccountWithdraw;
import kz.jusan.timka.solidbank.exceptions.AccountCreationException;
import kz.jusan.timka.solidbank.exceptions.AccountNotFound;
import kz.jusan.timka.solidbank.exceptions.AccountWithdrawException;
import kz.jusan.timka.solidbank.repositories.AccountRepository;
import kz.jusan.timka.solidbank.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    private final UserService userService;
    private final TransactionRepository transactionRepository;
    @Override
    @Transactional
    public boolean create(CreationAccountDto creationAccountDto, String username) {
        Account account = null;
        User user = findUser(username);

        if (creationAccountDto.getAccountType().equals("CHECKING")) {
            account = new Account("CHECKING", 1L, user.getId(), 0.0, true);
        } else if (creationAccountDto.getAccountType().equals("FIXED")) {
            account = new Account("FIXED", 1L,  user.getId(), 0.0, false);
        } else if (creationAccountDto.getAccountType().equals("SAVING")) {
            account = new Account("SAVING", 1L, user.getId(), 0.0, true);
        }

        if (account == null) {
            throw new AccountCreationException("Wrong type of account");
        }
        accountRepository.save(account);
        Long accountID = accountRepository.getLastId();
        accountRepository.setAccountFullId(String.format("%03d%06d", account.getBankID(), accountID), accountID, user.getId());
        System.out.println("Bank account created");
        return true;
    }

    @Override
    public void deleteById(String fullAccountId, String username) {
        User user = findUser(username);
        Account account = accountRepository.findAccountByAccountFullIdAndClientID(fullAccountId, user.getId());
        if(account == null) {
            throw new AccountNotFound("Account not found by " + fullAccountId);
        }
        accountRepository.delete(account);
    }

    @Override
    @Transactional
    public void deposit(String accountFullId, double amount, Long clientId) {
        Account account = accountRepository.findAccountByAccountFullIdAndClientID(accountFullId, clientId);
        if(account == null) {
            throw new AccountNotFound("Account not found by " + accountFullId);
        }
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

    }

    @Override
    public AccountDto getClientAccount(String accountID, String username) {
        User user = findUser(username);
        Account account = accountRepository.findAccountByAccountFullIdAndClientID(accountID, user.getId());
        if(account == null) {
            throw new AccountNotFound("Account not found by " + accountID);
        }
        return new AccountDto(account.getAccountFullId(), account.getAccountType(),user.getId() , account.getBalance(), account.isWithdrawAllowed());
    }

    @Override
    public AccountWithdraw getClientWithdrawAccount(Long clientID, String accountID) {
        return accountRepository.getClientWithdrawAccount(clientID, accountID);

    }

    @Override
    public List<AccountDto> getClientAccounts(String username) {
        User user = findUser(username);

        List<Account> accountList = accountRepository.findAccountsByClientID(user.getId());
        return accountList.stream().map(a -> new AccountDto(a.getAccountFullId(), a.getAccountType(), a.getClientID(), a.getBalance(), a.isWithdrawAllowed())).collect(Collectors.toList());

    }

    @Override
    public void withdraw(double amount, String accountFullID, Long clientID) {

        Account account = accountRepository.findAccountByAccountFullIdAndClientID(accountFullID, clientID);

        if(account == null) {
            throw new AccountNotFound("Account not found by " + accountFullID);
        }
        if(!account.isWithdrawAllowed()) {
            throw new AccountWithdrawException("You can't withdraw money from fixed account");
        }
        if(amount > account.getBalance()) {
            System.out.println("there are not enough funds in your account");
            throw new AccountWithdrawException("there are not enough funds in your account");
        }
        account.setBalance(account.getBalance() - amount);

        accountRepository.save(account);
        System.out.printf("%.2f$ transferred from %s account\n", amount,accountFullID);
    }

    @Override
    @Transactional
    public void transfer(String senderAccId, TransferDto transferDto, String username) {
        User user = findUser(username);
        Account senderAcc = accountRepository.findAccountByAccountFullIdAndClientID(senderAccId, user.getId());
        Account receivedAcc = accountRepository.findAccountByAccountFullId(transferDto.getAccountFullId());
        if(transferDto.getBalance() <= 0) {
            throw new IllegalArgumentException("Amount should be more than 0");
        }
        if(senderAcc == null) {
            throw new AccountNotFound("Account not found by " + senderAccId);
        }
        if(receivedAcc == null) {
            throw new AccountNotFound("Account not found by " + transferDto.getAccountFullId());
        }
        if(senderAcc.getAccountFullId().equals(receivedAcc.getAccountFullId())) {
            throw new AccountWithdrawException("You can't transfer from the same account");
        }
        if(!senderAcc.isWithdrawAllowed()) {
            throw new AccountWithdrawException("You can't transfer money from fixed account");
        }
        double balance = senderAcc.getBalance();
        if(balance - transferDto.getBalance() < 0) {
            throw new AccountWithdrawException("there are not enough funds in your account");
        }
        senderAcc.setBalance(senderAcc.getBalance() - transferDto.getBalance());
        receivedAcc.setBalance(receivedAcc.getBalance() + transferDto.getBalance());
        accountRepository.save(senderAcc);
        accountRepository.save(receivedAcc);

        Transaction senderTransaction = new Transaction(new Date().toString(), "withdraw transfer", senderAccId, transferDto.getBalance(), user.getId());
        Transaction receiverTransaction = new Transaction(new Date().toString(), "refill transfer", transferDto.getAccountFullId(), transferDto.getBalance(),receivedAcc.getClientID());
        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);
    }


    private User findUser(String username) {
        return userService.findByUsername(username);
    }
}