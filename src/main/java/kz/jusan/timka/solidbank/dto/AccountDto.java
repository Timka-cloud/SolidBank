package kz.jusan.timka.solidbank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;

@Data
@AllArgsConstructor
public class AccountDto {
    private String accountFullId;
    private String accountType;
    private Long clientID;
    private double balance;
    private boolean withdrawAllowed;
}