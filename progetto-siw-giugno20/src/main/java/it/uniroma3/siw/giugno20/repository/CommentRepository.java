package it.uniroma3.siw.giugno20.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.giugno20.model.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long>{

}
