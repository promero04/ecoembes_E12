# Cambios finales TW3

Este documento resume los cambios finales aplicados al prototipo, con el motivo de cada cambio y referencia exacta a archivo y linea.

## Cambios en servidor (autenticacion, contenedores, plantas)
- Token como timestamp y persistido como String para cumplir el requisito de token basado en tiempo y evitar problemas de tipo: `ecoembes_E12/src/main/java/com/ecoembes/service/AuthService.java:35`, `ecoembes_E12/src/main/java/com/ecoembes/service/AuthService.java:41`, `ecoembes_E12/src/main/java/com/ecoembes/entity/Personal.java:30`, `ecoembes_E12/src/main/java/com/ecoembes/entity/Personal.java:71`.
- Validacion de token en operaciones de personal (listar, crear, consultar uso de contenedores) para bloquear acceso sin login: `ecoembes_E12/src/main/java/com/ecoembes/facade/ContenedorController.java:44`, `ecoembes_E12/src/main/java/com/ecoembes/facade/ContenedorController.java:66`, `ecoembes_E12/src/main/java/com/ecoembes/facade/ContenedorController.java:79`, `ecoembes_E12/src/main/java/com/ecoembes/facade/ContenedorController.java:102`.
- Validacion de token en consulta de capacidad y asignacion a plantas para asegurar acceso autenticado: `ecoembes_E12/src/main/java/com/ecoembes/facade/PlantaController.java:48`, `ecoembes_E12/src/main/java/com/ecoembes/facade/PlantaController.java:66`.
- DTO dedicado para la asignacion de contenedores y evitar mapas sin tipado: `ecoembes_E12/src/main/java/com/ecoembes/DTO/AsignacionRequestDTO.java:1`, `ecoembes_E12/src/main/java/com/ecoembes/facade/PlantaController.java:68`.
- Registro de auditoria con identidad del personal que asigna contenedores: `ecoembes_E12/src/main/java/com/ecoembes/service/PlantaService.java:99`, `ecoembes_E12/src/main/java/com/ecoembes/service/PlantaService.java:121`, `ecoembes_E12/src/main/java/com/ecoembes/service/PlantaService.java:130`.

## Cliente Ecoembes (aplicacion web)
- Nuevo proyecto cliente con entrada Spring Boot para UI del personal: `ecoembes_E12/src/main/java/com/ecoembes/cliente/ClienteEcoembesApp.java:1`.
- Configuracion de WebClient y propiedades para apuntar al servidor Ecoembes: `ecoembes_E12/src/main/java/com/ecoembes/cliente/config/ClienteConfig.java:1`, `ecoembes_E12/src/main/java/com/ecoembes/cliente/config/ClienteProperties.java:1`, `ecoembes_E12/src/main/resources/application-client.properties:1`.
- Cliente HTTP tipado para login, logout, consultas y asignaciones: `ecoembes_E12/src/main/java/com/ecoembes/cliente/service/EcoembesApiClient.java:1`.
- Controlador web con sesiones y formularios para las funciones del personal: `ecoembes_E12/src/main/java/com/ecoembes/cliente/facade/ClienteController.java:36`, `ecoembes_E12/src/main/java/com/ecoembes/cliente/facade/ClienteController.java:81`, `ecoembes_E12/src/main/java/com/ecoembes/cliente/facade/ClienteController.java:115`, `ecoembes_E12/src/main/java/com/ecoembes/cliente/facade/ClienteController.java:139`, `ecoembes_E12/src/main/java/com/ecoembes/cliente/facade/ClienteController.java:157`.
- Plantillas de login y panel con todas las operaciones solicitadas: `ecoembes_E12/src/main/resources/templates/login.html:1`, `ecoembes_E12/src/main/resources/templates/panel.html:1`.
- Estilos CSS del cliente para una UI clara y usable en desktop y mobile: `ecoembes_E12/src/main/resources/static/css/cliente.css:1`.

## Build y documentacion
- Dependencia Thymeleaf y task para ejecutar el cliente desde Gradle: `ecoembes_E12/build.gradle:25`, `ecoembes_E12/build.gradle:62`.
- README actualizado con el cuarto servicio y nota de simulacion del contenedor: `README.md:3`, `README.md:42`, `README.md:59`.
