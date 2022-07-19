package kz.jusan.timka.solidbank.dto;

import lombok.Data;

@Data
public class TransferDto {
    private String accountFullId;
    private double balance;
}