package com.foro.demo.repository;

import com.foro.demo.domain.Tema;
import org.springframework.boot.data.autoconfigure.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TemaRepository extends JpaRepository<Tema, Long> {

    boolean existsByTituloIgnoreCaseAndContenidoIgnoreCase(String titulo, String contenido);

    // 👇 Nuevo: para update (excluye el registro con ese id)
    boolean existsByTituloIgnoreCaseAndContenidoIgnoreCaseAndIdNot(
            String titulo, String contenido, Long id);

    Page<Tema> findByCategoria_NombreIgnoreCase(String nombreCategoria, Pageable pageable);

    Page<Tema> findByCreadoEnBetween(Instant desde, Instant hasta, Pageable pageable);

    Page<Tema> findByCategoria_NombreIgnoreCaseAndCreadoEnBetween(
            String nombreCategoria, Instant desde, Instant hasta, Pageable pageable
    );
}

