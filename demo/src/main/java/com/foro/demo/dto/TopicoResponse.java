package com.foro.demo.dto;

import java.time.Instant;

import com.foro.demo.domain.Tema;
import java.time.Instant;

public class TopicoResponse {
    private Long id;
    private String titulo;
    private String mensaje;     // <- mapea desde Tema.contenido
    private Long autorId;       // <- t.getUsuario().getId()
    private Long cursoId;       // <- t.getCategoria().getId()
    private Instant creadoEn;

    public TopicoResponse() {}

    public TopicoResponse(Tema t) {
        this.id = t.getId();
        this.titulo = t.getTitulo();
        this.mensaje = t.getContenido();               // 👈 importante
        this.autorId = t.getUsuario().getId();
        this.cursoId = t.getCategoria().getId();
        this.creadoEn = t.getCreadoEn();
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }
    public Instant getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Instant creadoEn) { this.creadoEn = creadoEn; }
}

