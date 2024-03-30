package org.utl.dsm.MagnoEvento.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "registros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registros {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long idRegistro;

    @ManyToOne // Many registrations can have one assistant
    @JoinColumn(name = "idAsistente", nullable = false)
    private Asistente asistente;

    @ManyToOne // Many registrations can have one speaker
    @JoinColumn(name = "idConferencista", nullable = false)
    private Conferencista conferencista;

    @Column(nullable = false)
    private String fechaRegistro;
}
