package com.example.formapp5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.formapp5.api.ApiService;
import com.example.formapp5.api.RetrofitClient;
import com.example.formapp5.models.Organizador;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrganizadoresActivity extends AppCompatActivity {
    private static final String TAG = "OrganizadoresActivity";

    private EditText etNome, etEmail, etSenha;
    private Button btnSalvar, btnVoltarLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizadores);

        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        btnSalvar.setOnClickListener(v -> cadastrarOrganizador());

        btnVoltarLogin.setOnClickListener(v -> {
            Intent intent = new Intent(OrganizadoresActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void cadastrarOrganizador() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        Log.d(TAG, "=== CADASTRANDO ORGANIZADOR VIA API ===");
        Log.d(TAG, "Nome: " + nome);
        Log.d(TAG, "Email: " + email);

        // Validações
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter pelo menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar loading
        progressBar.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(false);

        // Criar objeto Organizador
        Organizador organizador = new Organizador(nome, email, senha);

        // Chamar API
        ApiService apiService = RetrofitClient.getApiService();
        apiService.registrarOrganizador(organizador).enqueue(new Callback<Organizador>() {
            @Override
            public void onResponse(Call<Organizador> call, Response<Organizador> response) {
                progressBar.setVisibility(View.GONE);
                btnSalvar.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Organizador cadastrado com sucesso!");
                    Toast.makeText(OrganizadoresActivity.this,
                            "Cadastro realizado com sucesso!",
                            Toast.LENGTH_LONG).show();

                    // Voltar para login
                    Intent intent = new Intent(OrganizadoresActivity.this, MainActivity.class);
                    intent.putExtra("EMAIL_CADASTRADO", email);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, "❌ Erro ao cadastrar: " + response.code());
                    Toast.makeText(OrganizadoresActivity.this,
                            "Erro: Email já cadastrado ou inválido!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Organizador> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnSalvar.setEnabled(true);

                Log.e(TAG, "❌ Erro de conexão: " + t.getMessage());
                Toast.makeText(OrganizadoresActivity.this,
                        "Erro ao conectar com o servidor. Verifique sua internet.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
