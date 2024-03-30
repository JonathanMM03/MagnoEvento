package org.utl.dsm.MagnoEvento.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "conferencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conferencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long idConferencia;

    @OneToOne // Many conferences can have one speaker
    @JoinColumn(name = "idConferencista", nullable = false)
    @JsonProperty("conferencista")
    @JsonIgnore
    private Conferencista conferencista;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String fechaInicio;

    @Column(nullable = false)
    private String fechaFin;
}
