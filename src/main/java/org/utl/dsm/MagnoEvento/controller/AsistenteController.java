package org.utl.dsm.MagnoEvento.controller;

import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.ModelAndView;
import org.utl.dsm.MagnoEvento.entity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utl.dsm.MagnoEvento.repository.AsistenciaRepository;
import org.utl.dsm.MagnoEvento.repository.AsistenteRepository;
import org.utl.dsm.MagnoEvento.repository.PersonaRepository;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/asistente")
@Tag(name = "Asistente")
@CrossOrigin(origins = "*") // Consider restricting origins for better security
@Validated
public class AsistenteController {

    @Autowired
    private AsistenteRepository asistenteRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @GetMapping(value = "/all")
    @Operation(summary = "Obtiene una lista de todos los asistentes")
    public List<Asistente> getAllAsistentes() {
        return asistenteRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un asistente por su ID")
    public ResponseEntity<Asistente> getAsistenteById(@PathVariable Long id) {
        return asistenteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Asistente no encontrado con ID: " + id));
    }

    @GetMapping("/findByEmail/{correo}")
    @Operation(summary = "Busca un asistente por su correo")
    public ResponseEntity<Asistente> getAsistenteByCorreo(@PathVariable String correo) {
        Asistente asistente = asistenteRepository.findByCorreo(correo);
        if (asistente != null) {
            return ResponseEntity.ok(asistente);
        } else {
            throw new ResourceNotFoundException("Asistente no encontrado para el correo: " + correo);
        }
    }

    @GetMapping("/findByMatricula/{matricula}")
    @Operation(summary = "Busca un asistente por su matrícula")
    public ResponseEntity<Asistente> getAsistenteByMatricula(@PathVariable String matricula) {
        Asistente asistente = asistenteRepository.findByMatricula(matricula);
        if (asistente != null) {
            return ResponseEntity.ok(asistente);
        } else {
            throw new ResourceNotFoundException("Asistente no encontrado para la matrícula: " + matricula);
        }
    }


    @PostMapping("/")
    @Operation(summary = "Crea un nuevo asistente")
    public ResponseEntity<Asistente> createAsistente(@RequestBody Asistente asistente) {
        Persona persona = asistente.getPersona();
        if (persona != null) {
            // Verificar si la persona ya tiene un ID asignado, si no lo tiene, guardarla
            if (persona.getIdPersona() == null) {
                persona = personaRepository.save(persona);
                asistente.setPersona(persona);
            }
        }

        Asistente savedAsistente = asistenteRepository.save(asistente);
        return new ResponseEntity<>(savedAsistente, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un asistente por su ID")
    public ResponseEntity<Asistente> updateAsistenteById(@PathVariable Long id,
                                                       @RequestBody Asistente asistenteDetails) {
        return asistenteRepository.findById(id)
                .map(asistente -> {
                    asistente.setMatricula(asistenteDetails.getMatricula());
                    asistente.setCorreo(asistenteDetails.getCorreo());
                    asistente.setGradoAcademico(asistenteDetails.getGradoAcademico());
                    asistente.setFoto(asistenteDetails.getFoto());
                    return ResponseEntity.ok(asistenteRepository.save(asistente));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Asistente no encontrado con ID: " + id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un asistente por su ID")
    public ResponseEntity<?> deleteAsistenteById(@PathVariable Long id) {
        asistenteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private List<Conferencia> getConferenciasConfirmadasByMatricula(String matricula) {
        // Consultar la base de datos para obtener las conferencias confirmadas por la matrícula
        List<Conferencia> conferencias = new ArrayList<>();
        List<Conferencia> conferencias1 = asistenciaRepository.findConferenciasByMatricula(matricula);
        conferencias.addAll(conferencias1);
        return conferencias;
    }

    @GetMapping("/generar/{matricula}")
    public ModelAndView generarCertificado(@PathVariable String matricula) {
        ModelAndView modelAndView = new ModelAndView("diploma");

        // Obtener la información del asistente y sus asistencias confirmadas
        Asistente asistente = asistenteRepository.findByMatricula(matricula);
        List<Conferencia> conferencias = getConferenciasConfirmadasByMatricula(matricula);

        if (asistente == null || conferencias.isEmpty()) {
            // Si no se encuentra el asistente o no tiene asistencias confirmadas, devolver un error
            // Aquí podrías manejar el error según tus necesidades
            modelAndView.setViewName("error");
            return modelAndView;
        }

        // Pasar los datos al modelo para que Thymeleaf los pueda utilizar en la vista
        modelAndView.addObject("nombreAsistente", String.format("%s %s %s", asistente.getPersona().getNombre(), asistente.getPersona().getApellidoPaterno(), asistente.getPersona().getApellidoMaterno()));
        modelAndView.addObject("matricula", asistente.getMatricula());
        modelAndView.addObject("nombreConferencia1", !conferencias.isEmpty() ? conferencias.get(0).getNombre() : "[Conference 1 Name]");
        modelAndView.addObject("nombreConferencia2", conferencias.size() > 1 ? conferencias.get(1).getNombre() : "[Conference 2 Name]");

        // Devolver el objeto ModelAndView con la vista y los parámetros
        return modelAndView;
    }

    private String generarCertificadoHtml(Asistente asistente, List<Conferencia> conferencias) {
        // Aquí implementarías la generación del certificado HTML usando los datos del asistente y las conferencias
        // Puedes utilizar bibliotecas como Thymeleaf, FreeMarker, Velocity o simplemente concatenar cadenas de texto HTML
        // Por ejemplo, utilizando String.format para insertar los datos dinámicos en el HTML
        String nombreAsistente = asistente.getPersona().getNombre();
        String matricula = asistente.getMatricula();
        String nombreConferencia1 = conferencias.size() > 0 ? conferencias.get(0).getNombre() : "[Conference 1 Name]";
        String nombreConferencia2 = conferencias.size() > 1 ? conferencias.get(1).getNombre() : "[Conference 2 Name]";

        String certificadoHtml = String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js" />
            <meta name="description" content="">
            <meta name="keywords" content="">
            <meta name="author" content="Jonathan Moreno Muñoz">
            <meta name="author" content="Oscar Octavio Alvarado Cornejo">
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
        
            <script src="https://kit.fontawesome.com/5b2670a1a9.js" crossorigin="anonymous"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
            <title>Conference Attendance Diploma</title>
            <link rel="stylesheet" href="css/style.css" type="text/css">
        </head>
        <body>
        <div class="diploma-container">
            <div class="diploma-logo diploma-logo-left">
                <img src="img/mlogo.png" alt="Microsoft Logo" width="80">
            </div>
            <br>
            <br>
            <div class="diploma-header">
                <h1>Diploma de asistencia a las conferencias</h1>
            </div>
            <div class="diploma-content">
                <p>Este diploma es para:</p>
                <p><strong>Nombre:</strong> <span id="nombreAsistente">%s</span></p>
                <p><strong>Matricula:</strong> <span id="matricula">%s</span></p>
                <p>Por asistir a las siguientes conferencias:</p>
                <ol>
                    <li><span id="nombreConferencia1">%s</span></li>
                    <li><span id="nombreConferencia2">%s</span></li>
                </ol>
                <p>¡Felicitaciones por tu participación y ayuda a expandir tu conocimiento!</p>
            </div>
            <div class="diploma-signature">
                <p>Firma del primer conferencista</p>
                <img src="img/elon_firma.png" alt="Signature" width="150">
            </div>
            <div class="diploma-signature-second">
                <p>Firma del segundo conferencista</p>
                <img src="img/billgates-removebg-preview.png" alt="Signature" width="150">
            </div>
            <div class="diploma-logo diploma-logo-right">
                <img src="img/spacex_logo-removebg-preview.png" alt="SpaceX Logo" width="200">
            </div>
            <!-- Add your university logo here -->
            <div class="diploma-logo diploma-logo-university">
                <img src="img/utlogo-removebg-preview.png" alt="University Logo" width="100">
            </div>
        </div>
        </body>
        </html>
        """, nombreAsistente, matricula, nombreConferencia1, nombreConferencia2);

        return certificadoHtml;
    }
}