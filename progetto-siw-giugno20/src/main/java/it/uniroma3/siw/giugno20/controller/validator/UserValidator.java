package it.uniroma3.siw.giugno20.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.giugno20.model.User;
import it.uniroma3.siw.giugno20.services.UserService;

@Component
public class UserValidator implements Validator{
	@Autowired
	UserService userService;
	
	final Integer MIN_NAME_LENGTH = 3;
	final Integer MAX_NAME_LENGTH = 20;

	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;
		String firstName = user.getFirstName().trim();
		String lastName = user.getLastName().trim();
		
		if(firstName.isBlank())
			errors.rejectValue("firstName", "required");
		else if(firstName.length() < MIN_NAME_LENGTH || firstName.length() > MAX_NAME_LENGTH) 
			errors.rejectValue("firstName", "size");
		
		if(lastName.isBlank())
			errors.rejectValue("lastName", "required");
		else if(lastName.length() < MIN_NAME_LENGTH || lastName.length() > MAX_NAME_LENGTH) 
			errors.rejectValue("lastName", "size");
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}
}
