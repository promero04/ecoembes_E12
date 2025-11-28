# API REST servicios externos de plantas de reciclaje

## PlasSB (Spring Boot + JPA/H2 en `:8082`)
- `GET /api/v1/health` → 200 `{status:"ok"}`.
- `POST /api/v1/capacidades` body `{fecha:"yyyy-MM-dd", capacidadTon:number}` → 201 con `Capacidad {id, fecha, capacidadTon}`.
- `GET /api/v1/capacidades?fechaInicio=&fechaFin=` → 200 lista de capacidades en rango.
- `GET /api/v1/capacidades/{fecha}` → 200 `Capacidad`, 404 si no existe.
- `POST /api/v1/asignaciones` body `{asignacionId, fecha, solicitante, contenedores:[{id,numEnvases,estado}], totalEnvases}` → 201 `{asignacionId, estado:"ACEPTADA", mensaje}`.
- `GET /api/v1/asignaciones?fecha=` → 200 lista de asignaciones (filtro opcional por fecha).
- `GET /api/v1/asignaciones/{asignacionId}` → 200 detalle con contenedores, 404 si no existe.
- `POST /api/v1/asignaciones/{asignacionId}/estado` body `{estado:"EN_PROCESO|COMPLETADA|INCIDENCIA", detalle}` → 200 asignación actualizada.
- `POST /api/v1/alertas/saturacion` body `{porcentajeOcupacion, timestamp, detalle}` → 202.

Persistencia: entidades JPA `Capacidad(fecha,capacidadTon)`, `Asignacion(asignacionId,fecha,solicitante,totalEnvases,estado,detalle)` y `ContenedorAsignado(asignacion_id, contenedor_id, numEnvases, estado)` en H2.

## ContSocket (fachada REST + sockets + memoria en `:8083`)
- `GET /api/v1/health` → 200 `{status:"ok"}`.
- `PUT /api/v1/capacidades/{fecha}` body `{capacidadTon:number}` → 200 capacidad registrada.
- `GET /api/v1/capacidades/{fecha}` → 200 `{fecha, capacidadTon}`, 404 si no existe.
- `POST /api/v1/asignaciones` body `{asignacionId, fecha, solicitante, contenedores:[{id,numEnvases,estado}], totalEnvases}` → 201 `{asignacionId, estado:"ACEPTADA", mensaje}`.
- `GET /api/v1/asignaciones/{asignacionId}` → 200 detalle en memoria, 404 si no existe.
- `POST /api/v1/asignaciones/{asignacionId}/estado` body `{estado:"EN_PROCESO|COMPLETADA|INCIDENCIA", detalle}` → 200 con estado actualizado.

Persistencia: estructuras en memoria (`capacidadesPorFecha`, `asignaciones`). Un servidor TCP (puerto `9090`) recibe comandos simples (`ASIGNACION|{id}|{planta}|{total}`) como canal alternativo al REST.
