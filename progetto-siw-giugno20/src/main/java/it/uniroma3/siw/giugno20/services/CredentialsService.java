package it.uniroma3.siw.giugno20.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.giugno20.model.Credentials;
import it.uniroma3.siw.giugno20.repository.CredentialsRepository;

@Service
public class CredentialsService {

	@Autowired
	protected CredentialsRepository credentialsRepository;
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	@Transactional
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials getCredentials(String userName) {
		Optional<Credentials> result = this.credentialsRepository.findByUserName(userName);
		return result.orElse(null);
	}
	
	@Transactional
	public Credentials saveCredentials(Credentials credentials) {
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
	@Transactional
	public List<Credentials> getAllCredentials() {
		Iterable<Credentials> i = this.credentialsRepository.findAll();
		List<Credentials> lista = new ArrayList<>();
		for(Credentials c : i) 
			lista.add(c);
		return lista;
	}
	
	@Transactional
	public void deleteCredentials(String userName) {
		this.credentialsRepository.deleteByUserName(userName);
	}
}
