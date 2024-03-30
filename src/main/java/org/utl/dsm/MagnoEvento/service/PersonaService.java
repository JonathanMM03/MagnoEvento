package org.utl.dsm.MagnoEvento.service;

import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.utl.dsm.MagnoEvento.entity.Persona;
import org.utl.dsm.MagnoEvento.repository.PersonaRepository;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Transactional
    public Persona savePersona(Persona persona) {
        return personaRepository.save(persona);
    }

    @Transactional(readOnly = true)
    public Persona getPersonaById(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Persona not found with id: " + id));
    }

    @Transactional
    public void deletePersonaById(Long id) {
        personaRepository.deleteById(id);
    }
}
