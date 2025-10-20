package com.example.formapp5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etEmail, etSenha;
    private Button btnEntrar, btnCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // inicializa banco de dados
        dbHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCadastro = findViewById(R.id.btnCadastro);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fazerLogin();
            }
        });

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(intent);
            }
        });

        // essa parte faz o login receber os dados do cadastro
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("EMAIL_CADASTRADO")) {
            String emailCadastrado = intent.getStringExtra("EMAIL_CADASTRADO");
            String nomeCadastrado = intent.getStringExtra("NOME_CADASTRADO");

            if (emailCadastrado != null && !emailCadastrado.isEmpty()) {
                etEmail.setText(emailCadastrado);
                Toast.makeText(this, "Bem-vindo, " + nomeCadastrado + "! Faça seu login.", Toast.LENGTH_LONG).show();
                etSenha.requestFocus();
            }
        }
    }

    private void fazerLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        //verifica se preencheu tudo certo
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // verifica no banco se login é válido
        boolean loginValido = dbHelper.fazerLogin(email, senha);

        if (loginValido) {
            Toast.makeText(this, "Login realizado", Toast.LENGTH_SHORT).show();

            //vai para o Dashboard
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            intent.putExtra("EMAIL_USUARIO", email);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Email ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }
}