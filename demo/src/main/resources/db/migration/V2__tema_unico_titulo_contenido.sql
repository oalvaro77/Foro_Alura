ALTER TABLE temas
    ADD COLUMN contenido_hash CHAR(64) GENERATED ALWAYS AS (SHA2(contenido, 256)) STORED,
  ADD UNIQUE KEY uk_tema_titulo_contenido (titulo, contenido_hash);
