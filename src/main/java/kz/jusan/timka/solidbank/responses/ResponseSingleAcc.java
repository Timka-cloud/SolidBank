package kz.jusan.timka.solidbank.responses;


import kz.jusan.timka.solidbank.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class ResponseSingleAcc {
    private String message;
    private int statusCode;
    private AccountDto accountDto;
}