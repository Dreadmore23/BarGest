# BarberGest
### Sistema de gestión de citas para barbería

## 1. Descripción del proyecto

BarberGest es un proyecto de software orientado a mejorar la gestión de citas de una barbería, facilitando el registro de clientes, barberos, servicios y citas.

El sistema busca reducir los problemas asociados al manejo manual de las citas, como la duplicación de horarios, la desorganización de los registros y las dificultades para consultar la información.

El proyecto se desarrolla como parte del programa de formación **Análisis y Desarrollo de Software del SENA**.

**Evidencia:** GA7-220501096-AA2-EV01 – Codificación de módulos del software.

## 2. Objetivo general

Desarrollar un sistema de gestión de citas para barbería que facilite el agendamiento, evite duplicaciones y mejore el control de la atención y los servicios prestados.

## 3. Tecnologías utilizadas

| Tecnología | Utilización |
|---|---|
| Java 17 | Desarrollo del backend |
| JDBC | Conexión entre Java y MySQL |
| MySQL | Almacenamiento de información |
| Maven | Gestión de dependencias y compilación |
| HTML5 | Estructura de la interfaz web |
| CSS3 | Estilos de la interfaz |
| JavaScript | Funcionalidades del frontend |
| Visual Studio Code | Entorno de desarrollo |
| Git | Control de versiones local |
| GitHub | Repositorio remoto |

## 4. Módulos desarrollados

El backend de BarberGest cuenta con cuatro módulos que permiten realizar operaciones CRUD mediante Java, JDBC y MySQL.

### 4.1. Módulo de clientes

Permite:

- Registrar clientes.
- Consultar clientes registrados.
- Actualizar información de clientes.
- Eliminar clientes.

**Clase:** `ClienteDAO.java`

### 4.2. Módulo de barberos

Permite:

- Registrar barberos.
- Consultar barberos.
- Actualizar sus datos.
- Eliminar barberos.

**Clase:** `BarberoDAO.java`

### 4.3. Módulo de servicios

Permite:

- Registrar servicios de barbería.
- Consultar servicios disponibles.
- Actualizar nombre, descripción, duración y precio.
- Eliminar servicios.

**Clase:** `ServicioDAO.java`

### 4.4. Módulo de citas

Permite:

- Registrar citas asociadas a un cliente, barbero y servicio.
- Consultar las citas registradas.
- Actualizar información de las citas.
- Cancelar citas.
- Eliminar registros de citas.

**Clase:** `CitaDAO.java`

Los estados utilizados para las citas son:

- `PROGRAMADA`
- `CANCELADA`
- `REALIZADA`

La base de datos incluye una restricción que evita registrar dos citas activas para el mismo barbero, fecha y hora. Cuando una cita se cancela, ese horario queda disponible nuevamente.

## 5. Estructura del proyecto

```text
BarbeGest/
├── backend/
│   └── barbergest-backend/
│       ├── pom.xml
│       └── src/
│           ├── main/
│           │   └── java/
│           │       └── com/
│           │           └── barbergest/
│           │               ├── App.java
│           │               ├── ConexionBD.java
│           │               ├── ClienteDAO.java
│           │               ├── BarberoDAO.java
│           │               ├── CitaDAO.java
│           │               └── ServicioDAO.java
│           └── test/
│               └── java/
│                   └── com/
│                       └── barbergest/
│                           └── AppTest.java
├── css/
├── js/
├── index.html
├── .gitignore
└── README.md
```

## 6. Base de datos

El proyecto utiliza una base de datos MySQL llamada `barberia`.

Las tablas principales son:

- `cliente`
- `barbero`
- `servicio`
- `cita`

La tabla `cita` contiene las relaciones necesarias para asociar cada cita con un cliente, un barbero y un servicio.

La conexión con MySQL se realiza desde la clase `ConexionBD.java`, utilizando JDBC.

## 7. Configuración y ejecución del backend

### Requisitos

Para ejecutar el backend se necesita:

- Java JDK 17.
- Apache Maven.
- MySQL.
- Una base de datos `barberia` con las tablas y restricciones correspondientes.

### Configuración de la conexión

El proyecto utiliza la siguiente configuración local:

- Servidor: `localhost`
- Puerto: `3307`
- Base de datos: `barberia`

Las credenciales de MySQL se obtienen mediante variables de entorno para evitar incluir la contraseña directamente en el código.

En PowerShell:

```powershell
$env:BARBERGEST_DB_USER = "root"
$env:BARBERGEST_DB_PASSWORD = Read-Host "Contraseña de MySQL" -MaskInput
```

La contraseña debe corresponder a la configuración local de MySQL.

### Ejecutar el proyecto

Desde la carpeta `backend/barbergest-backend`, ejecutar:

```powershell
mvn compile exec:java "-Dexec.mainClass=com.barbergest.App"
```

Actualmente, `App.java` ejecuta las consultas de los cuatro módulos y muestra los resultados en la consola.

Las operaciones de inserción, actualización, cancelación y eliminación pueden probarse activando las llamadas correspondientes en `App.java`.

## 8. Pruebas realizadas

Se realizaron pruebas manuales de las operaciones CRUD en los cuatro módulos.

| Módulo | CREATE | READ | UPDATE | DELETE |
|---|---|---|---|---|
| Clientes | Correcto | Correcto | Correcto | Correcto |
| Barberos | Correcto | Correcto | Correcto | Correcto |
| Servicios | Correcto | Correcto | Correcto | Correcto |
| Citas | Correcto | Correcto | Correcto | Correcto |

Adicionalmente, se comprobó la cancelación de citas.

Las operaciones se ejecutaron desde Java y sus resultados se verificaron en MySQL Workbench.

## 9. Estándares de codificación

Se aplicaron las convenciones de nomenclatura de Java:

- **Clases:** PascalCase. Ejemplo: `ClienteDAO`.
- **Métodos:** camelCase. Ejemplo: `registrarCliente`.
- **Variables:** camelCase. Ejemplo: `idCliente`.
- **Paquetes:** letras minúsculas. Ejemplo: `com.barbergest`.
- **Constantes:** mayúsculas y guion bajo cuando corresponda.

Para las consultas SQL se utiliza `PreparedStatement`, permitiendo enviar parámetros a las operaciones sobre la base de datos.

## 10. Estado actual del proyecto

Se encuentran implementados y probados los cuatro módulos CRUD del backend mediante Java y JDBC.

El proyecto también incluye una estructura inicial de frontend desarrollada con HTML, CSS y JavaScript.

**La integración entre el frontend y el backend todavía está pendiente.** Actualmente, las operaciones del backend se ejecutan desde Java y se comprueban en MySQL.

## 11. Versionamiento

El proyecto utiliza Git para el control de versiones y GitHub como repositorio remoto.

**Repositorio:** https://github.com/Dreadmore23/BarGest

**Rama principal:** `main`

---

**Proyecto académico:** BarberGest  
**Programa:** Análisis y Desarrollo de Software – SENA  
**Evidencia:** GA7-220501096-AA2-EV01