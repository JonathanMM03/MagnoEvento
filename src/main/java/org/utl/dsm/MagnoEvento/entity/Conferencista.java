package org.utl.dsm.MagnoEvento.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conferencista")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conferencista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long idConferencista;

    @Column(nullable = false, length = 1000)
    private String gradoAcademico;

    @Column(nullable = false, length = 1000)
    private String logros;

    @OneToOne
    @JoinColumn(name = "idPersona", nullable = false)
    private Persona persona;
}