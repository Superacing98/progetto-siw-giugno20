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
import it.uniroma3.siw.giugno20.controller.validator.CredentialsValidator;
import it.uniroma3.siw.giugno20.controller.validator.UserValidator;
import it.uniroma3.siw.giugno20.model.Credentials;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.services.CredentialsService;
import it.uniroma3.siw.giugno20.services.ProjectService;

@Controller
public class UserController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	CredentialsValidator credentialsValidator;
	
	@Autowired
	UserValidator userValidator;
	
	@RequestMapping(value = { "/home" }, method = RequestMethod.GET)
	public String home(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		Credentials credentials = sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		return "home";
	}
	
	@RequestMapping(value = { "/me" }, method = RequestMethod.GET)
	public String me(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		Credentials credentials = sessionData.getLoggedCredentials();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("credentials", credentials);
		return "userProfile";
	}
	
	@RequestMapping(value = { "/me/edit/form" }, method = RequestMethod.POST)
	public String showUserEditForm(Model model) {
		User loggedUser = this.sessionData.getLoggedUser();
		Credentials updateCredentials = this.sessionData.getLoggedCredentials();
		model.addAttribute("userForm", loggedUser);
		model.addAttribute("credentialsForm", updateCredentials);
		model.addAttribute("user", loggedUser);
		model.addAttribute("credentials", updateCredentials);
		return "editUser";
	}
	
	@RequestMapping(value = { "/me/edit" }, method = RequestMethod.POST)
	public String editProfile(@Valid @ModelAttribute("userForm") User user,
								BindingResult userBinding,
								@Valid @ModelAttribute("credentialsForm") Credentials credentials,
								BindingResult credentialsBinding,
								Model model) {
		
		User updateUser = sessionData.getLoggedUser();
		Credentials updateCredentials = sessionData.getLoggedCredentials();
		this.userValidator.validate(user, userBinding);
		this.credentialsValidator.validate(credentials, credentialsBinding);
		if(!userBinding.hasErrors() && !credentialsBinding.hasErrors()) {
			updateUser.setFirstName(user.getFirstName());
			updateUser.setLastName(user.getLastName());
			updateCredentials.setUserName(credentials.getUserName());
			updateCredentials.setPassword(credentials.getPassword());
			updateCredentials.setUser(updateUser);
			this.credentialsService.saveCredentials(updateCredentials);
			return "redirect:/me";
		}
		return "editUser";
	}
	
	@RequestMapping(value = { "/admin" }, method = RequestMethod.GET)
	public String admin(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("user", loggedUser);
		return "admin";
	}
	
	@RequestMapping(value = { "/admin/users" }, method = RequestMethod.GET)
	public String usersList(Model model) {
		Credentials credentials = sessionData.getLoggedCredentials();
		List<Credentials> allCredentials = this.credentialsService.getAllCredentials();
		allCredentials.remove(credentials);		
		model.addAttribute("credentialsList", allCredentials);
		return "allUsers";
	}
	
	@RequestMapping(value = { "/admin/users/{username}/delete" }, method = RequestMethod.POST)
	public String removeUser(Model model, @PathVariable String username) {
		this.credentialsService.deleteCredentials(username);
		return "redirect:/admin/users";
	}
}
