package kz.jusan.timka.solidbank.exceptions;

public class AccountWithdrawException extends RuntimeException{
    public AccountWithdrawException(String message) {
        super(message);
    }
}