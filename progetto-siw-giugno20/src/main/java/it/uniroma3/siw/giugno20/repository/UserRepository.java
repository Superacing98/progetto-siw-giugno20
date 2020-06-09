package it.uniroma3.siw.giugno20.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

}
