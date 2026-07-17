package tareas.demo.config;

import tareas.demo.models.Auditoria;

public record AuditoriaEvent(Auditoria auditoria, String documentoUsuario) {}
