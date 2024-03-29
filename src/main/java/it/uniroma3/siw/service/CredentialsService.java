package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredentialsRepository credentialsRepository;	

	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}

	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}

	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}

	/**
	 * Aggiunge al modello passato come parametro gli attribuiti "amministratoreLoggato"=true se l'utente corrente è un
	 * amministratore, "userLoggato"=true se invece è un utente qualsiasi.
	 * @param model
	 */
	public void adattaAdUtente(Model model) {
		try {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = this.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				model.addAttribute("amministratoreLoggato",true);
				model.addAttribute("userLoggato",false);
			}
			else if(credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				model.addAttribute("userLoggato",true);
				model.addAttribute("amministratoreLoggato",false);
			}
		}
		catch(Exception e)
		{
			model.addAttribute("userLoggato",false);
			model.addAttribute("amministratoreLoggato",false);
		}
	}
}
