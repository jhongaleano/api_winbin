
runbook_de_incidentes.md
100%
# 🚨 Runbook de Operaciones e Incidentes (RAP 1)

**Propósito:** Guía de acción rápida para el equipo de Guardia/SRE frente a fallos en el entorno de producción local.

---

## 🛑 Incidente 1: Errores 401 Unauthorized
**Descripción:** Los usuarios repostan este error al intentar hacer una peticion en la api

### 🔍 Diagnóstico:
1. Revise los logs del contenedor y/o backend:
   ```bash
   docker logs nombre_contenedor
   ```
   
2. Revise el localhost:8080 para mirar error
    ```bash
        curl -i http://localhost:8080
    ```

## Solución
 1. verifiar apicontroller si el endponit contiene alguan extencion (/api).

 2. comprueba nuevamente en localhost:8080/api
    ```bash
        curl -i http://localhost:8080/api
    ```
---

---

## 🛑 incidenete 2: Error Connection refused connect failed

1. revisa los logs del contenedor
    ```bash
   docker logs nombre_contenedor
   ```
2. revisa si tienes levantado los servicios de xampp 

3. revisa que las variables de entorno esten correctas

4. revisa que el application.properties este correcto

    ```bash
    spring.config.import=optional:file:.env

    spring.application.name=demo

    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

    jwt.secret=${JWT_SECRET}
    jwt.expiration=${JWT_EXPIRATION}

    # URL base (local por defecto; Render usa RENDER_EXTERNAL_URL o perfil prod)
    server.url=${RENDER_EXTERNAL_URL:http://localhost:8080}
    server.forward-headers-strategy=framework

    # OpenAPI / Swagger UI (springdoc)
    springdoc.api-docs.path=/v3/api-docs
    springdoc.swagger-ui.path=/swagger-ui.html
    springdoc.swagger-ui.operationsSorter=method
    springdoc.swagger-ui.tagsSorter=alpha
    springdoc.swagger-ui.doc-expansion=list
    springdoc.swagger-ui.try-it-out-enabled=true
    springdoc.swagger-ui.filter=true
    springdoc.swagger-ui.display-request-duration=true
    springdoc.show-actuator=false
    logging.level.org.springframework.web.socket=TRACE
    logging.level.org.springframework.messaging=TRACE

    ```
---

---
## 🛑 incidenete 3: Error jwt the specified key
**Descripción:** El error nos indica que el tamaño del los bytes del jwt.secret tiene que ser mayor o igual a  256 bytes

1. revisa los logs del contenedor
```bash
   docker logs nombre_contenedor
   ```
2. revisar que la variable de entorno de JWTSECRET tenga contenido mayor a 256 bytes 

```bash
JWT_SECRET=nQVxEfDkuPNxdpw5n3PJZsccUfFlBXcO7Vk2zsXaA5o=
``` 

---

## ⏪ Procedimiento de Emergencia: Rollback (Deshacer actualización)
Si una nueva versión (v2) causa un fallo crítico inmediato que no puede ser solucionado rápidamente:

1. Ejecute el siguiente comando para deshacer el ultimo commit:
```bash
git revert HEAD
```
2. Actualiza o arregla los errores de forma local.

3. Sube los cambios nuevamente a gthub
```bash
git push origin <tu_rama>
```