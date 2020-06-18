package it.uniroma3.siw.giugno20.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.giugno20.model.Comment;

@Component
public class CommentValidator implements Validator{

	final Integer MAX_COMMENT_LENGTH = 1000;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Comment.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Comment comment = (Comment) o;
		String testo = comment.getTesto();
		
		if(testo.isBlank())
			errors.rejectValue("testo", "required");
		else if(testo.length() > MAX_COMMENT_LENGTH)
			errors.rejectValue("testo", "size");
	}
}
