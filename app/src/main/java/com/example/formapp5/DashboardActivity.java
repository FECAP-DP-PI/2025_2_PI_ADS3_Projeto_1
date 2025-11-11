package com.example.formapp5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvTotalAlunos, tvPagamentosPendentes, tvPagamentosRecebidos, tvProximoEvento;
    private Button btnGerenciarAlunos, btnCriarEvento, btnPagamentos;
    private Button btnRelatorios, btnProfessores, btnTurmas;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvTotalAlunos = findViewById(R.id.tvTotalAlunos);
        tvPagamentosPendentes = findViewById(R.id.tvPagamentosPendentes);
        tvPagamentosRecebidos = findViewById(R.id.tvPagamentosRecebidos);
        tvProximoEvento = findViewById(R.id.tvProximoEvento);

        btnGerenciarAlunos = findViewById(R.id.btnGerenciarAlunos);
        btnCriarEvento = findViewById(R.id.btnCriarEvento);
        btnPagamentos = findViewById(R.id.btnPagamentos);
        btnRelatorios = findViewById(R.id.btnRelatorios);
        btnProfessores = findViewById(R.id.btnProfessores);
        btnTurmas = findViewById(R.id.btnTurmas);
    }


    private void configurarBotoes() {
        btnGerenciarAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Função em desenvolvimento", Toast.LENGTH_SHORT).show();
            }
        });

        btnCriarEvento.setOnClickListener(v -> {
            Intent i = new Intent(DashboardActivity.this, CriarEventoActivity.class);
            startActivity(i);
        });

        btnPagamentos.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PagamentosActivity.class);
            startActivity(intent);
        });

        btnRelatorios.setOnClickListener(v -> {
            Toast.makeText(this, "Relatórios em desenvolvimento", Toast.LENGTH_SHORT).show();
        });
    }

    private void mostrarDialogoSair() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    // Limpar SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Não", null)
                .show();
    }}