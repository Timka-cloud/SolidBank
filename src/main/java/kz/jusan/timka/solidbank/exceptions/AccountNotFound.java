package kz.jusan.timka.solidbank.exceptions;

public class AccountNotFound extends RuntimeException{
    public AccountNotFound(String message) {
        super(message);
    }
}