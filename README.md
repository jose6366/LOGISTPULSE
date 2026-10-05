# LOGISTPULSE

Proyecto integrador de Diseño de Sistemas (USFQ). LOGISTPULSE implementa un flujo mínimo de gestión de pedidos con arquitectura MVC, persistencia PostgreSQL, autenticación, historial de estados y Docker.

## Alcance

### Incluye
- Login con Spring Security.
- Registro de pedidos e ítems.
- Consulta de pedidos y detalle.
- Actualización controlada de estado con historial.
- REST API de pedidos.
- `GET /health` público con HTTP 200.
- Persistencia PostgreSQL.
- Ejecución reproducible mediante Docker Compose.

### Fuera de alcance de esta etapa
- Optimización automática de rutas.
- GPS en tiempo real.
- Integración con transportistas externos.
- Predicción avanzada de ETA.
- Aplicación móvil productiva.

## Stack
Java 21, Spring Boot 3.5.6, Spring MVC, Thymeleaf, Spring Data JPA, Spring Security, PostgreSQL 16, Maven y Docker Compose.

## Estructura
`controller/` recibe solicitudes; `service/` contiene reglas de transición; `model/` representa pedidos/ítems/despacho/historial; `repository/` persiste; `templates/` son las vistas.

## Requisitos
Docker Desktop/Engine + Docker Compose. Para ejecución sin Docker: Java 21, Maven 3.9+ y PostgreSQL.

## Configuración
```bash
cp .env.example .env
```
No subir `.env`.

## Ejecutar
```bash
docker compose up --build
```
Abrir `http://localhost:8081`.

## Credenciales de demostración académica
Estas credenciales son exclusivamente locales y de demostración. No corresponden a usuarios ni secretos reales.

- `operator` / `operator123`
- `supervisor` / `supervisor123`

## Salud y operación comprobable
```bash
curl http://localhost:8081/health
curl -u operator:operator123 http://localhost:8081/api/orders
curl -u operator:operator123 -H 'Content-Type: application/json' -d '{"customerName":"Sucursal Sur","destination":"Quito","product":"Caja térmica","quantity":2}' http://localhost:8081/api/orders
curl -u operator:operator123 http://localhost:8081/api/orders
```
El POST demuestra HU-LP-02 y el GET demuestra HU-LP-03.

## Cambios de estado
```bash
curl -u operator:operator123 -X POST http://localhost:8081/api/orders/1/status/READY
curl -u operator:operator123 http://localhost:8081/api/orders/1/history
```
Transiciones válidas: `CREATED -> READY -> DISPATCHED -> DELIVERED`; desde CREATED/READY puede cancelarse.

## Estado, logs y apagado
```bash
docker compose ps
docker compose logs -f app
docker compose down
```
Para borrar datos: `docker compose down -v`.

## Pruebas
```bash
mvn test
```
GitHub Actions ejecuta tests en pushes y PR a `main`.

## Estrategia GitHub Flow
Ver `docs/BRANCHING.md`. Usar ramas `feature/<hu>-descripcion`, commits descriptivos, Pull Request, revisión de otro integrante y merge.

## Evidencia requerida antes de la entrega
Guardar en `../evidencias/LOGISTPULSE/`:
- captura del repositorio y ramas;
- captura y enlace del Pull Request fusionado;
- evidencia de revisión/comentario de otro integrante;
- `docker compose up --build`;
- `docker compose ps`;
- respuesta `GET /health` HTTP 200;
- respuesta exitosa de la operación del Sprint 1.

## Troubleshooting
- Puerto 8081 ocupado: cambiar `APP_PORT`.
- DB no saludable: revisar `docker compose logs db`.
- Reinicio limpio: `docker compose down -v`.

## Correcciones de calidad incluidas en esta versión
- Lombok eliminado del código para evitar dependencia de generación de getters/builders durante compilación.
- `/health` comprueba conexión real a la base de datos con `SELECT 1`.
- CSRF permanece activo para vistas web y solo se ignora en la API REST de demostración.
- Se añadieron pruebas automatizadas del flujo principal del Sprint.
- La asignación de despacho está expuesta por REST y por la vista web.
- La interfaz solo muestra transiciones de estado permitidas y se registra auditoría explícita.
