package org.utl.dsm.MagnoEvento.repository;

import org.utl.dsm.MagnoEvento.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByNombre(String nombre);
}