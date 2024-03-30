package org.utl.dsm.MagnoEvento.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.utl.dsm.MagnoEvento.entity.Asistencia;
import org.utl.dsm.MagnoEvento.entity.Conferencia;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    @Query("SELECT a.conferencia FROM Asistencia a WHERE a.matricula = :matricula")
    List<Conferencia> findConferenciasByMatricula(String matricula);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Asistencia a WHERE a.matricula = :matricula AND a.conferencia.idConferencia = :conferenciaId")
    boolean existsByMatriculaAndConferencia(String matricula, Long conferenciaId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Asistencia a WHERE a.matricula = :matricula")
    void deleteByMatricula(String matricula);

    @Query("SELECT a FROM Asistencia a WHERE a.conferencia.idConferencia = :idConferencia")
    List<Asistencia> findByConferenciaId(Long idConferencia);
}
