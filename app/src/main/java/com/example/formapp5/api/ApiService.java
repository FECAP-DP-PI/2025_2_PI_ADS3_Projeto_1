package com.example.formapp5.api;

import com.example.formapp5.models.*;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface ApiService {

    @POST("alunos/register")
    Call<Aluno> registrarAluno(@Body Aluno aluno);

    @POST("alunos/login")
    Call<LoginResponse> loginAluno(@Body LoginRequest request);

    @GET("alunos/{id}")
    Call<Aluno> getAlunoById(@Path("id") int id);

    @POST("organizadores/register")
    Call<Organizador> registrarOrganizador(@Body Organizador organizador);

    @POST("organizadores/login")
    Call<LoginResponse> loginOrganizador(@Body LoginRequest request);

    @POST("eventos")
    Call<Evento> criarEvento(@Body Evento evento);

    @GET("eventos")
    Call<List<Evento>> listarEventos();

    @GET("eventos/{id}")
    Call<Evento> getEventoById(@Path("id") int id);

    @GET("eventos/organizador/{organizador_id}")
    Call<List<Evento>> getEventosPorOrganizador(@Path("organizador_id") int organizadorId);

    @PUT("eventos/{id}")
    Call<Evento> atualizarEvento(@Path("id") int id, @Body Evento evento);

    @DELETE("eventos/{id}")
    Call<Void> deletarEvento(@Path("id") int id);

    @POST("pagamentos")
    Call<Pagamento> criarPagamento(@Body Pagamento pagamento);

    @GET("pagamentos/aluno/{aluno_id}")
    Call<List<Pagamento>> getPagamentosPorAluno(@Path("aluno_id") int alunoId);

    @GET("pagamentos/evento/{evento_id}")
    Call<List<Pagamento>> getPagamentosPorEvento(@Path("evento_id") int eventoId);

    @PUT("pagamentos/{id}")
    Call<Pagamento> atualizarPagamento(@Path("id") int id, @Body Pagamento pagamento);
}
