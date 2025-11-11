package com.example.formapp5;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.formapp5.api.ApiService;
import com.example.formapp5.api.RetrofitClient;
import com.example.formapp5.models.Pagamento;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagamentosActivity extends AppCompatActivity {
    private static final String TAG = "PagamentosActivity";

    private LinearLayout layoutPagamentos;
    private ProgressBar progressBar;
    private Button btnVoltar;
    private TextView tvSemPagamentos, tvTotalPago, tvTotalPendente;

    private int alunoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamentos);

        // Recuperar ID do aluno logado
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        alunoId = prefs.getInt("usuario_id", 0);

        if (alunoId == 0) {
            Toast.makeText(this, "Erro: Usuário não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        layoutPagamentos = findViewById(R.id.layoutPagamentos);
        progressBar = findViewById(R.id.progressBar);
        btnVoltar = findViewById(R.id.btnVoltar);
        tvSemPagamentos = findViewById(R.id.tvSemPagamentos);
        tvTotalPago = findViewById(R.id.tvTotalPago);
        tvTotalPendente = findViewById(R.id.tvTotalPendente);

        btnVoltar.setOnClickListener(v -> finish());

        carregarPagamentos();
    }

    private void carregarPagamentos() {
        Log.d(TAG, "=== CARREGANDO PAGAMENTOS DO ALUNO " + alunoId + " ===");

        progressBar.setVisibility(View.VISIBLE);
        layoutPagamentos.removeAllViews();
        tvSemPagamentos.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.getPagamentosPorAluno(alunoId).enqueue(new Callback<List<Pagamento>>() {
            @Override
            public void onResponse(Call<List<Pagamento>> call, Response<List<Pagamento>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Pagamento> pagamentos = response.body();

                    if (pagamentos.isEmpty()) {
                        tvSemPagamentos.setVisibility(View.VISIBLE);
                        tvSemPagamentos.setText("Você ainda não possui pagamentos.");
                        Log.d(TAG, "📭 Nenhum pagamento encontrado");
                    } else {
                        Log.d(TAG, "✅ " + pagamentos.size() + " pagamentos carregados");

                        double totalPago = 0;
                        double totalPendente = 0;

                        for (Pagamento pagamento : pagamentos) {
                            adicionarCardPagamento(pagamento);

                            if ("pago".equalsIgnoreCase(pagamento.getStatus())) {
                                totalPago += pagamento.getValor();
                            } else {
                                totalPendente += pagamento.getValor();
                            }
                        }

                        // Atualizar totais
                        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                        tvTotalPago.setText("Total Pago: " + formatoMoeda.format(totalPago));
                        tvTotalPendente.setText("Total Pendente: " + formatoMoeda.format(totalPendente));
                    }
                } else {
                    Toast.makeText(PagamentosActivity.this,
                            "Erro ao carregar pagamentos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pagamento>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                tvSemPagamentos.setVisibility(View.VISIBLE);
                tvSemPagamentos.setText("Erro ao conectar com o servidor.");

                Log.e(TAG, "❌ Erro ao carregar pagamentos: " + t.getMessage());
            }
        });
    }

    private void adicionarCardPagamento(Pagamento pagamento) {
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 24);
        cardView.setLayoutParams(cardParams);
        cardView.setCardElevation(8);
        cardView.setRadius(12);
        cardView.setContentPadding(32, 32, 32, 32);

        // Cor diferente para status
        if ("pago".equalsIgnoreCase(pagamento.getStatus())) {
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else if ("pendente".equalsIgnoreCase(pagamento.getStatus())) {
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Valor
        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        TextView tvValor = new TextView(this);
        tvValor.setText("💰 " + formatoMoeda.format(pagamento.getValor()));
        tvValor.setTextSize(20);
        tvValor.setTextColor(getResources().getColor(android.R.color.black));
        tvValor.setTypeface(null, android.graphics.Typeface.BOLD);
        linearLayout.addView(tvValor);

        // Status
        TextView tvStatus = new TextView(this);
        tvStatus.setText("Status: " + pagamento.getStatus().toUpperCase());
        tvStatus.setTextSize(16);
        tvStatus.setPadding(0, 8, 0, 0);
        linearLayout.addView(tvStatus);

        // Data
        TextView tvData = new TextView(this);
        tvData.setText("📅 " + pagamento.getDataPagamento());
        tvData.setTextSize(14);
        tvData.setPadding(0, 8, 0, 0);
        linearLayout.addView(tvData);

        cardView.addView(linearLayout);
        layoutPagamentos.addView(cardView);
    }
}