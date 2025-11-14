import express from "express";
import db from "../database.js";

const router = express.Router();

router.get("/", (req, res) => {
  db.all("SELECT * FROM eventos", [], (err, rows) => {
    if (err) return res.status(500).json({ error: err.message });
    res.json(rows);
  });
});

router.post("/new", (req, res) => {
  const { nome, data, local, descricao, organizador_id } = req.body;
  db.run(
    "INSERT INTO eventos (nome, data, local, descricao, organizador_id) VALUES (?, ?, ?, ?, ?)",
    [nome, data, local, descricao, organizador_id],
    function (err) {
      if (err) return res.status(400).json({ error: err.message });
      res.json({ id: this.lastID, nome, data, local, descricao });
    }
  );
});

export default router;
