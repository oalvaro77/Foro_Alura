package com.foro.demo.service;

import com.foro.demo.domain.Categoria;
import com.foro.demo.domain.Tema;
import com.foro.demo.domain.Usuario;
import com.foro.demo.dto.TemaRequest;
import com.foro.demo.dto.TemaResponse;
import com.foro.demo.repository.CategoriaRepository;
import com.foro.demo.repository.TemaRepository;
import com.foro.demo.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemaService {
    private final TemaRepository temaRepo;
    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;

    public TemaService(TemaRepository temaRepo, UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo) {
        this.temaRepo = temaRepo;
        this.usuarioRepo = usuarioRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional
    public TemaResponse crear(TemaRequest req) {
        Usuario u = usuarioRepo.findById(req.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Categoria c = categoriaRepo.findById(req.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));

        Tema t = new Tema();
        t.setTitulo(req.getTitulo());
        t.setContenido(req.getContenido());
        t.setUsuario(u);
        t.setCategoria(c);

        Tema guardado = temaRepo.save(t);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<TemaResponse> listar() {
        return temaRepo.findAll().stream().map(this::toResponse).toList();
    }

    private TemaResponse toResponse(Tema t) {
        TemaResponse r = new TemaResponse();
        r.setId(t.getId());
        r.setTitulo(t.getTitulo());
        r.setContenido(t.getContenido());
        r.setUsuarioId(t.getUsuario().getId());
        r.setCategoriaId(t.getCategoria().getId());
        r.setCreadoEn(t.getCreadoEn());
        return r;
    }
}

