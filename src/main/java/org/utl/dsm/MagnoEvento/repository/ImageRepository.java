package org.utl.dsm.MagnoEvento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.utl.dsm.MagnoEvento.entity.Resources;

public interface ImageRepository extends JpaRepository<Resources, Long> {
    // Métodos personalizados si los necesitas
}
