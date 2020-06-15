package it.uniroma3.siw.giugno20.controller;


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
import it.uniroma3.siw.giugno20.services.ProjectService;
import it.uniroma3.siw.giugno20.services.TaskService;

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
	
	@RequestMapping(value = { "/projects/{projectId}/task/add" }, method = RequestMethod.GET)
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
		this.taskValidator.validate(task, taskBinding);
		if(!taskBinding.hasErrors()) {
			task.setProject(project);
			this.taskService.saveTask(task);
			return "redirect:/projects/" + project.getId();
		}
		return "newTask";
	}
}
