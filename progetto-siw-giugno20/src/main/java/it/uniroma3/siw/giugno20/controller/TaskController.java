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
import it.uniroma3.siw.giugno20.controller.validator.CommentValidator;
import it.uniroma3.siw.giugno20.controller.validator.TaskValidator;
import it.uniroma3.siw.giugno20.model.Comment;
import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.Tag;
import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.services.CommentService;
import it.uniroma3.siw.giugno20.services.ProjectService;
import it.uniroma3.siw.giugno20.services.TagService;
import it.uniroma3.siw.giugno20.services.TaskService;
import it.uniroma3.siw.giugno20.services.UserService;

@Controller
public class TaskController {

	@Autowired
	TaskService taskService;
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TaskValidator taskValidator;
	
	@Autowired
	CommentValidator commentValidator;
	
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
			this.taskService.addTaskToProject(task, project);
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
		List<Tag> tags = this.tagService.retrieveTag(task);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser)) 
			return "redirect:/projects/";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("members", members);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		model.addAttribute("tags", tags);
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
		model.addAttribute("project", project);
		model.addAttribute("task", updateTask);
		this.taskValidator.validate(task, taskBindingResult);
		if(!taskBindingResult.hasErrors()) {
			updateTask.setName(task.getName());
			updateTask.setDescription(task.getDescription());
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
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/assign" }, method = RequestMethod.POST)
	public String showMembersList(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		List<User> members = this.userService.getMembers(project);
		User loggedUser = sessionData.getLoggedUser();
		members.remove(loggedUser);
		model.addAttribute("members", members);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		return "assignTask";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/assignSuccess/{userId}" }, method = RequestMethod.GET)
	public String assignTask(@PathVariable Long projectId,
								@PathVariable Long taskId,
								@PathVariable Long userId,
								Model model) {

		Project project = this.projectService.getProject(projectId);
		User user = this.userService.getUser(userId);
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("user", user);
		if(user.getTasks().contains(task))
			return "assignTaskError";
		this.taskService.addTaskToUser(task, user);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		return "assignSuccess";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/comment/add/form" }, method = RequestMethod.POST)
	public String showCommentForm(Model model, @PathVariable Long projectId, @PathVariable Long taskId) {
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("task", task);
		model.addAttribute("project", project);
		model.addAttribute("commentForm", new Comment());
		return "newComment";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/task/{taskId}/comment/add" }, method = RequestMethod.POST)
	public String addComment(@Valid @ModelAttribute("commentForm") Comment comment, 
								BindingResult commentBinding,
								Model model, 
								@PathVariable Long projectId,
								@PathVariable Long taskId) {
		
		Project project = this.projectService.getProject(projectId);
		Task task = this.taskService.getTask(taskId);
		model.addAttribute("project", project);
		model.addAttribute("task", task);
		this.commentValidator.validate(comment, commentBinding);
		if(!commentBinding.hasErrors()) {
			this.commentService.addCommentToTask(comment, task);
			return "redirect:/projects/" + projectId + "/task/" + taskId;
		}
		return "newComment";
	}
}
