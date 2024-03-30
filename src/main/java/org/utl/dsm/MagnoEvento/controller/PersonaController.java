package org.utl.dsm.MagnoEvento.controller; // Assuming you want to move it to a separate package

import org.utl.dsm.MagnoEvento.entity.Persona;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utl.dsm.MagnoEvento.repository.PersonaRepository;

import java.util.List;

@RestController
@RequestMapping("/persona")
@Tag(name = "Persona")
@CrossOrigin(origins = "*") // Consider restricting origins for better security
@Validated
public class PersonaController {

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("/all")
    @Operation(summary = "Obtiene una lista de todas las personas")
    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una persona por su ID")
    public ResponseEntity<Persona> getPersonaById(@PathVariable Long id) {
        Persona persona = personaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Persona no encontrada con ID: " + id));

        return ResponseEntity.ok(persona); // Use ResponseEntity for flexibility
    }

    @PostMapping
    @Operation(summary = "Crea una nueva persona")
    public ResponseEntity<Persona> createPersona(@RequestBody Persona persona) {
        // Validate the persona object using @Valid annotation
        Persona savedPersona = personaRepository.save(persona);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPersona);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una persona por su ID")
    public ResponseEntity<Persona> updatePersonaById(@PathVariable Long id,
                                                     @RequestBody Persona personaDetails) {
        Persona existingPersona = personaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Persona no encontrada con ID: " + id));

        existingPersona.setNombre(personaDetails.getNombre());
        existingPersona.setApellidoPaterno(personaDetails.getApellidoPaterno());
        existingPersona.setApellidoMaterno(personaDetails.getApellidoMaterno());

        Persona updatedPersona = personaRepository.save(existingPersona);
        return ResponseEntity.ok(updatedPersona);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una persona por su ID")
    public ResponseEntity<?> deletePersonaById(@PathVariable Long id) {
        personaRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // No content response is better here
    }
}