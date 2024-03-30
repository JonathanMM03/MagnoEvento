package org.utl.dsm.MagnoEvento.service;

import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.utl.dsm.MagnoEvento.entity.Conferencia;
import org.utl.dsm.MagnoEvento.entity.Conferencista;
import org.utl.dsm.MagnoEvento.repository.ConferencistaRepository;

import java.util.List;

@Service
public class ConferencistaService {

    @Autowired
    private ConferencistaRepository conferencistaRepository;

    public List<Conferencista> getAllConferencistas() {
        return conferencistaRepository.findAll();
    }

    public Conferencista getConferencistaById(Long id) {
        return conferencistaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conferencista not found with id: " + id));
    }

    public Conferencista createConferencista(Conferencista conferencista) {
        return conferencistaRepository.save(conferencista);
    }

    public Conferencista updateConferencista(Long id, Conferencista conferencistaDetails) {
        Conferencista conferencista = conferencistaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conferencista not found with id: " + id));

        conferencista.setGradoAcademico(conferencistaDetails.getGradoAcademico());
        conferencista.setLogros(conferencistaDetails.getLogros());
        // Actualizar otros atributos según sea necesario

        return conferencistaRepository.save(conferencista);
    }

    public void deleteConferencista(Long id) {
        conferencistaRepository.deleteById(id);
    }
}
