package it.uniroma3.siw.giugno20.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.giugno20.model.Task;

@Component
public class TaskValidator implements Validator{

	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Task task = (Task) o;
		String name = task.getName();
		String description = task.getDescription();
		
		if(name.isBlank())
			errors.rejectValue("name", "required");
		else if(name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH)
			errors.rejectValue("name", "size");
		else if(description.length() > MAX_DESCRIPTION_LENGTH)
			errors.rejectValue("description", "size");
	}
}
