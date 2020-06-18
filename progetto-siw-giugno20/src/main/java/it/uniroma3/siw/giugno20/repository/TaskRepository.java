package it.uniroma3.siw.giugno20.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.model.User;

public interface TaskRepository extends CrudRepository<Task, Long>{

	public List<Task> findByUserTask(User userTask);
	
}
