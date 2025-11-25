# API REST Servicios Externos de Plantas de Reciclaje

## PlasSB (Spring Boot + JPA/H2)
- `GET /api/v1/health` → 200 `{status:"ok"}`.
- `POST /api/v1/capacidades` body `{fecha:"yyyy-MM-dd", capacidadTon:number}` → 201/200 capacidad creada/actualizada.
- `GET /api/v1/capacidades?fechaInicio=&fechaFin=` → 200 lista de capacidades en rango.
- `GET /api/v1/capacidades/{fecha}` → 200 capacidad de la fecha, 404 si no existe.
- `POST /api/v1/asignaciones` body `{asignacionId, fecha, solicitante, contenedores:[{id,numEnvases,estado}], totalEnvases}` → 201 `{asignacionId, estado:"ACEPTADA|RECHAZADA", mensaje}`.
- `GET /api/v1/asignaciones?fecha=` → 200 lista de asignaciones.
- `GET /api/v1/asignaciones/{asignacionId}` → 200 detalle, 404 si no existe.
- `POST /api/v1/asignaciones/{asignacionId}/estado` body `{estado:"EN_PROCESO|COMPLETADA|INCIDENCIA", detalle}` → 200 con asignación actualizada.
- `POST /api/v1/alertas/saturacion` body `{porcentajeOcupacion, timestamp, detalle}` → 202 (opcional).

Persistencia: entidades JPA `Capacidad(fecha, capacidadTon)`, `Asignacion(id, fecha, solicitante, totalEnvases, estado)`, `ContenedorAsignado(asignacion_id, contenedor_id, numEnvases, estado)` en H2.

## ContSocket (fachada REST sobre lógica vía sockets + memoria)
- `GET /api/v1/health` → 200 `{status:"ok"}`.
- `PUT /api/v1/capacidades/{fecha}` body `{capacidadTon:number}` → 200 capacidad registrada.
- `GET /api/v1/capacidades/{fecha}` → 200 capacidad, 404 si no existe.
- `POST /api/v1/asignaciones` body `{asignacionId, fecha, solicitante, contenedores:[{id,numEnvases,estado}], totalEnvases}` → 201 `{asignacionId, estado:"ACEPTADA|RECHAZADA", mensaje}`.
- `GET /api/v1/asignaciones/{asignacionId}` → 200 detalle en memoria, 404 si no existe.
- `POST /api/v1/asignaciones/{asignacionId}/estado` body `{estado:"EN_PROCESO|COMPLETADA|INCIDENCIA", detalle}` → 200 con estado actualizado.

Persistencia: estructuras en memoria (`capacidadesPorFecha`, `asignaciones`) con DTOs equivalentes; la fachada REST traduce la llamada HTTP a operaciones socket/in-memory. Documentar con Swagger y usar HTTP codes estándar. 
