package org.utl.dsm.MagnoEvento.repository;

import org.utl.dsm.MagnoEvento.entity.Asistente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AsistenteRepository extends JpaRepository<Asistente,Long> {
    Asistente findByCorreo(String correo);
    Asistente findByMatricula(String matricula);
}
