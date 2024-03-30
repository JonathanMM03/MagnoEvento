package org.utl.dsm.MagnoEvento.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.utl.dsm.MagnoEvento.entity.Asistencia;
import org.utl.dsm.MagnoEvento.entity.Asistente;
import org.utl.dsm.MagnoEvento.entity.Conferencia;
import org.utl.dsm.MagnoEvento.repository.AsistenciaRepository;
import org.utl.dsm.MagnoEvento.repository.AsistenteRepository;
import org.utl.dsm.MagnoEvento.repository.ConferenciaRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/asistencia")
@Tag(name = "Asistencia")
@CrossOrigin(origins = "*") // Consider restricting origins for better security
@Validated
public class AsistenciaController {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private AsistenteRepository asistenteRepository;

    @Autowired
    private ConferenciaRepository conferenciaRepository;

    // Obtener las credenciales del archivo de propiedades
    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Operation(summary = "Obtener conferencias disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se devuelven las conferencias disponibles"),
            @ApiResponse(responseCode = "500", description = "Se produjo un error interno")
    })
    @GetMapping("/available")
    public ResponseEntity<List<Conferencia>> getAvailableConferences() {
        // Obtener todas las conferencias disponibles
        List<Conferencia> conferenciasDisponibles = conferenciaRepository.findAll();

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Lista para almacenar las conferencias disponibles en el rango de fechas
        List<Conferencia> conferenciasEnRango = new ArrayList<>();

        // Iterar sobre todas las conferencias disponibles
        for (Conferencia conferencia : conferenciasDisponibles) {
            LocalDate fechaInicioConferencia = LocalDate.parse(conferencia.getFechaInicio(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fechaFinConferencia = LocalDate.parse(conferencia.getFechaFin(), DateTimeFormatter.ISO_LOCAL_DATE);

            // Verificar si la fecha actual está dentro del rango de fechas de la conferencia
            if (fechaActual.isEqual(fechaInicioConferencia) || fechaActual.isEqual(fechaFinConferencia) ||
                    (fechaActual.isAfter(fechaInicioConferencia) && fechaActual.isBefore(fechaFinConferencia))) {
                conferenciasEnRango.add(conferencia);
            }
        }

        return ResponseEntity.ok(conferenciasEnRango);
    }


    @GetMapping("/confirmar-asistencia/{matricula}")
    @Operation(summary = "Confirmar asistencia a un evento")
    public ResponseEntity<?> confirmarAsistencia(@PathVariable String matricula) {
        // Verificar si la matrícula está en la base de datos
        Optional<Asistente> asistenteOptional = Optional.ofNullable(asistenteRepository.findByMatricula(matricula));
        if (asistenteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("""
                {"response":"%s"}
                ""","No se encontró ningún asistente con esa matrícula"));
        }

        Asistente asistente = asistenteOptional.get();

        // Obtener todas las conferencias disponibles
        List<Conferencia> conferenciasDisponibles = conferenciaRepository.findAll();

        // Verificar si hay eventos disponibles en el rango de fechas
        String mensajeConfirmacion;
        if (hayEventosDisponibles(asistente,conferenciasDisponibles)) {
            mensajeConfirmacion = "Registro de asistencia exitoso. Se ha enviado un correo de confirmación con las conferencias a las que asistió.";
        } else {
            mensajeConfirmacion = "No hay eventos disponibles en el momento";
        }

        return ResponseEntity.ok(String.format("""
                {"response":"%s"}
                """, mensajeConfirmacion));
    }

    @Operation(summary = "Obtener asistencias por ID de conferencia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se devuelven las asistencias para la conferencia especificada"),
            @ApiResponse(responseCode = "404", description = "No se encontraron asistencias para la conferencia especificada")
    })
    @GetMapping("/{idConferencia}")
    public ResponseEntity<List<Asistencia>> getAsistenciasByConferencia(@PathVariable Long idConferencia) {
        List<Asistencia> asistencias = asistenciaRepository.findByConferenciaId(idConferencia);
        if (asistencias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(asistencias);
    }

    @Operation(summary = "Obtener asistencias para todas las conferencias")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se devuelven las asistencias para todas las conferencias"),
            @ApiResponse(responseCode = "404", description = "No se encontraron asistencias para ninguna de las conferencias")
    })
    @GetMapping//(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAsistenciasForAllConferencias() {
        List<Conferencia> conferencias = conferenciaRepository.findAll();

        Map<String, Object> asistenciasPorConferencia = new HashMap<>();

        for (Conferencia conferencia : conferencias) {
            List<Asistencia> asistenciasConferencia = asistenciaRepository.findByConferenciaId(conferencia.getIdConferencia());
            List<Map<String, Object>> asistenciasJson = new ArrayList<>();

            // Convertir cada objeto Asistencia a un mapa y agregarlo a la lista
            for (Asistencia asistencia : asistenciasConferencia) {
                Map<String, Object> asistenciaMap = new HashMap<>();
                asistenciaMap.put("asistente", asistenteRepository.findByMatricula(asistencia.getMatricula())); // Suponiendo que Asistencia tiene una propiedad "id"
                // Agregar otras propiedades de Asistencia si es necesario
                asistenciasJson.add(asistenciaMap);
            }

            // Agregar la lista de asistencias al detalle de la conferencia
            Map<String, Object> detalleConferencia = new HashMap<>();
            detalleConferencia.put("conferencia", conferencia);
            detalleConferencia.put("cantidadAsistentes", asistenciasConferencia.size());
            detalleConferencia.put("asistencias", asistenciasJson); // Aquí se agrega la lista de asistencias en formato JSON
            asistenciasPorConferencia.put(conferencia.toString(), detalleConferencia);
        }

        System.out.println(asistenciasPorConferencia.toString());

        return ResponseEntity.ok(asistenciasPorConferencia);
    }

    public boolean hayEventosDisponibles(Asistente asistente, List<Conferencia> conferenciasDisponibles) {
        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Verificar si hay conferencias en el rango de fechas
        List<Conferencia> conferenciasEnRango = new ArrayList<>();
        for (Conferencia conferencia : conferenciasDisponibles) {
            LocalDate fechaInicioConferencia = LocalDate.parse(conferencia.getFechaInicio(), DateTimeFormatter.ISO_LOCAL_DATE);
            LocalDate fechaFinConferencia = LocalDate.parse(conferencia.getFechaFin(), DateTimeFormatter.ISO_LOCAL_DATE);

            // Comparar si el día actual está dentro del rango de fechas de la conferencia
            if ((fechaActual.isEqual(fechaInicioConferencia) || fechaActual.isAfter(fechaInicioConferencia)) &&
                    (fechaActual.isEqual(fechaFinConferencia) || fechaActual.isBefore(fechaFinConferencia))) {
                conferenciasEnRango.add(conferencia);
            }
        }

        if (!conferenciasEnRango.isEmpty()) {
            // Verificar si el asistente ya ha asistido a alguna de las conferencias en el rango
            List<Conferencia> conferenciasNoAsistidas = new ArrayList<>();
            for (Conferencia conferencia : conferenciasEnRango) {
                boolean asistenciaRegistrada = asistenciaRepository.existsByMatriculaAndConferencia(
                        asistente.getMatricula(), conferencia.getIdConferencia());
                if (!asistenciaRegistrada) {
                    conferenciasNoAsistidas.add(conferencia);
                }
            }

            if (!conferenciasNoAsistidas.isEmpty()) {
                // Registrar las asistencias para las conferencias no asistidas
                for (Conferencia conferencia : conferenciasNoAsistidas) {
                    Asistencia asistencia = new Asistencia();
                    asistencia.setMatricula(asistente.getMatricula());
                    asistencia.setConferencia(conferencia);
                    asistenciaRepository.save(asistencia);
                }

                StringBuilder mensaje = new StringBuilder();
                mensaje.append("Hay eventos disponibles para la matrícula ").append(asistente.getMatricula()).append(".\n");
                mensaje.append("Eventos disponibles:\n");
                for (Conferencia conferencia : conferenciasNoAsistidas) {
                    mensaje.append("- ").append(conferencia.getNombre()).append("\n");
                    mensaje.append("  Fecha de inicio: ").append(conferencia.getFechaInicio()).append("\n");
                    mensaje.append("  Fecha de fin: ").append(conferencia.getFechaFin()).append("\n");
                    mensaje.append("  Conferencista: ").append(conferencia.getConferencista().getPersona().getNombre())
                            .append(" ").append(conferencia.getConferencista().getPersona().getApellidoPaterno()).append("\n");
                    mensaje.append("  Grado académico del conferencista: ").append(conferencia.getConferencista().getGradoAcademico()).append("\n");
                    mensaje.append("  Logros del conferencista: ").append(conferencia.getConferencista().getLogros()).append("\n");
                    mensaje.setLength(0);
                }
                // Enviar correo de confirmación al correo electrónico del asistente
                enviarCorreoConfirmacion(asistente.getCorreo(), "Confirmación de Asistencia", mensaje.toString());
                // Puedes enviar este mensaje por correo electrónico o manejarlo de acuerdo a tus necesidades
                System.out.println(mensaje.toString());
                return true;
            } else {
                // Si el asistente ya asistió a todas las conferencias disponibles en el rango
                return false;
            }
        } else {
            return false;
        }
    }

    @DeleteMapping("/clear/{matricula}")
    @Operation(summary = "Anular asistencias por matrícula")
    public ResponseEntity<?> clearAttendances(@PathVariable String matricula) {
        // Verificar si la matrícula está en la base de datos de asistentes
        Optional<Asistente> asistenteOptional = Optional.ofNullable(asistenteRepository.findByMatricula(matricula));
        if (asistenteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("""
            {"response":"%s"}
            """, "No se encontró ningún asistente con esa matrícula"));
        }

        // Eliminar las asistencias asociadas a la matrícula proporcionada
        asistenciaRepository.deleteByMatricula(matricula);

        return ResponseEntity.ok(String.format("""
            {"response":"%s"}
            """, "Se han anulado todas las asistencias para la matrícula: " + matricula));
    }

    private void enviarCorreoConfirmacion(String correoDestino, String asunto, String mensaje) {
        // Configurar el mensaje de correo
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(correoDestino);
        mailMessage.setSubject(asunto);
        mailMessage.setText(mensaje);

        // Configurar las credenciales para enviar el correo
        mailMessage.setFrom(emailUsername);

        // Enviar el correo electrónico
        javaMailSender.send(mailMessage);
    }
}
