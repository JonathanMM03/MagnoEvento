package org.utl.dsm.MagnoEvento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utl.dsm.MagnoEvento.entity.Conferencista;
import org.utl.dsm.MagnoEvento.entity.Persona;
import org.utl.dsm.MagnoEvento.exception.ConferencistaNotFoundException;
import org.utl.dsm.MagnoEvento.exception.ErrorResponseBody;
import org.utl.dsm.MagnoEvento.repository.ConferencistaRepository;
import org.utl.dsm.MagnoEvento.repository.PersonaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conferencista")
@Tag(name = "Conferencista")
@CrossOrigin(origins = "*") // Consider restricting origins for better security
@Validated
public class ConferencistaController {

    @Autowired
    private ConferencistaRepository conferencistaRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un conferencista por su ID")
    public ResponseEntity<Conferencista> getAsistenteById(@PathVariable Long id) {
        return conferencistaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Conferencista no encontrado con ID: " + id));
    }

    @GetMapping("find/{nombre}")
    public ResponseEntity<?> getConferencistaByNombre(@PathVariable String nombre) {
        Optional<Conferencista> conferencista = conferencistaRepository.findByPersona_Nombre(nombre);
        if (conferencista.isPresent()) {
            return ResponseEntity.ok(conferencista);
        } else {
            return ResponseEntity.ok(new ErrorResponseBody("No encontrado"));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los conferencistas", description = "Obtiene una lista de todos los conferencistas")
    @ApiResponse(responseCode = "200", description = "Lista de conferencistas", content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Conferencista>> getAllConferencistas() {
        List<Conferencista> conferencistas = conferencistaRepository.findAll();
        return ResponseEntity.ok(conferencistas);
    }

    @PostMapping("/")
    @Operation(summary = "Crear un nuevo conferencista", description = "Crea un nuevo conferencista")
    @ApiResponse(responseCode = "201", description = "Conferencista creado", content = @Content(mediaType = "application/json"))
    public ResponseEntity<Conferencista> createConferencista(@RequestBody Conferencista conferencista) {
        Conferencista savedConferencista = conferencistaRepository.save(conferencista);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedConferencista);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un conferencista", description = "Actualiza un conferencista existente")
    @ApiResponse(responseCode = "200", description = "Conferencista actualizado", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Conferencista no encontrado")
    public ResponseEntity<Conferencista> updateConferencista(@PathVariable Long id, @RequestBody Conferencista conferencistaDetails) {
        Conferencista conferencista = conferencistaRepository.findById(id)
                .orElseThrow(() -> new ConferencistaNotFoundException("Conferencista no encontrado con ID: " + id));

        conferencista.setGradoAcademico(conferencistaDetails.getGradoAcademico());
        conferencista.setLogros(conferencistaDetails.getLogros());

        Conferencista updatedConferencista = conferencistaRepository.save(conferencista);
        return ResponseEntity.ok(updatedConferencista);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un conferencista", description = "Elimina un conferencista por su ID")
    @ApiResponse(responseCode = "204", description = "Conferencista eliminado con éxito")
    @ApiResponse(responseCode = "404", description = "Conferencista no encontrado")
    public ResponseEntity<Void> deleteConferencista(@Parameter(description = "ID del conferencista a eliminar", required = true) @PathVariable Long id) {
        conferencistaRepository.findById(id).orElseThrow(() -> new ConferencistaNotFoundException("Conferencista no encontrado con ID: " + id));
        conferencistaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}