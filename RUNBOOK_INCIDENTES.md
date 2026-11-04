
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
   
2. Revise en la docuemntacion (Swagger) si tiene los permisos suficientes para el endponit:
    - https://winbin-api.duckdns.org/swagger-ui/index.html#/
   
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