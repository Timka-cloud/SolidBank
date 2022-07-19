package kz.jusan.timka.solidbank.entities.accounts;


public class AccountWithdraw extends Account{


    public AccountWithdraw(String accountType, Long bankID, Long clientID, double balance, boolean withdrawAllowed) {
        super(accountType, bankID, clientID, balance, withdrawAllowed);
    }
/**
 * можно снимать и вносить деньги.
 * @param accountType
 * @param id
 * @param clientID
 * @param balance
 * @param withdrawAllowed
 */

}