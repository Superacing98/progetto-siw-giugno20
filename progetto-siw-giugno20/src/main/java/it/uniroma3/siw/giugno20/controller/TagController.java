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

import it.uniroma3.siw.giugno20.controller.validator.TagValidator;
import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.Tag;
import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.services.ProjectService;
import it.uniroma3.siw.giugno20.services.TagService;
import it.uniroma3.siw.giugno20.services.TaskService;

@Controller
public class TagController {

	@Autowired
	TagService tagService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TagValidator tagValidator;
		
	@RequestMapping(value = { "/projects/{projectId}/tag/add/form" }, method = RequestMethod.POST)
	public String showTagProjectForm(Model model, @PathVariable Long projectId) {
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		model.addAttribute("tagForm", new Tag());
		return "newTagProject";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/tag/add" }, method = RequestMethod.POST)
	public String addTagProject(@Valid @ModelAttribute("tagForm") Tag tag, 
								BindingResult tagBinding,
								Model model, 
								@PathVariable Long projectId) {
		
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("project", project);
		this.tagValidator.validate(tag, tagBinding);
		if(!tagBinding.hasErrors()) {
			this.tagService.addTagToProject(tag, project);
			return "redirect:/projects/" + projectId;
		}
		return "newTagProject";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/tag/{tagId}" }, method = RequestMethod.GET)
	public String task(Model model, @PathVariable Long projectId, @PathVariable Long tagId) {
		Project project = projectService.getProject(projectId);
		Tag tag = this.tagService.getTag(tagId);
		model.addAttribute("project", project);
		model.addAttribute("tag", tag);
		return "tag"; 
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/tag/add/form" }, method = RequestMethod.POST)
	public String showTagTaskForm(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		model.addAttribute("tagForm", new Tag());
		return "newTagTask";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/tag/add" }, method = RequestMethod.POST)
	public String addTagToTask(@Valid @ModelAttribute("tagForm") Tag tag, 
								BindingResult tagBinding,
								Model model, 
								@PathVariable Long projectId,
								@PathVariable Long taskId) {
		
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		this.tagValidator.validate(tag, tagBinding);
		if(!tagBinding.hasErrors()) {
			this.tagService.addTagToTask(tag, task);
			return "redirect:/projects/" + projectId + "/task/" + taskId;
		}
		return "newTagTask";
	}
}
