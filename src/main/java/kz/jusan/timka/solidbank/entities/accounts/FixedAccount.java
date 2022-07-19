package kz.jusan.timka.solidbank.entities.accounts;

public class FixedAccount extends AccountDeposit{


    public FixedAccount(String accountType, Long bankID, Long clientID, double balance, boolean withdrawAllowed) {
        super(accountType, bankID, clientID, balance, withdrawAllowed);
    }
}