# WinBin API — Documentación OpenAPI / Swagger

## URLs en producción (AWS)

| Recurso | URL |
|---------|-----|
| **Índice de endpoints** | https://winbin-api.duckdns.org/api |
| **Swagger UI (interactivo)** | https://winbin-api.duckdns.org/swagger-ui/index.html |
| **OpenAPI JSON** | https://winbin-api.duckdns.org/v3/api-docs |
| **OpenAPI YAML** | https://winbin-api.duckdns.org/v3/api-docs.yaml |
| **Login** | https://winbin-api.duckdns.org/api/auth/login |

## URLs en local

| Recurso | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html |
| **OpenAPI JSON** | http://localhost:8080/v3/api-docs |
| **Índice API** | http://localhost:8080/api |

---

## Cómo usar Swagger UI

### 1. Abrir la documentación

- **Producción:** https://winbin-api.duckdns.org/swagger-ui/index.html
- **Local:** `./gradlew bootRun` → http://localhost:8080/swagger-ui/index.html

### 2. Seleccionar servidor

En la parte superior de Swagger UI elige:

- **Producción — AWS** → peticiones van a `https://winbin-api.duckdns.org`
- **Desarrollo local** → `http://localhost:8080`

### 3. Autenticarse con JWT

1. Expande **Autenticación** → `POST /api/auth/login`
2. **Try it out** → body:
   ```json
   {
     "documento": "1234567890",
     "contrasenna": "tuPassword"
   }
   ```
3. **Execute** → copia el `token` de la respuesta
4. Pulsa **Authorize** (candado arriba a la derecha)
5. Escribe: `Bearer eyJhbGciOiJIUzI1NiIs...` (incluye la palabra `Bearer`)
6. **Authorize** → **Close**

### 4. Probar endpoints protegidos

Ejemplos recomendados:

- `GET /api/usuarios/perfil` — perfil del usuario logueado
- `GET /api/ranking/general` — top 10 estudiantes
- `GET /api/PeriodoRanking/activo` — periodo vigente
- `POST /api/registroia/guardar-resultado` — guardar resultado IA

--- 

### Build & Start (Gradle)

```
Build Command:  ./gradlew build -x test
Start Command:  java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### Verificar después del deploy

1. https://winbin-api.duckdns.org/api — índice con URLs correctas
2. https://winbin-api.duckdns.org/swagger-ui/index.html — Swagger UI
3. https://winbin-api.duckdns.org/v3/api-docs — JSON OpenAPI

---

## Exportar documentación

### Postman

1. Abre Postman → **Import**
2. Pega: `https://winbin-api.duckdns.org/v3/api-docs`
3. Se importan todos los endpoints con schemas

### Flutter / cliente HTTP

No necesitas Swagger en la app; usa el JSON OpenAPI como referencia o genera un cliente con [openapi-generator](https://openapi-generator.tech/).

---

## Dependencia utilizada

```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:3.1.0'
```

Compatible con **Spring Boot 4.x**. Documentación oficial: [springdoc.org](https://springdoc.org/)

---

## Corrección aplicada al índice `/api`

Se corrigió un bug donde las URLs aparecían como `https:/-winbin-api.duckdns.org/...` porque `String.replace("/api", "")` eliminaba el `api` del hostname `winbin-api.duckdns.org`. Ahora se usa recorte por URI o headers `X-Forwarded-*` de Render.
