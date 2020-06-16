package it.uniroma3.siw.giugno20.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.giugno20.controller.session.SessionData;
import it.uniroma3.siw.giugno20.controller.validator.TaskValidator;
import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.services.ProjectService;
import it.uniroma3.siw.giugno20.services.TaskService;
import it.uniroma3.siw.giugno20.services.UserService;

@Controller
public class TaskController {

	@Autowired
	TaskService taskService;
	
	@Autowired
	TaskValidator taskValidator;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = { "/projects/{projectId}/task/add/form" }, method = RequestMethod.POST)
	public String showTaskForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		model.addAttribute("taskForm", new Task());
		return "newTask";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/add" }, method = RequestMethod.POST)
	public String addTask(@Valid @ModelAttribute("taskForm") Task task, 
								BindingResult taskBinding,
								Model model, 
								@PathVariable Long projectId) {
		
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		this.taskValidator.validate(task, taskBinding);
		if(!taskBinding.hasErrors()) {
			task.setProject(project);
			this.taskService.saveTask(task);
			return "redirect:/projects/" + project.getId();
		}
		return "newTask";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}" }, method = RequestMethod.GET)
	public String task(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		List<User> members = this.userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser)) 
			return "redirect:/projects/";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		return "task"; 
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/edit/form" }, method = RequestMethod.POST)
	public String showEditForm(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("taskForm", task);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		return "editTask";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/edit" }, method = RequestMethod.POST)
	public String editTask(@Valid @ModelAttribute("taskForm") Task task,
			BindingResult taskBindingResult,
			 @PathVariable Long projectId,
			 @PathVariable Long taskId,
			Model model) {
		
		Project project = this.projectService.getProject(projectId);
		Task updateTask = this.taskService.getTask(taskId);
		this.taskValidator.validate(task, taskBindingResult);
		if(!taskBindingResult.hasErrors()) {
			updateTask.setName(task.getName());
			updateTask.setDescription(task.getDescription());
			updateTask.setProject(project);
			this.taskService.saveTask(updateTask);
			return "redirect:/projects/" + project.getId();
		}
		return "editTask";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/delete" }, method = RequestMethod.POST)
	public String deleteTask(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		Project project = this.projectService.getProject(projectId);
		this.taskService.deleteTask(taskId);
		return "redirect:/projects/" + project.getId();
	}
}
