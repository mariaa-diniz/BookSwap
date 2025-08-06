package com.bookswap.model;

import java.time.LocalDateTime;

public class Troca {

    private int id;
    private int idLivroSolicitado;
    private int idUsuarioSolicitante;
    private int idLivroOfertado;
    private int idUsuarioOfertado;
    private String status;
    private LocalDateTime dataProposta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLivroSolicitado() {
        return idLivroSolicitado;
    }

    public void setIdLivroSolicitado(int idLivroSolicitado) {
        this.idLivroSolicitado = idLivroSolicitado;
    }

    public int getIdUsuarioSolicitante() {
        return idUsuarioSolicitante;
    }

    public void setIdUsuarioSolicitante(int idUsuarioSolicitante) {
        this.idUsuarioSolicitante = idUsuarioSolicitante;
    }

    public int getIdLivroOfertado() {
        return idLivroOfertado;
    }

    public void setIdLivroOfertado(int idLivroOfertado) {
        this.idLivroOfertado = idLivroOfertado;
    }

    public int getIdUsuarioOfertado() {
        return idUsuarioOfertado;
    }

    public void setIdUsuarioOfertado(int idUsuarioOfertado) {
        this.idUsuarioOfertado = idUsuarioOfertado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataProposta() {
        return dataProposta;
    }

    public void setDataProposta(LocalDateTime dataProposta) {
        this.dataProposta = dataProposta;
    }
}