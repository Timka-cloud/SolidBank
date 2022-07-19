package kz.jusan.timka.solidbank.repositories;


import kz.jusan.timka.solidbank.entities.accounts.Account;
import kz.jusan.timka.solidbank.entities.accounts.AccountWithdraw;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findAccountsByClientID(Long id);
    Account findAccountByAccountFullIdAndClientID(String accountID, Long clientId);
    Account findAccountByAccountFullId(String accountID);

    @Modifying
    @Query(value = "update Account a set a.account_full_id = :accountFullId where a.id = :accountID and a.client_id = :clientId", nativeQuery = true)
    void setAccountFullId(String accountFullId, Long accountID, Long clientId);

    @Query(value = "SELECT a.id FROM Account a ORDER BY a.id DESC LIMIT 1", nativeQuery = true)
    Long getLastId();

    @Modifying
    @Query(value = "update Account a set a.balance = :amount where a.account_full_id= :accountFullId", nativeQuery = true)
    void updateAccount(String accountFullId, double amount);



    @Query(value = "select * from Account a where a.withdraw_allowed = 1 and a.client_id = :clientID and a.account_full_id = :accountID", nativeQuery = true)
    AccountWithdraw getClientWithdrawAccount(Long clientID, String accountID);


}