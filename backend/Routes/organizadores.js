import express from "express";
import bcrypt from "bcrypt";
import db from "../database.js";

const router = express.Router();

router.post("/register", async (req, res) => {
  const { nome, email, senha } = req.body;
  const hashed = await bcrypt.hash(senha, 10);

  db.run(
    "INSERT INTO organizadores (nome, email, senha) VALUES (?, ?, ?)",
    [nome, email, hashed],
    function (err) {
      if (err) return res.status(400).json({ error: "Email já cadastrado!" });
      res.json({ id: this.lastID, nome, email });
    }
  );
});

router.post("/login", (req, res) => {
  const { email, senha } = req.body;
  db.get(
    "SELECT * FROM organizadores WHERE email = ?",
    [email],
    async (err, row) => {
      if (err || !row)
        return res.status(400).json({ error: "Organizador não encontrado" });
      const match = await bcrypt.compare(senha, row.senha);
      if (!match) return res.status(401).json({ error: "Senha incorreta" });
      res.json({ message: "Login realizado com sucesso", organizador: row });
    }
  );
});

export default router;
