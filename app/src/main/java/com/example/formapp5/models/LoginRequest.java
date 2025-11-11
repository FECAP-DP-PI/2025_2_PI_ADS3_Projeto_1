package com.example.formapp5.models;

public class LoginRequest {
    private String email;
    private String senha;

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}