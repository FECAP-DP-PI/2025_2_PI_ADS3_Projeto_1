package com.example.formapp5.models;

public class Evento {
    private int id;
    private String nome;
    private String data;
    private String local;
    private String descricao;
    private int organizador_id;

    public Evento(String nome, String data, String local, String descricao, int organizador_id) {
        this.nome = nome;
        this.data = data;
        this.local = local;
        this.descricao = descricao;
        this.organizador_id = organizador_id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getOrganizadorId() { return organizador_id; }
    public void setOrganizadorId(int organizador_id) { this.organizador_id = organizador_id; }
}