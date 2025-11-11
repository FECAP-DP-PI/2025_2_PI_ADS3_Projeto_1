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


public class AlunosActivity extends AppCompatActivity {

    private ImageView ivFotoPerfil;
    private TextView tvNomeCompleto, tvMatricula, tvEmail, tvTurma;
    private Button btnPagamentos, btnEventos, btnEditarPerfil, btnSair;
    private SharedPreferences sharedPreferences;
    private int alunoId;
    private String nomeAluno, emailAluno, telefoneAluno, nomeTurma;
    private int turmaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alunos);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        inicializarViews();

        carregarDadosUsuario();

        configurarBotoes();

        btnEventos.setOnClickListener(v -> {
            Intent i = new Intent(AlunosActivity.this, EventosActivity.class);
            startActivity(i);
        });

        btnPagamentos.setOnClickListener(v -> {
            Intent i = new Intent(AlunosActivity.this, PagamentosActivity.class);
            startActivity(i);
        });
    }

    private void inicializarViews() {
        ivFotoPerfil = findViewById(R.id.ivFotoPerfil);
        tvNomeCompleto = findViewById(R.id.tvNomeCompleto);
        tvMatricula = findViewById(R.id.tvMatricula);
        tvEmail = findViewById(R.id.tvEmail);
        tvTurma = findViewById(R.id.tvTurma);

        btnPagamentos = findViewById(R.id.btnPagamentos);
        btnEventos = findViewById(R.id.btnEventos);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnSair = findViewById(R.id.btnSair);
    }


    private void carregarDadosUsuario() {
        Intent intent = getIntent();

        if (intent.hasExtra("aluno_id")) {

            alunoId = intent.getIntExtra("aluno_id", 0);
            nomeAluno = intent.getStringExtra("nome");
            emailAluno = intent.getStringExtra("email");
            telefoneAluno = intent.getStringExtra("telefone");
            turmaId = intent.getIntExtra("turma_id", 0);
            nomeTurma = intent.getStringExtra("turma_nome");

            salvarDadosLocal();
        } else {
            carregarDadosLocal();
        }
        exibirDados();
    }

    private void salvarDadosLocal() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("aluno_id", alunoId);
        editor.putString("nome", nomeAluno);
        editor.putString("email", emailAluno);
        editor.putInt("turma_id", turmaId);
        editor.putString("turma_nome", nomeTurma);
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }

    private void carregarDadosLocal() {
        alunoId = sharedPreferences.getInt("aluno_id", 0);
        nomeAluno = sharedPreferences.getString("nome", "com.example.formapp5.models.Aluno");
        emailAluno = sharedPreferences.getString("email", "email@exemplo.com");
        turmaId = sharedPreferences.getInt("turma_id", 0);
        nomeTurma = sharedPreferences.getString("turma_nome", "Sem turma");
    }

    private void exibirDados() {
        tvNomeCompleto.setText(nomeAluno);
        tvMatricula.setText(String.valueOf(alunoId));
        tvEmail.setText(emailAluno);
        tvTurma.setText(nomeTurma != null ? nomeTurma : "Sem turma");
    }

    private void configurarBotoes() {

        btnPagamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlunosActivity.this, PagamentosActivity.class);
                intent.putExtra("aluno_id", alunoId);
                intent.putExtra("nome_aluno", nomeAluno);
                startActivity(intent);
            }
        });

        btnEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlunosActivity.this, EventosActivity.class);
                intent.putExtra("turma_id", turmaId);
                intent.putExtra("aluno_id", alunoId);
                startActivity(intent);
            }
        });

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AlunosActivity.this, "Função em desenvolvimento", Toast.LENGTH_SHORT).show();
            }
        });

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoSair();
            }
        });
    }

    private void mostrarDialogoSair() {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Deseja realmente sair da sua conta?")
                .setPositiveButton("Sim", (dialog, which) -> {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();


                    Intent intent = new Intent(AlunosActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarDadosLocal();
        exibirDados();
    }
}