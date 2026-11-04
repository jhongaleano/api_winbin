manual_de_instalaci_n.md
100%
# 🛠️ Manual de Instalación y Despliegue

## 1. Descripción del Proyecto
Este documento describe el proceso de despliegue de la API de winbin, configurada con Springboot y Docker

## 2. Arquitectura del Sistema
- **Backend:** SpringBot

## 3. Prerrequisitos de Software
Antes de iniciar, asegúrese de tener instalado:
* **Docker Engine** (v20.10 o superior)
* **Java**
* **Git**
* **MySql**
* Gestor de Xampp
* Sistema Operativo: [Ej: Linux Ubuntu 22.04, Windows 11 con WSL2, etc.]

## 4. Pasos de Instalación

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

#### Paso 5: Creacion de imagen

1. Verifica la configuracion correcta de tu archivo .env, de lo contrario java lanzará error y la imagen no se construira
2. Asegerate de tener el Docker Desktop activado
3. Empaquetar el proyecto en una imagen de docker:
```bash
docker build -t nombre_imagen .
```

### Paso 6: Correr el Contenedor de docker 
Ejecute los siguientes comandos para correr y confirmar que el sistema está operativo:

**1. Correr el contenedor de la API:**
```bash
docker run -p 8080:8080 --env-file .env nombre_imagen
```

**2. Verifiar el estado de los contenedores de la API:**
```bash
docker ps
```

**3. Verificar la conectividad de la API**
```bash
curl -i http://localhost:8080/api
```

##### **(Debe devolver `HTTP/1.1 200 OK`)**


## 5. Comandos de Operación Diaria
* **Ver logs en terminal:** `docker logs -f api-backend`
* **Apagar el entorno:** `docker down`