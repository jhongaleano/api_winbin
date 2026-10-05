package tareas.demo.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tareas.demo.dto.GraficoDTO;
import tareas.demo.models.DetalleSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public interface DetalleSessionRepository
                extends JpaRepository<DetalleSession, UUID>, JpaSpecificationExecutor<DetalleSession> {
        @Query(value = "SELECT DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') AS fecha, COUNT(d.id_session) AS cantidad " +
                        "FROM detalleSession d " +
                        "GROUP BY DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') " +
                        "ORDER BY DATE_FORMAT(d.fecha_hora, '%Y-%m-%d') ASC", nativeQuery = true)
        List<GraficoDTO> obtenerSesionesPorDia();

        static Specification<DetalleSession> conFiltros(String documento, LocalDate fecha, String idPeriodo) {
                return (root, query, criteriaBuilder) -> {
                        List<Predicate> predicates = new ArrayList<>();

                        if (documento != null && !documento.isBlank()) {
                                predicates.add(criteriaBuilder.equal(root.get("documento").get("documento"),
                                                documento));
                        }

                        if (fecha != null) {
                                LocalDateTime inicioDia = fecha.atStartOfDay();
                                LocalDateTime finDia = fecha.atTime(LocalTime.MAX);
                                predicates.add(criteriaBuilder.between(root.get("fechaHora"), inicioDia, finDia));
                        }

                        if (idPeriodo != null && !idPeriodo.isBlank()) {
                                predicates.add(criteriaBuilder.equal(root.get("id_periodo").get("idPeriodo"),
                                                idPeriodo));
                        }

                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                };
        }

}
