manual_de_instalaci_n.md
100%
# 🛠️ Manual de Instalación y Despliegue

## 1. Descripción del Proyecto
Este documento describe el proceso completo de instalación, configuración y despliegue de la API, así como los puertos asignados, los accesos a los paneles de administración y los comandos de operación diaria.

## 2. Arquitectura del Sistema
| Servicio | Descripción | Contenedor |
|---|---|---|
| **API Backend** | Spring Boot (Java) | `api-backend` |
| **API Python** | Servicio complementario | `api-python` |
| **Nginx Proxy Manager** | Proxy inverso y certificados SSL | `nginx-proxy` |
| **Uptime Kuma** | Monitoreo de disponibilidad | `uptime-kuma` |
| **Dozzle** | Visor de logs de contenedores | `dozzle` |

Todos los servicios comparten la red interna `red-interna`, por lo que se comunican entre sí usando el nombre del contenedor (por ejemplo `http://api-backend:8080`).


## 3. Prerrequisitos de Software
Antes de iniciar, asegúrese de tener instalado:
* **Docker Engine** (v20.10 o superior)
* **Java**
* **Git**
* **MySQL** accesible desde el contenedor (local, XAMPP o remoto)
* Sistema operativo: Linux (Ubuntu 22.04 o similar), o Windows 11 con WSL2 y Docker Desktop

## 4. Asignación de Puertos

Estos son los puertos que cada servicio publica en el servidor (formato `host:contenedor`):

| Servicio | Puerto host | Puerto contenedor | Uso |
|---|---|---|---|
| API Backend | **8081** | 8080 | API REST de Winbin |
| API Python | **5000** | 5000 | Servicio Python |
| Dozzle | **8080** | 8080 | Visor de logs (web) |
| Nginx Proxy Manager | **80** | 80 | HTTP |
| Nginx Proxy Manager | **443** | 443 | HTTPS |
| Nginx Proxy Manager | **81** | 81 | Panel de administración |
| Uptime Kuma | **3001** | 3001 | Panel de monitoreo |

**Importante:**
* El puerto **8080 del servidor lo ocupa Dozzle**; por eso la API se expone en **8081**. No asigne el mismo puerto host a dos servicios.
* Verifique que los puertos estén libres antes de levantar los contenedores:
  ```bash
  sudo ss -tulpn | grep -E ':(80|81|443|3001|5000|8080|8081)\s'
  ```
  Si alguno está ocupado, cambie el número de la **izquierda** en `docker-compose.yml` (por ejemplo `"8082:8080"`), nunca el de la derecha.
* Los puertos **81, 3001 y 8080** exponen paneles de administración. En un servidor público, restrínjalos con firewall o acceda a ellos por Nginx Proxy Manager con autenticación.
* Si el `docker-compose.yml` del repositorio no incluye la sección `ports`, agréguela a cada servicio como se muestra en el paso 5.

## 5. Pasos de Instalación



#### Paso 1: Clonar el repositorio

```bash
git clone [https://github.com/jhongaleano/api_winbin.git]
```

#### Paso 2: Abrir la carpeta del proyecto

```bash
cd api_winbin
```

#### Paso 3: Abrir la carpeta en un editor de código

```bash
code .
```

#### Paso 4: Configuración de Variables de Entorno (Si aplica)
Copie el archivo de ejemplo y configure las variables necesarias:
```bash
cp .env.example .env
# Edite el archivo .env con su editor de texto (nano, vim, etc.) NO OLVIDES MODIFICAR EL NOMBRE DEL ARCHIVO de ".env.example" a ".env"
```

#### Paso 5: Verificar `docker-compose.yml` (puertos)
aignarle los puertos a cada servicio en su sección `ports`:
```yaml
services:
  dozzle:
    ports:
      - "8080:8080"
  nginx-proxy:
    ports:
      - "80:80"
      - "443:443"
      - "81:81"
  uptime-kuma:
    ports:
      - "3001:3001"
  api-backend:
    ports:
      - "8081:8080"
  api-python:
    ports:
      - "5000:5000"
```

### Paso 6: Construir y levantar todos los servicios
```bash
docker compose up -d --build
```
## 5. Verificación de Despliegue

**1. Verifiar el estado de los contenedores de la API:**
```bash
docker compose ps
```

**2. Verificar la conectividad de la API**
```bash
curl -i http://localhost:8081/api
```
Debe devolver `HTTP/1.1 200 OK`.

**3.  Accesos a los Paneles**

| Panel | URL |
|---|---|
| API (Swagger) | `http://IP_DEL_SERVIDOR:8081/swagger-ui.html` |
| Logs (Dozzle) | `http://IP_DEL_SERVIDOR:8080` |
| Nginx Proxy Manager | `http://IP_DEL_SERVIDOR:81` |
| Uptime Kuma | `http://IP_DEL_SERVIDOR:3001` |
 


## 7. Comandos de Operación Diaria
* **Ver logs de la API:** `docker compose logs -f api-backend`
* **Ver logs de todos los servicios:** `docker compose logs -f`
* **Reiniciar un servicio:** `docker compose restart api-backend`
* **Reconstruir tras cambios en el código:** `docker compose up -d --build api-backend`
* **Detener el entorno:** `docker compose down`
* **Detener y borrar volúmenes (¡borra datos!):** `docker compose down -v`