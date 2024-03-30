package org.utl.dsm.MagnoEvento.service;

import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utl.dsm.MagnoEvento.entity.Asistente;
import org.utl.dsm.MagnoEvento.repository.AsistenteRepository;

import java.util.List;

@Service
public class AsistenteService {

    @Autowired
    private AsistenteRepository asistenteRepository;

    public List<Asistente> getAllAsistentes() {
        return asistenteRepository.findAll();
    }

    public Asistente getAsistenteById(Long id) {
        return asistenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Asistente not found with id: " + id));
    }

    public Asistente createAsistente(Asistente asistente) {
        return asistenteRepository.save(asistente);
    }

    public Asistente updateAsistente(Long id, Asistente asistenteDetails) {
        Asistente asistente = asistenteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Asistente not found with id: " + id));

        asistente.setMatricula(asistenteDetails.getMatricula());
        asistente.setCorreo(asistenteDetails.getCorreo());
        asistente.setGradoAcademico(asistenteDetails.getGradoAcademico());
        asistente.setFoto(asistenteDetails.getFoto());
        // Actualizar otros atributos según sea necesario

        return asistenteRepository.save(asistente);
    }

    public void deleteAsistente(Long id) {
        asistenteRepository.deleteById(id);
    }
}
