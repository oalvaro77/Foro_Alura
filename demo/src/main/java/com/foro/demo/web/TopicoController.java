package com.foro.demo.web;
import com.foro.demo.dto.TopicoRequest;
import com.foro.demo.dto.TopicoResponse;
import com.foro.demo.service.TopicoService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoService service;

    public TopicoController(TopicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TopicoResponse> crear(@Valid @RequestBody TopicoRequest body,
                                                UriComponentsBuilder uriBuilder) {
        TopicoResponse creado = service.crear(body);
        return ResponseEntity
                .created(uriBuilder.path("/topicos/{id}").buildAndExpand(creado.getId()).toUri())
                .body(creado);
    }

    // --- Listar tópicos (con filtros y paginación) ---
    @GetMapping
    public Page<TopicoResponse> listar(
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer anio,
            @PageableDefault(size = 10, sort = "creadoEn", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return service.listar(curso, anio, pageable);
    }

    // --- (Opcional) Obtener un tópico por id ---
    @GetMapping("/{id}")
    public ResponseEntity<TopicoResponse> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(r -> ResponseEntity.ok().body(r))          // evita ambigüedad de ok
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicoResponse> actualizar(@PathVariable Long id,
                                                     @Valid @RequestBody TopicoRequest body) {
        return service.actualizar(id, body)
                .map(r -> ResponseEntity.ok().body(r))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminar(id);
        return ResponseEntity.noContent().build(); //204
    }



    // --- Manejo simple de errores ---
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> dataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", ex.getMessage()));
    }
}
