package ua.ollyrudenko.application.universities.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ollyrudenko.application.universities.University;

@Data
@NoArgsConstructor
@Entity
@Table(name = "groups")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group  implements Serializable {

	private static final long 	serialVersionUID = 1L;
	
	@Id
	@GeneratedValue			(strategy = GenerationType.AUTO)
	@Column
	private long 			group_id; 

	@NotBlank(message="This field can't be empty")
	@Size(min=5, max=5)
	@Pattern(regexp = "^([\\p{Upper}{2}]*-[\\d{2}]*)+$", message="Name contains two Upper letters, hyphen and two digits (example AA-00)")
	private String 			name;
	
	@ManyToOne				
	@JoinColumn				(name = "university_id")
	@JsonIgnore
	private University 		university;

	@OneToMany	
	@JsonIgnore
	private List<Student> 	students = new ArrayList<Student>();

	@ManyToMany						
	@JoinTable				(name = "courses_groups", 
							joinColumns = @JoinColumn(name = "group_id"), 
							inverseJoinColumns = @JoinColumn(name = "course_id"))
	@JsonIgnore
	private Set<Course> 	courses = new HashSet<Course>();

	public Group(String name, University university) {
		super();
		this.name = name;
		this.university = university;
	}

	public Group(String name, long group_id, University university) {
		super();
		this.name = name;
		this.group_id = group_id;
		this.university = university;
	}

	public Group(String name, long group_id, List<Student> students, University university) {
		super();
		this.name = name;
		this.group_id = group_id;
		this.students = students;
		this.university = university;
	}

	public void addCourse(Course course) {
		courses.add(course);
		course.getGroups().add(this);
	}

	public void removeCourse(Course course) {
		courses.remove(course);
		course.getGroups().remove(this);
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
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		} else {
			Group gr = (Group) obj;
			if (!(name.equals(gr.name))) {
				return false;
			}
		}
		return true;
	}
}
