-- Usuarios

INSERT INTO users (id, username, password, email, first_name, last_name, address, photo_url)
VALUES
(1, 'admin', '$2a$10$u1S7zOezCu8hvHn.UZ1GeuEZB5kAvsyOFOQjYLm76vQS42yrZcJ6a', 'admin@araculture.com', 'Admin', 'User', 'Calle Admin 123', null),
(2, 'juanperez', '$2a$10$D3Pq84XYYptv5RjvWYNv1eC1k.3jHxfvEw/O3zX6WmO1lXW6vHVpG6', 'juan.perez@gmail.com', 'Juan', 'Perez', 'Av. Principal 45', null);

-- Roles (recordar que User.roles es un Set<String> en JPA, pero no tabla separada)
-- Insert roles manualmente usando el campo roles (es un collection table user_roles)

INSERT INTO user_roles (user_id, role) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER');

-- Categorías (sólo como texto en campo category en productos)

-- Productos de ejemplo

INSERT INTO products (id, name, description, price, category, image_url, popular, new_arrival)
VALUES
(1, 'Camisa Tradicional', 'Camisa de algodón 100% hecha a mano con diseños tradicionales.', 350.00, 'Ropa', 'https://example.com/images/camisa1.jpg', true, false),
(2, 'Falda Bordada', 'Falda con bordados típicos de la región.', 450.00, 'Ropa', 'https://example.com/images/falda1.jpg', true, true),
(3, 'Bolso Artesanal', 'Bolso hecho con técnicas ancestrales.', 600.00, 'Accesorios', 'https://example.com/images/bolso1.jpg', false, true),
(4, 'Aretes de Plata', 'Aretes elaborados en plata fina con diseño cultural.', 250.00, 'Accesorios', 'https://example.com/images/aretes1.jpg', true, false),
(5, 'Chaleco de Lana', 'Chaleco tejido a mano con lana natural.', 700.00, 'Ropa', 'https://example.com/images/chaleco1.jpg', false, false);

-- No insertamos pedidos, carrito ni favoritos para que inicies limpio
