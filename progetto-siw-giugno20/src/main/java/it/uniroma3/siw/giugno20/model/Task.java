package it.uniroma3.siw.giugno20.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * Name for this task
	 */
	@Column(nullable = false, length = 100)
	private String name;
	
	/**
	 * Description for this task
	 */
	@Column(length = 1000)
	private String description;
	
	/**
	 * 
	 */
	@Column(nullable = false, updatable = false)
	private LocalDateTime creationDate;
	
	@ManyToOne
	private User userTask;
	
	@ManyToMany(mappedBy = "tasks", cascade = CascadeType.REMOVE)
	private List<Tag> tags;
	
	@OneToMany(cascade = CascadeType.ALL)
	List<Comment> comments;
	
	public Task() {
		this.tags = new ArrayList<>();
		this.comments = new ArrayList<>();
	}
	
	public Task(String name, String description) {
		this();
		this.name = name;
		this.description = description;
	}
	
	@PrePersist
	public void onPersist() {
		this.creationDate = LocalDateTime.now();
	}
	
	//GETTERS AND SETTERS

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public User getUserTask() {
		return userTask;
	}

	public void setUserTask(User userTask) {
		this.userTask = userTask;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	public void addTag(Tag tag) {
		tags.add(tag);
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment) {
		comments.add(comment);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
