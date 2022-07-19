package kz.jusan.timka.solidbank.entities.accounts;

public class CheckingAccount extends AccountWithdraw {

    public CheckingAccount(String accountType, Long bankID, Long clientID, double balance, boolean withdrawAllowed) {
        super(accountType, bankID, clientID, balance, withdrawAllowed);
    }
}