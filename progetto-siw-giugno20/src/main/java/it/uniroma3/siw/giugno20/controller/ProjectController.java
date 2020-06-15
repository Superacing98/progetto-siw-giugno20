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
import it.uniroma3.siw.giugno20.controller.validator.ProjectValidator;
import it.uniroma3.siw.giugno20.model.Project;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.services.CredentialsService;
import it.uniroma3.siw.giugno20.services.ProjectService;
import it.uniroma3.siw.giugno20.services.UserService;

@Controller
public class ProjectController {

	@Autowired
	ProjectService projectService;

	@Autowired
	SessionData sessionData;

	@Autowired
	ProjectValidator projectValidator;

	@Autowired
	CredentialsService credentialsService;

	@Autowired
	UserService userService;

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.GET)
	public String showProjectForm(Model model) {
		model.addAttribute("projectForm", new Project());
		return "newProject";
	}

	@RequestMapping(value = { "/projects/add" }, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project,
			BindingResult projectBindingResult,
			Model model) {

		User loggedUser = sessionData.getLoggedUser();

		this.projectValidator.validate(project, projectBindingResult);
		if(!projectBindingResult.hasErrors()) { 
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "newProject";
	}

	@RequestMapping(value = { "/projects" }, method = RequestMethod.GET)
	public String myOwnedProject(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = this.projectService.retrieveProjectOwnedBy(loggedUser);
		model.addAttribute("projectsList", projectsList);
		model.addAttribute("loggedUser", loggedUser);
		return "projects";
	}

	@RequestMapping(value = { "/projects/{projectId}" }, method = RequestMethod.GET)
	public String project(Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		if(project == null) 
			return "redirect:/projects";
		List<User> members = this.userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser)) 
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		return "project";
	}

	@RequestMapping(value = { "/sharedProjects" }, method = RequestMethod.GET)
	public String mySharedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> sharedProjects = this.projectService.retrieveVisibleProjects(loggedUser);
		model.addAttribute("sharedProjects", sharedProjects);
		return "sharedProjects";
	}

	@RequestMapping(value = { "/projects/{projectId}/users" }, method = RequestMethod.POST)
	public String showUsersList(Model model, @PathVariable Long projectId) {
		List<User> usersList = this.userService.findAllUser();
		Project project = this.projectService.getProject(projectId);
		model.addAttribute("usersList", usersList);
		model.addAttribute("project", project);
		return "shareWith";
	}

	@RequestMapping(value = { "/projects/{projectId}/shareSuccess/{userId}" }, method = RequestMethod.GET)
	public String shareProject(@PathVariable Long projectId,
								@PathVariable Long userId,
								Model model) {

		Project project = this.projectService.getProject(projectId);
		User user = this.userService.getUser(userId);
		if(user == null)
			return "shareWith";
		this.projectService.shareProjectWithUser(project, user);
		model.addAttribute("user", user);
		return "shareSuccess";
	}
	
	@RequestMapping(value = { "/projects/{projectId}/delete" }, method = RequestMethod.POST)
	public String deleteProject(Model model, @PathVariable Long projectId) {
		this.projectService.deleteProject(projectId);
		return "redirect:/projects";
	}
}
