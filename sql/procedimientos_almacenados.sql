
-- ============================================================
-- PROYECTO: BarberGest
-- BASE DE DATOS: barberia
-- ARCHIVO: procedimientos_almacenados.sql
-- TOTAL: 17 procedimientos almacenados
-- INTEGRACIÓN: Java, Spring JDBC y MySQL
-- ============================================================

-- ESTÁNDARES DE CODIFICACIÓN:
-- 1. Nombres de procedimientos y campos en snake_case.
-- 2. Prefijo p_ para los parámetros de entrada.
-- 3. Palabras reservadas SQL en mayúsculas.
-- 4. Sangría de cuatro espacios.
-- 5. Comentarios descriptivos para cada procedimiento.
-- 6. Organización de procedimientos por módulos.
-- 7. Uso de DROP PROCEDURE IF EXISTS para reutilizar el script.

USE barberia;

DELIMITER $$


-- ============================================================
-- MÓDULO CLIENTE
-- ============================================================

-- 01. Registrar cliente.
-- Inserta un nuevo cliente con su nombre y teléfono.

DROP PROCEDURE IF EXISTS registrar_cliente $$

CREATE PROCEDURE registrar_cliente(
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20)
)
BEGIN
    INSERT INTO cliente (nombre, telefono)
    VALUES (p_nombre, p_telefono);
END $$


-- 02. Listar clientes.
-- Consulta todos los clientes registrados.

DROP PROCEDURE IF EXISTS listar_clientes $$

CREATE PROCEDURE listar_clientes()
BEGIN
    SELECT * FROM cliente;
END $$


-- 03. Actualizar cliente.
-- Modifica los datos del cliente identificado por su ID.

DROP PROCEDURE IF EXISTS actualizar_cliente $$

CREATE PROCEDURE actualizar_cliente(
    IN p_id_cliente INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20)
)
BEGIN
    UPDATE cliente
    SET nombre = p_nombre,
        telefono = p_telefono
    WHERE id_cliente = p_id_cliente;
END $$


-- 04. Eliminar cliente.
-- Elimina un cliente respetando las restricciones
-- de integridad referencial de la base de datos.

DROP PROCEDURE IF EXISTS eliminar_cliente $$

CREATE PROCEDURE eliminar_cliente(
    IN p_id_cliente INT
)
BEGIN
    DELETE FROM cliente
    WHERE id_cliente = p_id_cliente;
END $$


-- ============================================================
-- MÓDULO BARBERO
-- ============================================================

-- 05. Registrar barbero.
-- Inserta un nuevo barbero con su nombre y teléfono.

DROP PROCEDURE IF EXISTS registrar_barbero $$

CREATE PROCEDURE registrar_barbero(
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20)
)
BEGIN
    INSERT INTO barbero (nombre, telefono)
    VALUES (p_nombre, p_telefono);
END $$


-- 06. Listar barberos.
-- Consulta todos los barberos registrados.

DROP PROCEDURE IF EXISTS listar_barberos $$

CREATE PROCEDURE listar_barberos()
BEGIN
    SELECT * FROM barbero;
END $$


-- 07. Actualizar barbero.
-- Modifica los datos del barbero identificado por su ID.

DROP PROCEDURE IF EXISTS actualizar_barbero $$

CREATE PROCEDURE actualizar_barbero(
    IN p_id_barbero INT,
    IN p_nombre VARCHAR(100),
    IN p_telefono VARCHAR(20)
)
BEGIN
    UPDATE barbero
    SET nombre = p_nombre,
        telefono = p_telefono
    WHERE id_barbero = p_id_barbero;
END $$


-- 08. Eliminar barbero.
-- Elimina un barbero respetando las restricciones
-- de integridad referencial de la base de datos.

DROP PROCEDURE IF EXISTS eliminar_barbero $$

CREATE PROCEDURE eliminar_barbero(
    IN p_id_barbero INT
)
BEGIN
    DELETE FROM barbero
    WHERE id_barbero = p_id_barbero;
END $$


-- ============================================================
-- MÓDULO SERVICIO
-- ============================================================

-- 09. Registrar servicio.
-- Inserta un servicio con nombre, descripción,
-- duración y precio.
-- Recibe cuatro parámetros compatibles con ServicioDAO.

DROP PROCEDURE IF EXISTS registrar_servicio $$

CREATE PROCEDURE registrar_servicio(
    IN p_nombre VARCHAR(100),
    IN p_descripcion VARCHAR(255),
    IN p_duracion INT,
    IN p_precio DECIMAL(10,2)
)
BEGIN
    INSERT INTO servicio (
        nombre,
        descripcion,
        duracion,
        precio
    )
    VALUES (
        p_nombre,
        p_descripcion,
        p_duracion,
        p_precio
    );
END $$


-- 10. Listar servicios.
-- Consulta todos los servicios disponibles.

DROP PROCEDURE IF EXISTS listar_servicios $$

CREATE PROCEDURE listar_servicios()
BEGIN
    SELECT * FROM servicio;
END $$


-- 11. Actualizar servicio.
-- Modifica el nombre, descripción, duración y precio.
-- Recibe cinco parámetros compatibles con ServicioDAO.

DROP PROCEDURE IF EXISTS actualizar_servicio $$

CREATE PROCEDURE actualizar_servicio(
    IN p_id_servicio INT,
    IN p_nombre VARCHAR(100),
    IN p_descripcion VARCHAR(255),
    IN p_duracion INT,
    IN p_precio DECIMAL(10,2)
)
BEGIN
    UPDATE servicio
    SET nombre = p_nombre,
        descripcion = p_descripcion,
        duracion = p_duracion,
        precio = p_precio
    WHERE id_servicio = p_id_servicio;
END $$


-- 12. Eliminar servicio.
-- Elimina un servicio respetando las restricciones
-- de integridad referencial de la base de datos.

DROP PROCEDURE IF EXISTS eliminar_servicio $$

CREATE PROCEDURE eliminar_servicio(
    IN p_id_servicio INT
)
BEGIN
    DELETE FROM servicio
    WHERE id_servicio = p_id_servicio;
END $$


-- ============================================================
-- MÓDULO CITA
-- ============================================================

-- 13. Registrar cita.
-- Registra una nueva cita con estado inicial PROGRAMADA.
-- La restricción UNIQUE de la tabla cita impide registrar
-- dos citas programadas para el mismo barbero,
-- fecha y hora de inicio.

DROP PROCEDURE IF EXISTS registrar_cita $$

CREATE PROCEDURE registrar_cita(
    IN p_id_cliente INT,
    IN p_id_barbero INT,
    IN p_id_servicio INT,
    IN p_fecha DATE,
    IN p_hora TIME
)
BEGIN
    INSERT INTO cita (
        id_cliente,
        id_barbero,
        id_servicio,
        fecha,
        hora,
        estado
    )
    VALUES (
        p_id_cliente,
        p_id_barbero,
        p_id_servicio,
        p_fecha,
        p_hora,
        'PROGRAMADA'
    );
END $$


-- 14. Listar citas.
-- Consulta las citas registradas utilizando INNER JOIN
-- para obtener los datos del cliente, barbero y servicio.

DROP PROCEDURE IF EXISTS listar_citas $$

CREATE PROCEDURE listar_citas()
BEGIN
    SELECT
        c.id_cita,
        c.id_cliente,
        c.id_barbero,
        c.id_servicio,
        cl.nombre AS cliente,
        b.nombre AS barbero,
        s.nombre AS servicio,
        c.fecha,
        c.hora,
        c.estado
    FROM cita c
    INNER JOIN cliente cl
        ON c.id_cliente = cl.id_cliente
    INNER JOIN barbero b
        ON c.id_barbero = b.id_barbero
    INNER JOIN servicio s
        ON c.id_servicio = s.id_servicio;
END $$


-- 15. Actualizar cita.
-- Modifica los datos de una cita programada.
-- Recibe siete parámetros compatibles con CitaDAO.
-- Valida el estado recibido y protege el historial
-- de las citas canceladas y realizadas.

DROP PROCEDURE IF EXISTS actualizar_cita $$

CREATE PROCEDURE actualizar_cita(
    IN p_id_cita INT,
    IN p_id_cliente INT,
    IN p_id_barbero INT,
    IN p_id_servicio INT,
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_estado VARCHAR(20)
)
BEGIN
    -- Validar que el estado recibido sea permitido.

    IF p_estado IS NULL OR p_estado NOT IN (
        'PROGRAMADA',
        'CANCELADA',
        'REALIZADA'
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Estado de cita no valido';
    END IF;

    -- Actualizar únicamente las citas programadas.

    UPDATE cita
    SET id_cliente = p_id_cliente,
        id_barbero = p_id_barbero,
        id_servicio = p_id_servicio,
        fecha = p_fecha,
        hora = p_hora,
        estado = p_estado
    WHERE id_cita = p_id_cita
        AND estado = 'PROGRAMADA';
END $$


-- 16. Cancelar cita.
-- Cambia el estado de PROGRAMADA a CANCELADA.
-- Conserva el registro para mantener el historial.
-- La columna generada horario_activo pasa a NULL,
-- permitiendo reutilizar la hora de inicio.

DROP PROCEDURE IF EXISTS cancelar_cita $$

CREATE PROCEDURE cancelar_cita(
    IN p_id_cita INT
)
BEGIN
    UPDATE cita
    SET estado = 'CANCELADA'
    WHERE id_cita = p_id_cita
        AND estado = 'PROGRAMADA';
END $$


-- 17. Eliminar cita.
-- Elimina físicamente una cita de la base de datos.
-- Para cancelaciones habituales se debe utilizar
-- cancelar_cita, con el fin de conservar el historial.

DROP PROCEDURE IF EXISTS eliminar_cita $$

CREATE PROCEDURE eliminar_cita(
    IN p_id_cita INT
)
BEGIN
    DELETE FROM cita
    WHERE id_cita = p_id_cita;
END $$


-- ============================================================
-- RESTABLECER DELIMITADOR ORIGINAL
-- ============================================================

DELIMITER ;


-- ============================================================
-- VERIFICACIÓN DE PROCEDIMIENTOS ALMACENADOS
-- ============================================================

-- Mostrar los procedimientos creados.

SHOW PROCEDURE STATUS WHERE Db = 'barberia';

-- Verificar la cantidad de procedimientos almacenados.

SELECT COUNT(*) AS total_procedimientos
FROM information_schema.ROUTINES
WHERE ROUTINE_SCHEMA = 'barberia'
    AND ROUTINE_TYPE = 'PROCEDURE';


-- ============================================================
-- PRUEBAS DE CONSULTA
-- ============================================================

-- Estas consultas no modifican los registros existentes.

CALL listar_clientes();
CALL listar_barberos();
CALL listar_servicios();
CALL listar_citas();

