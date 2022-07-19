package kz.jusan.timka.solidbank.responses;

import kz.jusan.timka.solidbank.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ResponseManyAcc {
    private String message;
    private int statusCode;
    private List<AccountDto> accountList;

}