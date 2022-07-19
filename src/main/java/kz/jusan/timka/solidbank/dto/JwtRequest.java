package kz.jusan.timka.solidbank.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}