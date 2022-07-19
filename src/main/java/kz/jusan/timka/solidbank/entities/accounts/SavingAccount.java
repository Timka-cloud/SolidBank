package kz.jusan.timka.solidbank.entities.accounts;

public class SavingAccount extends AccountWithdraw{


    public SavingAccount(String accountType, Long bankID, Long clientID, double balance, boolean withdrawAllowed) {
        super(accountType, bankID, clientID, balance, withdrawAllowed);
    }
}