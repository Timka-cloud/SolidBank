package kz.jusan.timka.solidbank.entities.accounts;

import lombok.Data;


import javax.persistence.*;


@Data
@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "account_full_id")
    private String accountFullId;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "bank_id")
    private Long bankID;

    @Column(name = "client_id")
    private Long clientID;

    @Column(name = "balance")
    private double balance;

    @Column(name = "withdraw_allowed")
    private boolean withdrawAllowed;

    public Account(String accountType, Long bankID, Long clientID, double balance, boolean withdrawAllowed) {
        this.accountType = accountType;
        this.bankID = bankID;
        this.clientID = clientID;
        this.balance = balance;
        this.withdrawAllowed = withdrawAllowed;
    }

    public Account() {

    }

    @Override
    public String toString() {
        return String.format("Account{type=%s, id='%s', clientID='%s', balance=%.2f}", accountType, accountFullId, clientID, balance);
    }
}