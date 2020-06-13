package it.uniroma3.siw.giugno20.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.giugno20.controller.session.SessionData;
import it.uniroma3.siw.giugno20.model.Credentials;
import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.services.CredentialsService;

@Controller
public class UserController {

	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
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
}
