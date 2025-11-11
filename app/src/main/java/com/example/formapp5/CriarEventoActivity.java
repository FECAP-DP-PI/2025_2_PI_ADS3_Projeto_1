package com.example.formapp5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formapp5.api.ApiService;
import com.example.formapp5.api.RetrofitClient;
import com.example.formapp5.models.Evento;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CriarEventoActivity extends AppCompatActivity {
    private static final String TAG = "CriarEventoActivity";

    private EditText etNomeEvento, etDataEvento, etLocalEvento, etDescricaoEvento;
    private Button btnSalvarEvento, btnVoltar;

    private int organizadorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_evento);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        organizadorId = prefs.getInt("usuario_id", 0);

        if (organizadorId == 0) {
            Toast.makeText(this, "Erro: Usuário não identificado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etNomeEvento = findViewById(R.id.etNomeEvento);
        etDataEvento = findViewById(R.id.etDataEvento);
        etLocalEvento = findViewById(R.id.etLocalEvento);
        etDescricaoEvento = findViewById(R.id.etDescricaoEvento);
        btnSalvarEvento = findViewById(R.id.btnSalvarEvento);
        btnVoltar = findViewById(R.id.btnVoltar);

        btnSalvarEvento.setOnClickListener(v -> criarEvento());

        btnVoltar.setOnClickListener(v ->  {
            Intent i = new Intent(CriarEventoActivity.this, EventosActivity.class);

                finish();
        });
    }

    private void criarEvento() {
        String nome = etNomeEvento.getText().toString().trim();
        String data = etDataEvento.getText().toString().trim();
        String local = etLocalEvento.getText().toString().trim();
        String descricao = etDescricaoEvento.getText().toString().trim();

        Log.d(TAG, "=== CRIANDO EVENTO VIA API ===");
        Log.d(TAG, "Nome: " + nome);
        Log.d(TAG, "Data: " + data);
        Log.d(TAG, "Local: " + local);

        if (nome.isEmpty() || data.isEmpty() || local.isEmpty()) {
            Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!data.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Toast.makeText(this, "Data deve estar no formato DD/MM/YYYY", Toast.LENGTH_SHORT).show();
            return;
        }
        btnSalvarEvento.setEnabled(false);

        Evento evento = new Evento(nome, data, local, descricao, organizadorId);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.criarEvento(evento).enqueue(new Callback<Evento>() {
            @Override
            public void onResponse(Call<Evento> call, Response<Evento> response) {
                btnSalvarEvento.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "com.example.formapp5.models.Evento criado com sucesso!");
                    Toast.makeText(CriarEventoActivity.this,
                            "com.example.formapp5.models.Evento criado com sucesso!",
                            Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Log.e(TAG, "Erro ao criar evento: " + response.code());
                    Toast.makeText(CriarEventoActivity.this,
                            "Erro ao criar evento. Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Evento> call, Throwable t) {
                btnSalvarEvento.setEnabled(true);

                Log.e(TAG, "Erro de conexão: " + t.getMessage());
                Toast.makeText(CriarEventoActivity.this,
                        "Erro ao conectar com o servidor.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
