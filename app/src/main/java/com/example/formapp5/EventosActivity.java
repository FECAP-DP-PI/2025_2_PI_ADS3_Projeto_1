package com.example.formapp5;

import android.content.Intent;
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
import com.example.formapp5.models.Evento;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventosActivity extends AppCompatActivity {
    private static final String TAG = "EventosActivity";

    private LinearLayout layoutEventos;
    private Button btnVoltar, btnCriarEvento;
    private TextView tvSemEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        layoutEventos = findViewById(R.id.layoutEventos);
        btnVoltar = findViewById(R.id.btnVoltar);
        btnCriarEvento = findViewById(R.id.btnCriarEvento);

        btnVoltar.setOnClickListener(v -> finish());

        btnCriarEvento.setOnClickListener(v -> {
            Intent intent = new Intent(EventosActivity.this, CriarEventoActivity.class);
            startActivity(intent);
        });

        carregarEventos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarEventos(); // Recarregar eventos ao voltar
    }

    private void carregarEventos() {
        Log.d(TAG, "=== CARREGANDO EVENTOS ===");

        layoutEventos.removeAllViews();
        tvSemEventos.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.listarEventos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();

                    if (eventos.isEmpty()) {
                        tvSemEventos.setVisibility(View.VISIBLE);
                        tvSemEventos.setText("Nenhum evento cadastrado ainda.");
                        Log.d(TAG, "📭 Nenhum evento encontrado");
                    } else {
                        Log.d(TAG, "✅ " + eventos.size() + " eventos carregados");

                        for (Evento evento : eventos) {
                            adicionarCardEvento(evento);
                        }
                    }
                } else {
                    Toast.makeText(EventosActivity.this,
                            "Erro ao carregar eventos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                tvSemEventos.setVisibility(View.VISIBLE);
                tvSemEventos.setText("Erro ao conectar com o servidor.");

                Log.e(TAG, "❌ Erro ao carregar eventos: " + t.getMessage());
            }
        });
    }

    private void adicionarCardEvento(Evento evento) {
        // Criar CardView programaticamente
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

        // Layout interno
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Nome do evento
        TextView tvNome = new TextView(this);
        tvNome.setText(evento.getNome());
        tvNome.setTextSize(20);
        tvNome.setTextColor(getResources().getColor(android.R.color.black));
        tvNome.setTypeface(null, android.graphics.Typeface.BOLD);
        linearLayout.addView(tvNome);

        // Data
        TextView tvData = new TextView(this);
        tvData.setText("📅 " + evento.getData());
        tvData.setTextSize(16);
        tvData.setPadding(0, 8, 0, 0);
        linearLayout.addView(tvData);

        // Local
        TextView tvLocal = new TextView(this);
        tvLocal.setText("📍 " + evento.getLocal());
        tvLocal.setTextSize(16);
        tvLocal.setPadding(0, 8, 0, 0);
        linearLayout.addView(tvLocal);

        // Descrição (se houver)
        if (evento.getDescricao() != null && !evento.getDescricao().isEmpty()) {
            TextView tvDescricao = new TextView(this);
            tvDescricao.setText(evento.getDescricao());
            tvDescricao.setTextSize(14);
            tvDescricao.setPadding(0, 12, 0, 0);
            linearLayout.addView(tvDescricao);
        }

        cardView.addView(linearLayout);
        layoutEventos.addView(cardView);
    }
}