package it.uniroma3.siw.giugno20.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	protected TaskRepository taskRepository;
	
	@Transactional
	public Task getTask(Long id) {
		Optional<Task> result = this.taskRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Task saveTask(Task task) {
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public void deleteTask(Long id) {
		this.taskRepository.deleteById(id);
	}
	
	@Transactional
	public Task addTaskToUser(Task task, User user) {
		user.addTask(task);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public Task addTaskToProject(Task task, Project project) {
		project.addTask(task);
		return this.taskRepository.save(task);
	}
	
	@Transactional
	public List<Task> retrieveVisibleTasks(User user) {
		return this.taskRepository.findByUserTask(user);
	}
}
