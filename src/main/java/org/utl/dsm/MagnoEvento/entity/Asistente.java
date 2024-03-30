package org.utl.dsm.MagnoEvento.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asistente")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asistente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long idAsistente;

    @Column(nullable = false)
    private String matricula;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private Short gradoAcademico = 1; // Default value

    @Column // Large object
    @Lob
    private String foto;

    @OneToOne
    @JoinColumn(name = "idPersona", nullable = false)
    private Persona persona;
}