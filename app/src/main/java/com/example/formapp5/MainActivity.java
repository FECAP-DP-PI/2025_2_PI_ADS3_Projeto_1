package com.example.formapp5;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.formapp5.models.Aluno;
import com.example.formapp5.models.LoginRequest;
import com.example.formapp5.models.LoginResponse;
import com.example.formapp5.models.Organizador;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText etEmail, etSenha;
    private Button btnEntrar, btnSouOrganizador, btnSouAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar views
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnSouOrganizador = findViewById(R.id.btnSouOrganizador);
        btnSouAluno = findViewById(R.id.btnSouAluno);

        // Verificar se já está logado
        verificarSeJaEstaLogado();

        // Se veio do cadastro, preencher email
        Intent intent = getIntent();
        if (intent.hasExtra("EMAIL_CADASTRADO")) {
            etEmail.setText(intent.getStringExtra("EMAIL_CADASTRADO"));
            Toast.makeText(this, "Cadastro realizado! Faça login.", Toast.LENGTH_LONG).show();
        }

        // Botão Entrar
        btnEntrar.setOnClickListener(v -> fazerLogin());

        // Botão Cadastro Organizador
        btnSouOrganizador.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, OrganizadoresActivity.class);
            startActivity(i);
        });

        // Botão Cadastro Aluno
        btnSouAluno.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, CadastroActivity.class);
            startActivity(i);
        });
    }

    private void verificarSeJaEstaLogado() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        if (isLoggedIn) {
            String tipoUsuario = prefs.getString("tipo_usuario", "");
            Log.d(TAG, "Usuário já logado como: " + tipoUsuario);

            if (tipoUsuario.equals("aluno")) {
                irParaDashboardAluno();
            } else if (tipoUsuario.equals("organizador")) {
                irParaDashboardOrganizador();
            }
        }
    }

    private void fazerLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        // ✅ ADICIONE ESTAS LINHAS DE LOG
        Log.d("TESTE_LOGIN", "========================================");
        Log.d("TESTE_LOGIN", "Método fazerLogin() foi chamado!");
        Log.d("TESTE_LOGIN", "Email digitado: " + email);
        Log.d("TESTE_LOGIN", "Senha digitada: " + senha);
        Log.d("TESTE_LOGIN", "========================================");


        // Validações
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
            return;
        }

        btnEntrar.setEnabled(false);

        // Criar objeto de login
        LoginRequest loginRequest = new LoginRequest(email, senha);

        // Tentar login como aluno
        ApiService apiService = RetrofitClient.getApiService();
        apiService.loginAluno(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Aluno aluno = loginResponse.getAluno();

                    if (aluno != null) {
                        Log.d(TAG, "✅ Login como ALUNO bem-sucedido!");

                        btnEntrar.setEnabled(true);

                        salvarDadosAluno(
                                aluno.getId(),
                                aluno.getNome(),
                                aluno.getEmail(),
                                "",
                                aluno.getMatricula(),
                                0
                        );

                        Toast.makeText(MainActivity.this,
                                "Bem-vindo(a), " + aluno.getNome() + "!",
                                Toast.LENGTH_SHORT).show();

                        irParaDashboardAluno();
                        return;
                    }
                }
                tentarLoginOrganizador(loginRequest);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "❌ Erro ao tentar login como aluno: " + t.getMessage());
                tentarLoginOrganizador(loginRequest);
            }
        });
    }

    private void tentarLoginOrganizador(LoginRequest loginRequest) {
        ApiService apiService = RetrofitClient.getApiService();

        apiService.loginOrganizador(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnEntrar.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Organizador organizador = loginResponse.getOrganizador();

                    if (organizador != null) {
                        Log.d(TAG, "✅ Login como ORGANIZADOR bem-sucedido!");

                        salvarDadosOrganizador(
                                organizador.getId(),
                                organizador.getNome(),
                                organizador.getEmail()
                        );

                        Toast.makeText(MainActivity.this,
                                "Bem-vindo(a), " + organizador.getNome() + "!",
                                Toast.LENGTH_SHORT).show();

                        irParaDashboardOrganizador();
                        return;
                    }
                }

                Log.e(TAG, "❌ Email ou senha incorretos");
                Toast.makeText(MainActivity.this,
                        "Email ou senha incorretos",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnEntrar.setEnabled(true);

                Log.e(TAG, "❌ Erro de conexão: " + t.getMessage());
                Toast.makeText(MainActivity.this,
                        "Erro ao conectar com o servidor. Verifique sua internet.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void salvarDadosAluno(int id, String nome, String email, String telefone, String matricula, Integer turmaId) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("usuario_id", id);
        editor.putString("nome", nome);
        editor.putString("email", email);
        editor.putString("telefone", telefone);
        editor.putString("matricula", matricula);
        editor.putString("tipo_usuario", "aluno");

        if (turmaId != null && turmaId > 0) {
            editor.putInt("turma_id", turmaId);
        }

        editor.putBoolean("is_logged_in", true);
        editor.apply();

        Log.d(TAG, "💾 Dados do ALUNO salvos: " + nome);
    }

    private void salvarDadosOrganizador(int id, String nome, String email) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("usuario_id", id);
        editor.putString("nome", nome);
        editor.putString("email", email);
        editor.putString("tipo_usuario", "organizador");
        editor.putBoolean("is_logged_in", true);
        editor.apply();

        Log.d(TAG, "💾 Dados do ORGANIZADOR salvos: " + nome);
    }

    private void irParaDashboardAluno() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        Intent intent = new Intent(MainActivity.this, AlunosActivity.class);
        intent.putExtra("aluno_id", prefs.getInt("usuario_id", 0));
        intent.putExtra("nome", prefs.getString("nome", ""));
        intent.putExtra("email", prefs.getString("email", ""));
        intent.putExtra("telefone", prefs.getString("telefone", ""));
        intent.putExtra("matricula", prefs.getString("matricula", ""));
        intent.putExtra("turma_id", prefs.getInt("turma_id", 0));

        startActivity(intent);
        finish();
    }

    private void irParaDashboardOrganizador() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        intent.putExtra("organizador_id", prefs.getInt("usuario_id", 0));
        intent.putExtra("nome", prefs.getString("nome", ""));
        intent.putExtra("email", prefs.getString("email", ""));

        startActivity(intent);
        finish();
    }
}
