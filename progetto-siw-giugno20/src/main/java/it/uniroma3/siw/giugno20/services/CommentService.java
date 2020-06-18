package it.uniroma3.siw.giugno20.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.giugno20.model.Comment;
import it.uniroma3.siw.giugno20.model.Task;
import it.uniroma3.siw.giugno20.repository.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	CommentRepository commentRepository;
	
	@Transactional
	public Comment getComment(Long id) {
		Optional<Comment> result = this.commentRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public Comment saveComment(Comment comment) {
		return this.commentRepository.save(comment);
	}
	
	@Transactional
	public List<Comment> getAllComments() {
		Iterable<Comment> i = this.commentRepository.findAll();
		List<Comment> lista = new ArrayList<>();
		for(Comment c: i) 
			lista.add(c);
		return lista;
	}
	
	@Transactional
	public Comment addCommentToTask(Comment comment, Task task) {
		task.addComment(comment);
		return this.commentRepository.save(comment);
	}
}
