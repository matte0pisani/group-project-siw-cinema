package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Spettacolo;
import it.uniroma3.siw.repository.PrenotazioneRepository;
import it.uniroma3.siw.repository.SpettacoloRepository;

@Service
public class SpettacoloService {
	@Autowired
	private SpettacoloRepository repo;
	@Autowired
	private PrenotazioneRepository prenRepo;

	public List<Spettacolo> findAllSpettacoliPerFilm(Long idFilm) {
		return repo.findByFilmId(idFilm);
	}

	public void aggiornaPostiDisponibili(Spettacolo spettacolo) {
		spettacolo.setNumeroPosti(spettacolo.getNumeroPosti()-1);
		repo.save(spettacolo);
	}

	public List<Spettacolo> findAllSpettacoli() {
		return (List<Spettacolo>) repo.findAll();
	}

	public Spettacolo findById(Long spettacoloId) {
		return repo.findById(spettacoloId).orElse(null);
	}
	
	@Transactional
	public void libera(Long prenotazione) {
		Prenotazione p = prenRepo.findById(prenotazione).orElse(null);
		Spettacolo s = p.getSpettacolo();
		s.setNumeroPosti(s.getNumeroPosti()+1);
		repo.save(s);
	}
	
	@Transactional
	public void prenota(Long spettacolo) {
		Spettacolo s = repo.findById(spettacolo).orElse(null);
		s.setNumeroPosti(s.getNumeroPosti()-1);
		repo.save(s);
	}

	@Transactional
	public void addSpettacolo(Spettacolo spettacolo) {
		repo.save(spettacolo);
	}
	
	@Transactional
	public void deleteById(Long id) {
		repo.deleteById(id);
	}
}
