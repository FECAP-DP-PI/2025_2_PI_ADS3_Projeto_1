import sqlite3 from "sqlite3";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const dbPath = path.resolve(__dirname, "../db/database.sqlite"); 

const db = new sqlite3.Database(dbPath, (err) => {
  if (err) {
    console.error("Erro ao conectar ao banco:", err.message);
  } else {
    console.log(" Conectado ao banco de dados SQLite");
  }
});

db.serialize(() => {
  db.run(`CREATE TABLE IF NOT EXISTS alunos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    email TEXT UNIQUE,
    senha TEXT,
    matricula TEXT
  )`);

  db.run(`CREATE TABLE IF NOT EXISTS organizadores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    email TEXT UNIQUE,
    senha TEXT
  )`);

  db.run(`CREATE TABLE IF NOT EXISTS eventos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT,
    data TEXT,
    local TEXT,
    descricao TEXT,
    organizador_id INTEGER,
    FOREIGN KEY (organizador_id) REFERENCES organizadores(id)
  )`);

  db.run(`CREATE TABLE IF NOT EXISTS pagamentos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    aluno_id INTEGER,
    evento_id INTEGER,
    valor REAL,
    data_pagamento TEXT,
    status TEXT,
    FOREIGN KEY (aluno_id) REFERENCES alunos(id),
    FOREIGN KEY (evento_id) REFERENCES eventos(id)
  )`);
});

export default db;
