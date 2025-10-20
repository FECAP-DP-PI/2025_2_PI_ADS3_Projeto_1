package com.example.formapp5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText etNome, etEmail, etSenha, etConfirmarSenha;
    private Button btnRegistrar, btnVoltarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // Inicializar banco de dados
        dbHelper = new DatabaseHelper(this);

        // Conectar com o layout
        etNome = findViewById(R.id.etNome);
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        etConfirmarSenha = findViewById(R.id.etConfirmaSenha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnVoltarLogin = findViewById(R.id.btnVoltarLogin);

        btnRegistrar.setOnClickListener(v -> {
            cadastrarUsuario(); // chama a função que valida e cadastra
        });


        btnVoltarLogin.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void cadastrarUsuario() {

        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // verifica se preencheu tudo
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // verifica se as senhas são iguais
        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem!", Toast.LENGTH_SHORT).show();
            return; // PARA AQUI!
        }


        // verifica se o email ja foi cadastrado antes
        if (dbHelper.emailJaExiste(email)) {
            Toast.makeText(this, "Este email já está cadastrado!", Toast.LENGTH_SHORT).show();
            return;
        }

        // cadastra no banco de dados
        boolean sucesso = dbHelper.cadastrarUsuario(nome, email, senha);

        if (sucesso) {
            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();

            // coloca os dados na tela de login
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            intent.putExtra("EMAIL_CADASTRADO", email);
            intent.putExtra("NOME_CADASTRADO", nome);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erro ao cadastrar no banco. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}