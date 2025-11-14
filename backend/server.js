import express from "express";
import cors from "cors";
import bodyParser from "body-parser";

import alunosRoutes from "./routes/alunos.js";
import organizadoresRoutes from "./routes/organizadores.js";
import eventosRoutes from "./routes/eventos.js";
import pagamentosRoutes from "./routes/pagamentos.js";

const app = express();
const PORT = 3000;

app.use(cors());
app.use(bodyParser.json());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.use("/api/alunos", alunosRoutes);
app.use("/api/organizadores", organizadoresRoutes);
app.use("/api/eventos", eventosRoutes);
app.use("/api/pagamentos", pagamentosRoutes);

app.listen(PORT, () => {
  console.log(`Servidor rodando em http://localhost:${PORT}`);
});
app.get("/", (req, res) => {
  res.send(" Servidor do app de formatura está rodando!");
});
