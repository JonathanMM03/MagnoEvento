package org.utl.dsm.MagnoEvento.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    
    private String name;
    
    private String type;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] fileContent;
}
