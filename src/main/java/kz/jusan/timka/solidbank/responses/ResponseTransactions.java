package kz.jusan.timka.solidbank.responses;

import kz.jusan.timka.solidbank.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ResponseTransactions {
    private String message;
    private int statusCode;
    private List<TransactionDto> transactionDtoList;
}