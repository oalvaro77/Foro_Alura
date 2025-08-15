package com.foro.demo.repository;

import com.foro.demo.domain.Categoria;
import com.foro.demo.domain.Respuesta;
import com.foro.demo.domain.Tema;
import com.foro.demo.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {}