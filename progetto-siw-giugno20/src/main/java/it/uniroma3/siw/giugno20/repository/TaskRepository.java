package it.uniroma3.siw.giugno20.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long>{
 
}
