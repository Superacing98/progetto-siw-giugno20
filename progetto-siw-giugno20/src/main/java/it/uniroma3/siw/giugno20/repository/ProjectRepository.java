package it.uniroma3.siw.giugno20.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.User;

public interface ProjectRepository extends CrudRepository<Project, Long>{

	public List<Project> findByOwner(User user);

	public void deleteByName(String name);
	
	public List<Project> findByMembers(User user);
	
}
