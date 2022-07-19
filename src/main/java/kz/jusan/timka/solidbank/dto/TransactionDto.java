package kz.jusan.timka.solidbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String date;
    private String accountNumber;
    private double amount;
    private String typeOfOperation;
}