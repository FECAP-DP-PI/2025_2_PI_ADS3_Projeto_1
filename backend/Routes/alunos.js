import express from "express";
import bcrypt from "bcrypt";
import db from "../database.js";

const router = express.Router();

router.post("/register", async (req, res) => {
  const { nome, email, senha, matricula } = req.body;
  const hashed = await bcrypt.hash(senha, 10);

  db.run(
    "INSERT INTO alunos (nome, email, senha, matricula) VALUES (?, ?, ?, ?)",
    [nome, email, hashed, matricula],
    function (err) {
      if (err) return res.status(400).json({ error: "Email já cadastrado!" });
      res.json({ id: this.lastID, nome, email, matricula });
    }
  );
});

router.post("/login", (req, res) => {
  const { email, senha } = req.body;
  db.get("SELECT * FROM alunos WHERE email = ?", [email], async (err, row) => {
    if (err || !row)
      return res.status(400).json({ error: "Aluno não encontrado" });
    const match = await bcrypt.compare(senha, row.senha);
    if (!match) return res.status(401).json({ error: "Senha incorreta" });
    res.json({ message: "Login realizado com sucesso", aluno: row });
  });
});

router.get("/:id", (req, res) => {
  const { id } = req.params;
  db.get("SELECT * FROM alunos WHERE id = ?", [id], (err, row) => {
    if (err || !row)
      return res.status(404).json({ error: "Aluno não encontrado" });
    res.json(row);
  });
});

export default router;
