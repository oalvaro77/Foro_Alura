-- OJO: primero mira el nombre real de la FK con: SHOW CREATE TABLE respuestas;
ALTER TABLE respuestas DROP FOREIGN KEY fk_respuestas_tema;  -- usa el nombre real
ALTER TABLE respuestas
    ADD CONSTRAINT fk_respuestas_tema
        FOREIGN KEY (tema_id) REFERENCES temas(id) ON DELETE CASCADE;
