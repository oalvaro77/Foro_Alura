package com.foro.demo.service;

import com.foro.demo.domain.Tema;
import com.foro.demo.dto.TopicoRequest;
import com.foro.demo.dto.TopicoResponse;
import com.foro.demo.repository.CategoriaRepository;
import com.foro.demo.repository.TemaRepository;
import com.foro.demo.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TopicoService {

    private final TemaRepository temaRepo;
    private final UsuarioRepository usuarioRepo;
    private final CategoriaRepository categoriaRepo;

    public TopicoService(TemaRepository temaRepo, UsuarioRepository usuarioRepo, CategoriaRepository categoriaRepo) {
        this.temaRepo = temaRepo;
        this.usuarioRepo = usuarioRepo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional
    public TopicoResponse crear(TopicoRequest req) {
        if (temaRepo.existsByTituloIgnoreCaseAndContenidoIgnoreCase(req.getTitulo(), req.getMensaje())) {
            throw new IllegalStateException("Ya existe un tópico con el mismo título y mensaje.");
        }

        var autor = usuarioRepo.findById(req.getAutorId())
                .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
        var curso = categoriaRepo.findById(req.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso (categoría) no encontrado"));

        var tema = new Tema();
        tema.setTitulo(req.getTitulo());
        tema.setContenido(req.getMensaje());
        tema.setUsuario(autor);
        tema.setCategoria(curso);

        var guardado = temaRepo.save(tema);
        return toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public Page<TopicoResponse> listar(String curso, Integer anio, Pageable pageable) {
        Instant desde = null, hasta = null;
        if (anio != null) {
            desde = LocalDate.of(anio, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC);
            hasta = LocalDate.of(anio + 1, 1, 1).atStartOfDay().toInstant(ZoneOffset.UTC);
        }

        Page<Tema> page;
        if (curso != null && !curso.isBlank() && anio != null) {
            page = temaRepo.findByCategoria_NombreIgnoreCaseAndCreadoEnBetween(curso, desde, hasta, pageable);
        } else if (curso != null && !curso.isBlank()) {
            page = temaRepo.findByCategoria_NombreIgnoreCase(curso, pageable);
        } else if (anio != null) {
            page = temaRepo.findByCreadoEnBetween(desde, hasta, pageable);
        } else {
            page = temaRepo.findAll(pageable);
        }

        return page.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<TopicoResponse> obtenerPorId(Long id) {
        return temaRepo.findById(id).map(this::toResponse);
    }

    private TopicoResponse toResponse(Tema t) {
        var r = new TopicoResponse();
        r.setId(t.getId());
        r.setTitulo(t.getTitulo());
        r.setMensaje(t.getContenido());
        r.setAutorId(t.getUsuario().getId());
        r.setCursoId(t.getCategoria().getId());
        r.setCreadoEn(t.getCreadoEn());
        return r;
    }

    @Transactional
    public Optional<TopicoResponse> actualizar(Long id, @Valid TopicoRequest req) {
        return temaRepo.findById(id).map(t -> {
            // Normalizamos entradas para validar correctamente duplicados
            String nuevoTitulo  = req.getTitulo().trim();
            String nuevoMensaje = req.getMensaje().trim();

            // Regla: no permitir duplicados título+mensaje (excluyendo este id)
            if (temaRepo.existsByTituloIgnoreCaseAndContenidoIgnoreCaseAndIdNot(
                    nuevoTitulo, nuevoMensaje, id)) {
                throw new IllegalStateException("Ya existe un tópico con el mismo título y mensaje.");
            }

            var nuevoAutor = usuarioRepo.findById(req.getAutorId())
                    .orElseThrow(() -> new IllegalArgumentException("Autor no encontrado"));
            var nuevoCurso = categoriaRepo.findById(req.getCursoId())
                    .orElseThrow(() -> new IllegalArgumentException("Curso (categoría) no encontrado"));

            // Aplicamos cambios
            t.setTitulo(nuevoTitulo);
            t.setContenido(nuevoMensaje);
            t.setUsuario(nuevoAutor);
            t.setCategoria(nuevoCurso);

            var guardado = temaRepo.save(t);
            return toResponse(guardado);
        });


    }

    @Transactional
    public void eliminar(Long id) {
        var tema = temaRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tópico no encontrado"));
        // Si hay respuestas y tu FK no tiene ON DELETE CASCADE,
        // esto puede lanzar DataIntegrityViolationException.
        temaRepo.delete(tema);
    }

    // ❌ ELIMINAR ESTE MÉTODO ACCIDENTAL
    // public FileChannel obtenerPorId(Long id) { return null; }
}

