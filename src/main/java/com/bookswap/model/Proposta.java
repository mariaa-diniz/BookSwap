package com.bookswap.model;

import java.time.LocalDateTime;

public class Proposta {

    private int idTroca;
    private String tituloLivroSolicitado;
    private String tituloLivroOfertado;
    private String nomeOutroUsuario;
    private LocalDateTime dataProposta;
    private String status;

    public int getIdTroca() {
        return idTroca;
    }

    public void setIdTroca(int idTroca) {
        this.idTroca = idTroca;
    }

    public String getTituloLivroSolicitado() {
        return tituloLivroSolicitado;
    }

    public void setTituloLivroSolicitado(String tituloLivroSolicitado) {
        this.tituloLivroSolicitado = tituloLivroSolicitado;
    }

    public String getTituloLivroOfertado() {
        return tituloLivroOfertado;
    }

    public void setTituloLivroOfertado(String tituloLivroOfertado) {
        this.tituloLivroOfertado = tituloLivroOfertado;
    }

    public String getNomeOutroUsuario() {
        return nomeOutroUsuario;
    }

    public void setNomeOutroUsuario(String nomeOutroUsuario) {
        this.nomeOutroUsuario = nomeOutroUsuario;
    }

    public LocalDateTime getDataProposta() {
        return dataProposta;
    }

    public void setDataProposta(LocalDateTime dataProposta) {
        this.dataProposta = dataProposta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}