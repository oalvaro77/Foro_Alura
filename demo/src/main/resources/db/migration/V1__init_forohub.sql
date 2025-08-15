-- V1__init_forohub.sql
CREATE TABLE usuarios (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          nombre VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE categorias (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            nombre VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE temas (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       titulo VARCHAR(200) NOT NULL,
                       contenido TEXT NOT NULL,
                       usuario_id BIGINT NOT NULL,
                       categoria_id BIGINT NOT NULL,
                       creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                       FOREIGN KEY (categoria_id) REFERENCES categorias(id)
) ENGINE=InnoDB;

CREATE TABLE respuestas (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            contenido TEXT NOT NULL,
                            tema_id BIGINT NOT NULL,
                            usuario_id BIGINT NOT NULL,
                            creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (tema_id) REFERENCES temas(id),
                            FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;
