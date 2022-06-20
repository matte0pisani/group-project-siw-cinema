package it.uniroma3.siw.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Film;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.FilmService;

@Controller
public class FilmController {
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private FilmService filmService;

	@GetMapping("/film/Form")	// non chiaro chi lo chiama
	public String getFilmForm(Model model) {
		model.addAttribute("film", new Film());
		return "filmForm.html";
	}

	@GetMapping("/film/all")
	public String getFilms(Model model) {
		List<Film> films = filmService.findAll();
		model.addAttribute("films", films);
		credentialsService.adattaAdUtente(model);
		return "films.html";
	}

	@GetMapping("/film/nome")
	public String getFilmByNome(@RequestParam(name = "nome") String nome, Model model) {
		if(!(nome == null || nome.isBlank())) {
			Film film = filmService.findByTitolo(nome);
			if(film != null) {
				model.addAttribute("film", film);
				return "film.html";
			}
			else { // il film non esiste
				model.addAttribute("isFilmInesistente", true);				
			}
		}
		credentialsService.adattaAdUtente(model);
		return "index.html";
	}
	
	@GetMapping("/film/{id}")
	public String getFilm(@PathVariable("id") Long id, Model model){
		Film film = filmService.findById(id);
		model.addAttribute("film", film);
		credentialsService.adattaAdUtente(model);
		return "film.html";	
	}

	@PostMapping("/film")
	public String addFilm(@Valid Film film, BindingResult bindingResult, Model model) {
		if(!bindingResult.hasErrors()) {
			filmService.addFilm(film);
			model.addAttribute("film", film);
			return "film.html";
		}
		else return "filmForm.html";
	}

	@GetMapping("/film/deleteForm/{id}")
	public String toDeleteFilm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("film", filmService.findById(id));
		return "toDeleteFilm.html";
	}

	@PostMapping("film/delete/{id}")
	public String deleteFilm(@PathVariable("id") Long id, Model model) {
		filmService.deleteById(id);
		model.addAttribute("films", filmService.findAll());
		return "films.html";
	}

}
