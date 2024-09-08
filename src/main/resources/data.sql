-- Insertar domicilios
INSERT INTO domicilios (calle, numero, localidad, provincia) VALUES ('Calle 1', 1, 'Localidad 1', 'Provincia 1');
INSERT INTO domicilios (calle, numero, localidad, provincia) VALUES ('Calle 2', 2, 'Localidad 2', 'Provincia 2');
INSERT INTO domicilios (calle, numero, localidad, provincia) VALUES ('Calle 3', 3, 'Localidad 3', 'Provincia 3');
INSERT INTO domicilios (calle, numero, localidad, provincia) VALUES ('Calle 4', 4, 'Localidad 4', 'Provincia 4');

-- Insertar pacientes
INSERT INTO pacientes (nombre, apellido, dni, fecha_alta, domicilio_id) VALUES ('Paula', 'Pérez', '12345678', '2024-09-01', 1);
INSERT INTO pacientes (nombre, apellido, dni, fecha_alta, domicilio_id) VALUES ('Pol', 'Gómez', '87654321', '2024-09-02', 2);
INSERT INTO pacientes (nombre, apellido, dni, fecha_alta, domicilio_id) VALUES ('Pepa', 'Molina', '13579246', '2024-09-02', 3);
INSERT INTO pacientes (nombre, apellido, dni, fecha_alta, domicilio_id) VALUES ('Piero', 'Castillo', '24681357', '2024-09-03', 4);

-- Insertar odontólogos
INSERT INTO odontologos (nombre, apellido, matricula) VALUES ('Omar', 'Martínez', '10001');
INSERT INTO odontologos (nombre, apellido, matricula) VALUES ('Orley', 'López', '10002');
INSERT INTO odontologos (nombre, apellido, matricula) VALUES ('Opal', 'Gómez', '10003')