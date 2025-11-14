import express from "express";
import db from "../database.js";

const router = express.Router();

router.post("/", (req, res) => {
  const { aluno_id, evento_id, valor, data_pagamento, status } = req.body;
  db.run(
    "INSERT INTO pagamentos (aluno_id, evento_id, valor, data_pagamento, status) VALUES (?, ?, ?, ?, ?)",
    [aluno_id, evento_id, valor, data_pagamento, status],
    function (err) {
      if (err) return res.status(400).json({ error: err.message });
      res.json({ id: this.lastID, aluno_id, evento_id, valor, status });
    }
  );
});

router.get("/", (req, res) => {
  db.all("SELECT * FROM pagamentos", [], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json(rows);
  });
});

export default router;
