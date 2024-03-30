package org.utl.dsm.MagnoEvento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.utl.dsm.MagnoEvento.entity.Conferencia;

import java.time.LocalDate;
import java.util.List;

public interface ConferenciaRepository extends JpaRepository<Conferencia, Long> {

    @Query("SELECT c FROM Conferencia c WHERE c.fechaInicio >= :fechaInicio AND c.fechaFin <= :fechaFin")
    List<Conferencia> findEventosDisponiblesEnRango(LocalDate fechaInicio, LocalDate fechaFin);
}

