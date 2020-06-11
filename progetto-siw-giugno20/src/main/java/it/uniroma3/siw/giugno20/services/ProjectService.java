package it.uniroma3.siw.giugno20.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	protected ProjectRepository projectRepository;
	
	@Transactional
	public Project getProject(Long id) {
		Optional<Project> result = this.projectRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Project saveProject(Project project) {
		return this.projectRepository.save(project);
	}
	
	@Transactional
	public Project shareProjectWithUser(Project project, User user) {
		project.addMember(user);
		return this.projectRepository.save(project);
	}
	
	@Transactional
	public List<Project> retrieveProjectOwnedBy(User user) {
		return this.projectRepository.findByOwner(user);
	}
	
	@Transactional
	public void deleteProject(Long id) {
		this.projectRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteProject(String name) {
		this.projectRepository.deleteByName(name);
	}
	
	@Transactional
	public List<Project> getAllProjects() {
		Iterable<Project> i = this.projectRepository.findAll();
		List<Project> lista = new ArrayList<>();
		for(Project p : i) 
			lista.add(p);
		return lista;
	}
}
