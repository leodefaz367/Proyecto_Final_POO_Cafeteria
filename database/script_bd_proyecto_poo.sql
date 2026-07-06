-- Creación de tablas
CREATE TABLE usuarios(
                         id_usuario SERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         correo VARCHAR(100) UNIQUE NOT NULL,
                         clave VARCHAR(100) NOT NULL,
                         rol VARCHAR(20) NOT NULL,
                         estado BOOLEAN DEFAULT TRUE
);

CREATE TABLE categorias(
                           id_categoria SERIAL PRIMARY KEY,
                           nombre VARCHAR(80) UNIQUE NOT NULL
);

CREATE TABLE productos(
                          id_producto SERIAL PRIMARY KEY,
                          nombre VARCHAR(100) UNIQUE NOT NULL,
                          descripcion TEXT,
                          precio NUMERIC(10,2) NOT NULL CHECK(precio>0),
                          stock INTEGER NOT NULL CHECK(stock>=0),
                          imagen VARCHAR(255),
                          id_categoria INTEGER NOT NULL
);

CREATE TABLE ventas(
                       id_venta SERIAL PRIMARY KEY,
                       fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       total NUMERIC(10,2) NOT NULL,
                       id_cliente INTEGER NOT NULL,
                       id_cajero INTEGER NOT NULL
);

CREATE TABLE detalle_venta(
                              id_detalle SERIAL PRIMARY KEY,
                              id_venta INTEGER NOT NULL,
                              id_producto INTEGER NOT NULL,
                              cantidad INTEGER NOT NULL CHECK(cantidad>0),
                              precio NUMERIC(10,2) NOT NULL,
                              subtotal NUMERIC(10,2) NOT NULL
);

-- DATOS DE PRUEBA
INSERT INTO usuarios (nombre, correo, clave, rol) VALUES
                                                      ('Gregory Admin', 'admin@cafe.com', '1234', 'Cajero'),
                                                      ('Leonardo Cajero', 'cajero@cafe.com', '1234', 'Cajero'),
                                                      ('Usuario Reportes', 'reportes@cafe.com', '1234', 'Reportes');

INSERT INTO categorias (nombre) VALUES ('Bebidas Calientes'), ('Postres'), ('Bebidas Frías');

INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES
                                                                             ('Café Americano', 'Café negro tradicional', 1.50, 50, 1),
                                                                             ('Capuchino', 'Café expreso con leche espumada', 2.50, 30, 1),
                                                                             ('Cheesecake', 'Pastel de queso con fresas', 3.00, 20, 2);