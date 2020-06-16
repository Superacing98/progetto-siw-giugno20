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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	/**
	 * First name for this user
	 */
	@Column(nullable = false, length = 100)
	private String firstName;
	
	/**
	 * Last name for this user
	 */
	@Column(nullable = false, length = 100)
	private String lastName;
	
	/**
	 * 
	 */
	@Column(nullable = false, updatable = false)
	private LocalDateTime creationDate;
	
	/**
	 * 
	 */
	@ManyToMany(mappedBy = "members")
	private List<Project> visibleProjects;
	
	/**
	 * 
	 */
	@OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
	private List<Project> ownedProjects;
	
	/**
	 * 
	 */
	@OneToMany
	@JoinColumn(name = "user_tasks_id")
	private List<Task> tasks;
	
	public User() {
		this.visibleProjects = new ArrayList<>();
		this.ownedProjects = new ArrayList<>();
	}
	
	public User(String firstName, String lastName) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public List<Project> getVisibleProjects() {
		return visibleProjects;
	}

	public void setVisibleProjects(List<Project> visibleProjects) {
		this.visibleProjects = visibleProjects;
	}

	public List<Project> getOwnedProjects() {
		return ownedProjects;
	}

	public void setOwnedProjects(List<Project> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	
	public void addVisibleProject(Project project) {
		visibleProjects.add(project);
	}
	
	public void addOwnedProject(Project project) {
		ownedProjects.add(project);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
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
		User other = (User) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
}
