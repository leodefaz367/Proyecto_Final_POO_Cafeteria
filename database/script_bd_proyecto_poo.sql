CREATE TABLE usuarios(
                         id_usuario SERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         correo VARCHAR(100) UNIQUE NOT NULL,
                         clave VARCHAR(100) NOT NULL,
                         rol VARCHAR(20) NOT NULL CHECK (rol IN ('Administrador', 'Cajero', 'Reportes')),
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
                          precio NUMERIC(10,2) NOT NULL CHECK(precio > 0),
                          stock INTEGER NOT NULL CHECK(stock >= 0),
                          imagen VARCHAR(255),
                          id_categoria INTEGER NOT NULL,
                            -- Relación: Un producto pertenece a una categoría
                          CONSTRAINT fk_categoria FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE RESTRICT
);

CREATE TABLE ventas(
                       id_venta SERIAL PRIMARY KEY,
                       fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       total NUMERIC(10,2) NOT NULL CHECK(total >= 0),
                       id_cliente INTEGER,
                       nombre_cliente VARCHAR(100) DEFAULT 'Consumidor Final',
                       cedula_cliente VARCHAR(20) DEFAULT '9999999999',
                       id_cajero INTEGER NOT NULL,
                         -- Relación: Una venta debe estar vinculada a un usuario real (cajero)
                       CONSTRAINT fk_cajero FOREIGN KEY (id_cajero) REFERENCES usuarios(id_usuario) ON DELETE RESTRICT
);

CREATE TABLE detalle_venta(
                              id_detalle SERIAL PRIMARY KEY,
                              id_venta INTEGER NOT NULL,
                              id_producto INTEGER NOT NULL,
                              cantidad INTEGER NOT NULL CHECK(cantidad > 0),
                              precio NUMERIC(10,2) NOT NULL CHECK(precio > 0),
                              subtotal NUMERIC(10,2) NOT NULL CHECK(subtotal >= 0),
                                 -- Relaciones: Vincula el detalle con la venta (si se borra la venta, se borra el detalle) y con el producto
                              CONSTRAINT fk_venta FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE,
                              CONSTRAINT fk_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT
);

-- DATOS DE PRUEBA INICIALES

INSERT INTO usuarios (nombre, correo, clave, rol) VALUES
                                                      ('Gregory', 'admin@cafe.com', '1234', 'Administrador'),
                                                      ('Leonardo', 'cajero@cafe.com', '1234', 'Cajero'),
                                                      ('Usuario', 'reportes@cafe.com', '1234', 'Reportes');

INSERT INTO categorias (nombre) VALUES
                                    ('Bebidas Calientes'),
                                    ('Postres'),
                                    ('Bebidas Frías');

INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES
                                                                             ('Café Americano', 'Café negro tradicional', 1.50, 50, 1),
                                                                             ('Capuchino', 'Café expreso con leche espumada', 2.50, 30, 1),
                                                                             ('Cheesecake', 'Pastel de queso con fresas', 3.00, 20, 2);