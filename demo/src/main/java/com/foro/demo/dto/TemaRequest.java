package com.foro.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TemaRequest {
    @NotBlank private String titulo;
    @NotBlank private String contenido;
    @NotNull private Long usuarioId;
    @NotNull private Long categoriaId;


    // getters/setters

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }
}