package org.utl.dsm.MagnoEvento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.utl.dsm.MagnoEvento.entity.Conferencista;
import org.utl.dsm.MagnoEvento.entity.Persona;

import java.util.Optional;

@Repository
public interface ConferencistaRepository extends JpaRepository<Conferencista, Long> {
    Optional<Conferencista> findByPersona_Nombre(String nombre);
}