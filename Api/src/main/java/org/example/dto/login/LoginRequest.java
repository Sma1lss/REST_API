package org.example.dto.login;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
}
