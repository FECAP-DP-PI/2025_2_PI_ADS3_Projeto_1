package com.example.formapp5.models;

public class Aluno {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String matricula;

    public Aluno() {}

    public Aluno(String nome, String email, String senha, String matricula) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.matricula = matricula;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
}