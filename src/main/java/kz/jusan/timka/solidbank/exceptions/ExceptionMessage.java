package kz.jusan.timka.solidbank.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionMessage {
    private String message;
    private int statusCode;


}