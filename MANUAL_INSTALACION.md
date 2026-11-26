# WINBIN | Manual de Instalación y Técnico
----
## Descripción del Proyecto
Este documento describe el proceso de despliegue de la API de winbin, configurada con Springboot y Docker

---

### Arquitectura del Sistema

El ecosistema funciona bajo una arquitectura desacoplada basada en microservicios y clientes multiplataforma:

* **Backend Transaccional (api_winbin):** API REST desarrollada en: **Java + Spring Boot**, encargada de la lógica de negocio, autenticación JWT, gestión de puntos, rankings por periodo, auditoría y comunicación serial con el hardware.
  
* **Microservicio de Visión Artificial (winbin-ia):** Servicio asíncrono en **Python + FastAPI** integrado con **Groq API** y **Cloudinary** para identificar material (plástico/cartón), tamaño (pequeño, mediano, grande) y filtrar residuos no admitidos.
  
* **Base de Datos Relacional:** **MySQL** (alojada en Aiven Cloud), normalizada en Tercera Forma Normal (3FN) con 9 tablas **(usuarios, cursos, materiales, categoriaPuntaje, periodoRanking, detalleSession, registroIA, historialGanadores, auditoria)**.

* **Clientes Multiplataforma (front_winbin / frontend_winbin_maquina):** Desarrollados en **Flutter / Dart** (interfaz táctil, aplicación móvil de estudiantes y WinBin Panel web para administradores).
  
* **Control Físico (IoT / Hardware):** Microcontrolador **Arduino Uno** conectado vía USB serial (jSerialComm) que acciona servomotores **MG995** para la apertura automática de compuertas.

## Stack Tecnológico

| **Capa** | **Tecnologías** |
| :--- | :--- |
| **Backend Core** | Java 17+, Spring Boot, Spring Security (JWT), JPA/Hibernate, jSerialComm |
| **Microservicio IA** | Python 3.11+, FastAPI, Uvicorn, Groq Vision API, Cloudinary SDK |
| **Frontend** | Flutter, Dart, Material 3 |
| **Base de Datos** | MySQL 8.0 (Aiven Cloud) |
| **Hardware** | Arduino Uno, Servomotores MG995, Cámara USB |
| **DevOps & Monitoreo** | Docker, Docker Compose, Nginx Proxy Manager, AWS EC2, Render, Uptime Kuma, Dozzle |

## Prequisitos Previos

* **Java JDK 17** o superior y **Maven 3.8+**
* **Python 3.10+** (para el microservicio de IA)
* **Flutter SDK 3.x** (para compilar los clientes web/móvil/kiosco)
* **Docker y Docker Compose** (opcional para despliegue contenedorizado)
* **Arduino IDE** y placa **Arduino Uno** conectada por puerto USB (/dev/ttyACM0 en Linux o COMx en Windows)

---
## Pasos de Insralación Frontend

#### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/sofisdevops/WinBin.git

```

#### Paso 2: Abrir la carpeta del proyecto

```bash
cd winbin
```

#### Paso 3: Abrir la carpeta en un editor de código

```bash
code .
```

#### Paso 4: Instalar dependencias

```bash
flutter pub get
```

#### Paso 5: Correr el proyecto

* Ejecutar desde chrome:
```bash
flutter run -d chrome
```

* Ejecutar desde edge:
```bash
flutter run -d edge
```

* Si tienes un emulador abierto o un telefono por USB:
```bash
flutter run
```
## 5. Verificación de Despliegue

* Si tienes varios conectados, primero verifica el ID de tus dispositivos y luego corres su ID
```bash
flutter devices
flutter run -d <ID_del_dispositivo>
```
---

## Pasos de Instalación Backend

## Asignación de Puertos

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


#### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/jhongaleano/api_winbin.git
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

En el cofigo se encuentra un archivo .env de ejemplo, copie el archivo y configure las variables necesarias. No olvides modificar el nombre del archivo **".env.example"** a **".env**

```bash
cp .env.example .env
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