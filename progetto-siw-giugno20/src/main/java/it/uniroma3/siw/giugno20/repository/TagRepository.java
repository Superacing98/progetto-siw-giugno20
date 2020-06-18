package it.uniroma3.siw.giugno20.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.Tag;
import it.uniroma3.siw.giugno20.model.Task;

public interface TagRepository extends CrudRepository<Tag, Long>{

	public List<Tag> findByTasks(Task task);
	
}
