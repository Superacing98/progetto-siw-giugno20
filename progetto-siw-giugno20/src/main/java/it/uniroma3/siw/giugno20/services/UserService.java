package it.uniroma3.siw.giugno20.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Transactional
	public User getUser(Long id) {
		Optional<User> result = this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> findAllUser() {
		Iterable<User> i = this.userRepository.findAll();
		List<User> lista = new ArrayList<>();
		for(User u : i) 
			lista.add(u);
		return lista;
	}
	
	@Transactional
	public void deleteUser(Long id) {
		this.userRepository.deleteById(id);
	}
}
