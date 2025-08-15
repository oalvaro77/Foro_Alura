package com.foro.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TopicoRequest {
    @NotBlank @Size(max = 200)
    private String titulo;

    @NotBlank
    private String mensaje; // se mapeará a Tema.contenido

    @NotNull
    private Long autorId;   // Usuario existente

    @NotNull
    private Long cursoId;   // Categoria existente

    // getters y setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
}
