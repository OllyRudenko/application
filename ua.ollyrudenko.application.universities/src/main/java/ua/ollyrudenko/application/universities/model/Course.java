package ua.ollyrudenko.application.universities.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ollyrudenko.application.universities.University;

@Data
@NoArgsConstructor
@Entity
@Table(name = "courses")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course  implements Serializable {

	private static final long 	serialVersionUID = 1L;
	
	@Id
	@GeneratedValue	(strategy = GenerationType.AUTO)
	@Column
	private long 			course_id;
	
	@NotBlank(message="This field can not be empty") 
	@Size(min=2, max=50, message="The name must be between 2 and 50 symbols")
	private String 			name; 
	
	@NotBlank(message="The description can not be empty")
	@Size(max=200, message="The description must be between 2 and 200 symbols")
	private String 			courseDescription;
	
	@ManyToMany				(mappedBy = "courses") 
	@JsonIgnore
	private Set<Teacher> 	teachers = new HashSet<Teacher>();
	
	
	@ManyToMany 			(mappedBy = "courses", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Group> 		groups = new HashSet<Group>();
	
	@ManyToOne				(fetch = FetchType.EAGER)
	@JoinColumn				(name = "university_id")
	@JsonIgnore
	private University 		university;

	public Course(String name, University university) {
		super();
		this.name = name;
		this.university = university;
	}

	public Course(String name, long course_id, University university) {
		super();
		this.name = name;
		this.course_id = course_id;
		this.university = university;
	}

	public Course(String name, long course_id, String courseDescription, University university) {
		super();
		this.name = name;
		this.course_id = course_id;
		this.courseDescription = courseDescription;
		this.university = university;
	}

	public Course(String name, String courseDescription, University university) {
		super();
		this.name = name;
		this.courseDescription = courseDescription;
		this.university = university;
	}

	public Course(String name, long course_id, String courseDescription, Set<Group> groups, University university) {
		super();
		this.name = name;
		this.course_id = course_id;
		this.courseDescription = courseDescription;
		this.groups = groups;
		this.university = university;
	}

	public void addGroup(Group group) {
		groups.add(group);
		group.getCourses().add(this);
	}

	public void removeGroup(Group group) {
		groups.remove(group);
		group.getCourses().remove(this);
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
		Course other = (Course) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
