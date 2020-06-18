package it.uniroma3.siw.giugno20.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.Tag;
import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.repository.TagRepository;

@Service
public class TagService {
	
	@Autowired
	TagRepository tagRepository;
	
	@Transactional
	public Tag getTag(Long id) {
		Optional<Tag> result = this.tagRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Tag saveTag(Tag tag) {
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public List<Tag> getAllTags() {
		Iterable<Tag> i = this.tagRepository.findAll();
		List<Tag> lista = new ArrayList<>();
		for(Tag t : i) 
			lista.add(t);
		return lista;
	}
	
	@Transactional
	public Tag addTagToProject(Tag tag, Project project) {
		project.addTag(tag);
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public Tag addTagToTask(Tag tag, Task task) {
		tag.addTask(task);
		return this.tagRepository.save(tag);
	}
	
	@Transactional
	public List<Tag> retrieveTag(Task task) {
		return this.tagRepository.findByTasks(task);
	}
}
