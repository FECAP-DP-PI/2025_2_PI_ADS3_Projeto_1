package com.example.formapp5.models;

public class Pagamento {
    private int id;
    private int aluno_id;
    private int evento_id;
    private double valor;
    private String data_pagamento;
    private String status;

    public Pagamento(int aluno_id, int evento_id, double valor, String data_pagamento, String status) {
        this.aluno_id = aluno_id;
        this.evento_id = evento_id;
        this.valor = valor;
        this.data_pagamento = data_pagamento;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAlunoId() { return aluno_id; }
    public void setAlunoId(int aluno_id) { this.aluno_id = aluno_id; }

    public int getEventoId() { return evento_id; }
    public void setEventoId(int evento_id) { this.evento_id = evento_id; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getDataPagamento() { return data_pagamento; }
    public void setDataPagamento(String data_pagamento) { this.data_pagamento = data_pagamento; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}