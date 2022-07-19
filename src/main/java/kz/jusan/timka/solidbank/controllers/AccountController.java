package kz.jusan.timka.solidbank.controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import kz.jusan.timka.solidbank.dto.AccountDto;
import kz.jusan.timka.solidbank.dto.CreationAccountDto;
import kz.jusan.timka.solidbank.dto.TransactionDto;
import kz.jusan.timka.solidbank.dto.TransferDto;
import kz.jusan.timka.solidbank.responses.ResponseManyAcc;
import kz.jusan.timka.solidbank.responses.ResponseMessage;
import kz.jusan.timka.solidbank.responses.ResponseSingleAcc;
import kz.jusan.timka.solidbank.responses.ResponseTransactions;
import kz.jusan.timka.solidbank.services.AccountService;
import kz.jusan.timka.solidbank.services.TransactionDeposit;
import kz.jusan.timka.solidbank.services.TransactionService;
import kz.jusan.timka.solidbank.services.TransactionWithdraw;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@SecurityRequirement(name = "BasicAuth")
public class AccountController {
    private final AccountService accountService;
    private final TransactionDeposit transactionDeposit;
    private final TransactionWithdraw transactionWithdraw;

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ResponseManyAcc> getClientAccounts(Principal principal) {
        List<AccountDto> clientAccounts = accountService.getClientAccounts(principal.getName());
        return new ResponseEntity<>(ResponseManyAcc.builder().statusCode(HttpStatus.OK.value()).message(clientAccounts.size() + " accounts returned").accountList(clientAccounts).build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> create(@RequestBody CreationAccountDto creationAccountDto, Principal principal) {
        accountService.create(creationAccountDto, principal.getName());
        return new ResponseEntity<>(ResponseMessage.builder().message("Account has been created").statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    }

    @GetMapping("/{account_id}")
    public ResponseEntity<ResponseSingleAcc> getClientAccount(@PathVariable(name = "account_id") String fullAccountId, Principal principal) {
        AccountDto clientAccount = accountService.getClientAccount(fullAccountId, principal.getName());
        return new ResponseEntity<>(ResponseSingleAcc.builder().statusCode(HttpStatus.OK.value()).message("1 account returned").accountDto(clientAccount).build(), HttpStatus.OK);
    }

    @DeleteMapping("/{account_id}")
    public ResponseEntity<ResponseMessage> delete(@PathVariable(name = "account_id") String fullAccountId, Principal principal) {
        accountService.deleteById(fullAccountId, principal.getName());
        return new ResponseEntity<>(ResponseMessage.builder().statusCode(HttpStatus.OK.value()).message("Account 1 deleted").build(), HttpStatus.OK);
    }

    @PostMapping("/{account_id}/deposit")
    public ResponseEntity<ResponseMessage> depositBalance(@PathVariable(name = "account_id") String fullAccountId, @RequestParam double balance, Principal principal) {
        if(balance <= 0) {
            throw new IllegalArgumentException("Amount should be more than 0");
        }
        transactionDeposit.execute(fullAccountId, balance, principal.getName());
        return new ResponseEntity<>(ResponseMessage.builder().message(String.format("%.2f$ transferred to %s account", balance, fullAccountId)).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    }

    @PostMapping("/{account_id}/withdraw")
    public ResponseEntity<ResponseMessage> withdrawBalance(@PathVariable(name = "account_id") String fullAccountId, @RequestParam double balance, Principal principal) {
        if(balance <= 0) {
            throw new IllegalArgumentException("Amount should be more than 0");
        }
        transactionWithdraw.execute(fullAccountId, balance, principal.getName());
        return new ResponseEntity<>(ResponseMessage.builder().message(String.format("%.2f$ transferred from %s account", balance, fullAccountId)).statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    }

    @GetMapping("/{account_id}/transactions")
    public ResponseEntity<ResponseTransactions> getTransactionsByAccountId(@PathVariable(name = "account_id") String fullAccountId, Principal principal) {
        List<TransactionDto> list = transactionService.findByAccountFullId(fullAccountId, principal.getName());
        return new ResponseEntity<>(ResponseTransactions.builder().statusCode(HttpStatus.OK.value()).message(list.size() + " transactions returned").transactionDtoList(list).build(), HttpStatus.OK);
    }

    @PostMapping("/{account_id}/transfer")
    public ResponseEntity<ResponseMessage> getTransactionsByAccountId(@PathVariable(name = "account_id") String fullAccountId, @RequestBody TransferDto transferDto, Principal principal) {

        accountService.transfer(fullAccountId, transferDto, principal.getName());

        return new ResponseEntity<>(ResponseMessage.builder().statusCode(HttpStatus.OK.value()).message("operation completed successfully").build(), HttpStatus.OK);
    }
}