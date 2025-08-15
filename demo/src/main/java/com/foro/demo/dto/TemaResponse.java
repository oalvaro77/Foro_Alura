package com.foro.demo.dto;

import java.time.Instant;

public class TemaResponse {
    private Long id;
    private String titulo;
    private String contenido;
    private Long usuarioId;
    private Long categoriaId;
    private Instant creadoEn;


    // getters/setters

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(Instant creadoEn) {
        this.creadoEn = creadoEn;
    }

    public void setId(Long id) {

    }

    public void setTitulo(String titulo) {

    }
}

