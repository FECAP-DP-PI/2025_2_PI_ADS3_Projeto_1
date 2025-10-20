package com.example.formapp5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button btnAlunos = findViewById(R.id.btnAlunos);
        Button btnTurmas = findViewById(R.id.btnTurmas);
        Button btnProfessores = findViewById(R.id.btnProfessores);
        Button btnEventos = findViewById(R.id.btnEventos);
        Button btnPagamentos = findViewById(R.id.btnPagamentos);

        btnAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AlunosActivity.class);
                startActivity(intent);
            }
        });

        btnTurmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, TurmasActivity.class);
                startActivity(intent);
            }
        });
        btnProfessores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfessoresActivity.class);
                startActivity(intent);
            }
        });

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, EventosActivity.class);
                startActivity(intent);
            }
        });

        btnPagamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PagamentosActivity.class);
                startActivity(intent);
            }
        });
}}
