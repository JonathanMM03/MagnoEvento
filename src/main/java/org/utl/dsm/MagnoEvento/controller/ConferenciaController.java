package org.utl.dsm.MagnoEvento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utl.dsm.MagnoEvento.entity.Conferencia;
import org.utl.dsm.MagnoEvento.entity.Conferencista;
import org.utl.dsm.MagnoEvento.exception.ConferenciaNotFoundException;
import org.utl.dsm.MagnoEvento.exception.ConferencistaNotFoundException;
import org.utl.dsm.MagnoEvento.exception.ErrorResponseBody;
import org.utl.dsm.MagnoEvento.repository.ConferenciaRepository;
import org.utl.dsm.MagnoEvento.repository.ConferencistaRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/conferencia")
@Tag(name = "Conferencia")
@CrossOrigin(origins = "*") // Consider restricting origins for better security
@Validated
public class ConferenciaController {

    @Autowired
    private ConferenciaRepository conferenciaRepository;

    @Autowired
    private ConferencistaRepository conferencistaRepository;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una conferencia por su ID", description = "Obtiene una conferencia por su ID")
    @ApiResponse(responseCode = "200", description = "Conferencia encontrada", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Conferencia no encontrada")
    public ResponseEntity<?> getConferenciaById(@Parameter(description = "ID de la conferencia a buscar", required = true) @PathVariable Long id) {
        try {
            Optional<Conferencia> optionalConferencia = conferenciaRepository.findById(id);
            if (optionalConferencia.isPresent()) {
                Conferencia conferencia = optionalConferencia.get();
                return ResponseEntity.ok(conferencia);
            } else {
                // Devuelve un objeto JSON con el mensaje de error
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponseBody("Conferencia no encontrada con ID: " + id));
            }
        } catch (Exception e) {
            // En caso de error, devuelve un objeto JSON con el mensaje de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseBody("Error al procesar la solicitud"));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todas las conferencias", description = "Obtiene una lista de todas las conferencias")
    @ApiResponse(responseCode = "200", description = "Lista de conferencias", content = @Content(mediaType = "application/json"))
    public ResponseEntity<List<Conferencia>> getAllConferencias() {
        List<Conferencia> conferencias = conferenciaRepository.findAll();
        return ResponseEntity.ok(conferencias);
    }

    @PostMapping("/{nombre}")
    @Operation(summary = "Crear una nueva conferencia", description = "Crea una nueva conferencia")
    @ApiResponse(responseCode = "201", description = "Conferencia creada", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> createConferencia(@PathVariable String nombre, @RequestBody Conferencia conferencia) {
        try {
            // Verificar si el conferencista ya existe en la base de datos
            Optional<Conferencista> conferencistaOptional = conferencistaRepository.findByPersona_Nombre(nombre);
            if (conferencistaOptional.isPresent()) {
                Conferencista conferencistaExistente = conferencistaOptional.get();

                // Asignar el conferencista existente a la conferencia
                conferencia.setConferencista(conferencistaExistente);

                // Guardar la conferencia en la base de datos
                Conferencia savedConferencia = conferenciaRepository.save(conferencia);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedConferencia);
            } else {
                // Manejar el caso cuando el conferencista no se encuentra
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseBody("Conferencista no encontrado con nombre: " + nombre));
            }
        } catch (Exception e) {
            // Manejar otras excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseBody("Error al procesar la solicitud"));
        }
    }
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una conferencia", description = "Actualiza una conferencia existente")
    @ApiResponse(responseCode = "200", description = "Conferencia actualizada", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Conferencia no encontrada")
    public ResponseEntity<Conferencia> updateConferencia(@PathVariable Long id, @RequestBody Conferencia conferenciaDetails) {
        Conferencia conferencia = conferenciaRepository.findById(id)
                .orElseThrow(() -> new ConferenciaNotFoundException("Conferencia no encontrada con ID: " + id));

        conferencia.setNombre(conferenciaDetails.getNombre());
        conferencia.setFechaInicio(conferenciaDetails.getFechaInicio());
        conferencia.setFechaFin(conferenciaDetails.getFechaFin());

        Conferencia updatedConferencia = conferenciaRepository.save(conferencia);
        return ResponseEntity.ok(updatedConferencia);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una conferencia", description = "Elimina una conferencia por su ID")
    @ApiResponse(responseCode = "204", description = "Conferencia eliminada con éxito")
    @ApiResponse(responseCode = "404", description = "Conferencia no encontrada")
    public ResponseEntity<Void> deleteConferencia(@Parameter(description = "ID de la conferencia a eliminar", required = true) @PathVariable Long id) {
        conferenciaRepository.findById(id).orElseThrow(() -> new ConferenciaNotFoundException("Conferencia no encontrada con ID: " + id));
        conferenciaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}