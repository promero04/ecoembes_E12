# Cambios finales TW3

Este documento resume los cambios finales aplicados al prototipo, con el motivo de cada cambio y referencia exacta a archivo y linea.

## Cambios en servidor (autenticacion, contenedores, plantas)
- Token como timestamp y persistido como String para cumplir el requisito de token basado en tiempo y evitar problemas de tipo: `AuthService.java:35`, 
`AuthService.java:41`, 
`entity/Personal.java:30`, 
`entity/Personal.java:71`.
- Validacion de token en operaciones de personal (listar, crear, consultar uso de contenedores) para bloquear acceso sin login: `facade/ContenedorController.java:44`, 
`facade/ContenedorController.java:66`, 
`facade/ContenedorController.java:79`, 
`facade/ContenedorController.java:102`.
- Validacion de token en consulta de capacidad y asignacion a plantas para asegurar acceso autenticado: 
`facade/PlantaController.java:48`, 
`/facade/PlantaController.java:66`.
- DTO dedicado para la asignacion de contenedores y evitar mapas sin tipado: 
`DTO/AsignacionRequestDTO.java:1`, 
`facade/PlantaController.java:68`.
- Registro de auditoria con identidad del personal que asigna contenedores: 
`service/PlantaService.java:99`, 
`service/PlantaService.java:121`, 
`service/PlantaService.java:130`.

## Cliente Ecoembes (aplicacion web)
- Nuevo proyecto cliente con entrada Spring Boot para UI del personal: 
`ecoembes/cliente/ClienteEcoembesApp.java:1`.
- Configuracion de WebClient y propiedades para apuntar al servidor Ecoembes: 
`cliente/config/ClienteConfig.java:1`, 
`cliente/config/ClienteProperties.java:1`, 
`resources/application-client.properties:1`.
- Cliente HTTP tipado para login, logout, consultas y asignaciones: 
`cliente/service/EcoembesApiClient.java:1`.
- Controlador web con sesiones y formularios para las funciones del personal: 
`cliente/facade/ClienteController.java:36`, 
`cliente/facade/ClienteController.java:81`, 
`/cliente/facade/ClienteController.java:115`, 
`cliente/facade/ClienteController.java:139`, 
`cliente/facade/ClienteController.java:157`.
- Plantillas de login y panel con todas las operaciones solicitadas: 
`templates/login.html:1`, 
`templates/panel.html:1`.
- Estilos CSS del cliente para una UI clara y usable en desktop y mobile: 
`static/css/cliente.css:1`.

## Build y documentacion
- Dependencia Thymeleaf y task para ejecutar el cliente desde Gradle: `ecoembes_E12/build.gradle:25`, `ecoembes_E12/build.gradle:62`.
- README actualizado con el cuarto servicio y nota de simulacion del contenedor: `README.md:3`, `README.md:42`, `README.md:59`.
