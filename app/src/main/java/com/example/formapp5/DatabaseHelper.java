package com.example.formapp5;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "formatura.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOME = "Nome Completo";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_SENHA = "senha";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_SENHA + " TEXT NOT NULL)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // cria a tabela quando o app roda pela primeira vez
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        onCreate(db);
    }

    // aqui cadastra o usuario
    public boolean cadastrarUsuario(String nome, String email, String senha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SENHA, senha);

        // tenta inserir no banco
        long result = db.insert(TABLE_USUARIOS, null, values);
        db.close();

        return result != -1;
    }

    // aqui verifica se o email ja existe
    public boolean emailJaExiste(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // procura no banco
        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{COLUMN_EMAIL},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null
        );

        boolean existe = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return existe;
    }

    //faz login e verifica email e senha
    public boolean fazerLogin(String email, String senha) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USUARIOS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_SENHA + "=?",
                new String[]{email, senha},
                null, null, null
        );

        boolean loginValido = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return loginValido;
    }

    public void listarTodosUsuarios() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIOS, null);

            android.util.Log.d("BANCO_DADOS", "========== LISTA DE USUÁRIOS ==========");

            if (cursor != null && cursor.moveToFirst()) {
                int contador = 0;

                // Pegar os índices das colunas COM PROTEÇÃO
                int idxId = cursor.getColumnIndex(COLUMN_ID);
                int idxNome = cursor.getColumnIndex("nome");
                int idxEmail = cursor.getColumnIndex(COLUMN_EMAIL);
                int idxSenha = cursor.getColumnIndex(COLUMN_SENHA);

                do {
                    contador++;

                    // Verificar se os índices são válidos antes de usar
                    int id = (idxId >= 0) ? cursor.getInt(idxId) : -1;
                    String nome = (idxNome >= 0) ? cursor.getString(idxNome) : "N/A";
                    String email = (idxEmail >= 0) ? cursor.getString(idxEmail) : "N/A";
                    String senha = (idxSenha >= 0) ? cursor.getString(idxSenha) : "N/A";

                    android.util.Log.d("BANCO_DADOS", "Usuário " + contador + ":");
                    android.util.Log.d("BANCO_DADOS", "  ID: " + id);
                    android.util.Log.d("BANCO_DADOS", "  Nome: " + nome);
                    android.util.Log.d("BANCO_DADOS", "  Email: " + email);
                    android.util.Log.d("BANCO_DADOS", "  Senha: " + senha);
                    android.util.Log.d("BANCO_DADOS", "-------------------");
                } while (cursor.moveToNext());

                android.util.Log.d("BANCO_DADOS", "Total de usuários: " + contador);
            } else {
                android.util.Log.d("BANCO_DADOS", "⚠️ Banco vazio! Nenhum usuário cadastrado.");
            }
        } catch (Exception e) {
            android.util.Log.e("BANCO_DADOS", "❌ ERRO ao listar usuários: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        android.util.Log.d("BANCO_DADOS", "=======================================");
    }}