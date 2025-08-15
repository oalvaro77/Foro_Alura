package com.foro.demo.web;


import com.foro.demo.dto.TemaRequest;
import com.foro.demo.dto.TemaResponse;

import com.foro.demo.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/temas")
public class TemaController {
    private final TemaService service;

    public TemaController(TemaService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<TemaResponse> crear(@Valid @RequestBody TemaRequest dto) {
        TemaResponse creado = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public List<TemaResponse> listar() {
        return service.listar();
    }
}
