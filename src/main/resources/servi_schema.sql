-- ============================================================
-- SERVI — Schema de base de datos
-- Ejecutar en phpMyAdmin o MySQL CLI con XAMPP activo
-- ============================================================

CREATE DATABASE IF NOT EXISTS servi
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE servi;

-- ---- Usuarios ----
CREATE TABLE IF NOT EXISTS usuarios (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100)   NOT NULL,
    apellidos           VARCHAR(100)   DEFAULT '',
    email               VARCHAR(150)   NOT NULL UNIQUE,
    password            VARCHAR(255)   NOT NULL,
    telefono            VARCHAR(20)    DEFAULT '',
    poblacion           VARCHAR(100)   DEFAULT '',
    descripcion         TEXT,
    avatar              VARCHAR(255)   DEFAULT NULL,
    saldo_wallet        DECIMAL(10,2)  DEFAULT 0.00,
    valoracion_media    DOUBLE         DEFAULT 0.0,
    total_valoraciones  INT            DEFAULT 0,
    activo              TINYINT(1)     DEFAULT 1,
    fecha_registro      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Categorías ----
CREATE TABLE IF NOT EXISTS categorias (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    icono       VARCHAR(10)  DEFAULT '🔧',
    descripcion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Categorías de ejemplo (IDs explícitos para garantizar que id=1 existe)
INSERT INTO categorias (id, nombre, icono) VALUES
(1,  'Fontanería',     '🔧'),
(2,  'Electricidad',   '⚡'),
(3,  'Informática',    '💻'),
(4,  'Clases',         '📚'),
(5,  'Limpieza',       '🧹'),
(6,  'Reformas',       '🏠'),
(7,  'Jardinería',     '🌱'),
(8,  'Transporte',     '🚛'),
(9,  'Pintura',        '🎨'),
(10, 'Cocina',         '🍳'),
(11, 'Belleza',        '💇'),
(12, 'Otros',          '📌')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

-- ---- Servicios ----
CREATE TABLE IF NOT EXISTS servicios (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id      INT            NOT NULL,
    categoria_id    INT            NOT NULL,
    titulo          VARCHAR(200)   NOT NULL,
    descripcion     TEXT,
    precio          DECIMAL(10,2)  DEFAULT 0.00,
    tipo_precio     ENUM('hora','servicio','dia','mes') DEFAULT 'hora',
    poblacion       VARCHAR(100)   DEFAULT '',
    estado          ENUM('activo','pausado','eliminado') DEFAULT 'activo',
    fecha_creacion  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id)   REFERENCES usuarios(id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Trabajos ----
CREATE TABLE IF NOT EXISTS trabajos (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id      INT            NOT NULL,
    categoria_id    INT            NOT NULL,
    titulo          VARCHAR(200)   NOT NULL,
    descripcion     TEXT,
    presupuesto     DECIMAL(10,2)  DEFAULT NULL,
    poblacion       VARCHAR(100)   DEFAULT '',
    estado          ENUM('activo','cerrado','eliminado') DEFAULT 'activo',
    fecha_creacion  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id)   REFERENCES usuarios(id),
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Reservas ----
CREATE TABLE IF NOT EXISTS reservas (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    servicio_id     INT            NOT NULL,
    cliente_id      INT            NOT NULL,
    proveedor_id    INT            NOT NULL,
    fecha_servicio  DATE           DEFAULT NULL,
    estado          ENUM('pendiente','aceptada','finalizado','cancelada') DEFAULT 'pendiente',
    precio          DECIMAL(10,2)  DEFAULT 0.00,
    mensaje         TEXT,
    fecha_creacion  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (servicio_id)  REFERENCES servicios(id),
    FOREIGN KEY (cliente_id)   REFERENCES usuarios(id),
    FOREIGN KEY (proveedor_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Chats ----
CREATE TABLE IF NOT EXISTS chats (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    usuario1_id     INT NOT NULL,
    usuario2_id     INT NOT NULL,
    fecha_creacion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultimo_mensaje  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_chat (usuario1_id, usuario2_id),
    FOREIGN KEY (usuario1_id) REFERENCES usuarios(id),
    FOREIGN KEY (usuario2_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Mensajes ----
CREATE TABLE IF NOT EXISTS mensajes (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    chat_id     INT     NOT NULL,
    emisor_id   INT     NOT NULL,
    contenido   TEXT    NOT NULL,
    leido       TINYINT(1) DEFAULT 0,
    fecha_envio TIMESTAMP  DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (chat_id)   REFERENCES chats(id),
    FOREIGN KEY (emisor_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Valoraciones ----
CREATE TABLE IF NOT EXISTS valoraciones (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    reserva_id      INT NOT NULL,
    valorador_id    INT NOT NULL,
    valorado_id     INT NOT NULL,
    puntuacion      TINYINT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    comentario      TEXT,
    fecha_creacion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_valoracion (reserva_id, valorador_id),
    FOREIGN KEY (reserva_id)   REFERENCES reservas(id),
    FOREIGN KEY (valorador_id) REFERENCES usuarios(id),
    FOREIGN KEY (valorado_id)  REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Pagos ----
CREATE TABLE IF NOT EXISTS pagos (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    reserva_id      INT            NOT NULL,
    pagador_id      INT            NOT NULL,
    receptor_id     INT            NOT NULL,
    importe         DECIMAL(10,2)  NOT NULL,
    estado          ENUM('pendiente','completado','devuelto') DEFAULT 'pendiente',
    fecha_pago      TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reserva_id)   REFERENCES reservas(id),
    FOREIGN KEY (pagador_id)   REFERENCES usuarios(id),
    FOREIGN KEY (receptor_id)  REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ---- Wallet movimientos ----
CREATE TABLE IF NOT EXISTS wallet_movimientos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id       INT            NOT NULL,
    tipo             ENUM('entrada','salida') NOT NULL,
    importe          DECIMAL(10,2)  NOT NULL,
    concepto         VARCHAR(255)   DEFAULT '',
    fecha_movimiento TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
