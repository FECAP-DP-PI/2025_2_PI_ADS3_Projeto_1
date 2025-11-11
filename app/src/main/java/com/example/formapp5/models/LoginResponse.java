package com.example.formapp5.models;

public class LoginResponse {
    private String message;
    private Aluno aluno;
    private Organizador organizador;

    public String getMessage() { return message; }
    public Aluno getAluno() { return aluno; }
    public Organizador getOrganizador() { return organizador; }
}