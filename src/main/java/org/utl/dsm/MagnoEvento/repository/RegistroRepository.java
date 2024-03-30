package org.utl.dsm.MagnoEvento.repository;

import org.utl.dsm.MagnoEvento.entity.Registros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RegistroRepository extends JpaRepository<Registros, Long> {
}
