# Ecoembes E12 – Guía de ejecución de servicios

Este repo contiene tres servicios separados en el mismo proyecto Gradle:

- **Ecoembes Server** (API principal, persistencia H2, proxies a servicios externos).
- **PlasSB Server** (Spring Boot + JPA/H2, expone capacidades y asignaciones).
- **ContSocket Server** (REST + sockets + persistencia en memoria).

## Prerrequisitos
- JDK 21 instalado y en PATH.
- PowerShell / CMD en Windows.
- Desde la raíz del módulo: `C:\Users\usuario\git\ecoembes_E12\ecoembes_E12`.

## Comandos básicos (Windows)
Usa el wrapper Gradle del proyecto (`gradlew.bat`):

```powershell
cd C:\Users\usuario\git\ecoembes_E12\ecoembes_E12
```

### 1) Ecoembes Server (puerto 8081)
```powershell
.\gradlew.bat bootRun
```
- Swagger UI: http://localhost:8081/swagger-ui/index.html
- H2 console: http://localhost:8081/h2-console (JDBC `jdbc:h2:mem:ecoembes`, user `sa`, sin password).

### 2) PlasSB Server (puerto 8082)
```powershell
.\gradlew.bat bootRunPlasSb
```
- Swagger UI: http://localhost:8082/swagger-ui/index.html
- H2 console: http://localhost:8082/h2-console (JDBC `jdbc:h2:mem:plassb`, user `sa`, sin password).

### 3) ContSocket Server (puerto 8083, socket 9090)
```powershell
.\gradlew.bat bootRunContSocket
```
- Swagger UI: http://localhost:8083/swagger-ui/index.html
- Servidor TCP escuchando en `localhost:9090` (acepta líneas `ASIGNACION|{id}|{planta}|{total}`).

## Dependencias y perfiles
- Ecoembes usa perfil por defecto (`application.properties`).
- PlasSB usa perfil `plassb` (`application-plassb.properties`).
- ContSocket usa perfil `contsocket` (`application-contsocket.properties`).

## Testing
```powershell
.\gradlew.bat test
```

## Notas
- Los datos de demo (contenedores, plantas, capacidades) se cargan al arrancar si la tabla está vacía.
- Endpoints externos documentados en `docs/plantas-external-api.md`.
