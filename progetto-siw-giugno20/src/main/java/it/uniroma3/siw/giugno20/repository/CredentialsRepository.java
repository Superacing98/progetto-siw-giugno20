package it.uniroma3.siw.giugno20.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.Credentials;

public interface CredentialsRepository extends CrudRepository<Credentials, Long>{

	public Optional<Credentials> findByUserName(String userName);

	public void deleteByUserName(String userName);
}
