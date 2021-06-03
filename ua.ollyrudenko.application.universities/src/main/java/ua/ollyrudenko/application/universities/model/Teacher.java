package ua.ollyrudenko.application.universities.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ollyrudenko.application.universities.University;

@Data
@NoArgsConstructor
@Entity
@Table(name = "teachers")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Teacher extends Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn			(name = "university_id")
	@JsonIgnore
	private University 	university;

	@ManyToMany
	@JoinTable			(name = "courses_teachers", 
						joinColumns = @JoinColumn(name = "teacher_id"), 
						inverseJoinColumns = @JoinColumn(name = "course_id"))
	@JsonIgnore
	private Set<Course> courses = new HashSet<Course>();

	public Teacher(String firstName, String lastName) {
		super(firstName, lastName);
	}

	public Teacher(String firstName, String lastName, University universyty) {
		super(firstName, lastName);
		this.university = universyty;
	}

	public Teacher(String firstName, String lastName, long id, University universyty) {
		super(firstName, lastName, id);
		this.university = universyty;
	}

	public Teacher(long id, String firstName, String lastName, University universyty, Set<Course> courses) {
		super(firstName, lastName, id);
		this.university = universyty;
		this.courses = courses;
	}

	public void addCourse(Course course) {
		this.courses.add(course);
		course.getTeachers().add(this);
	}

	public void removeCourse(Course course, Teacher teacher) {
		this.courses.remove(course);
		course.getTeachers().remove(teacher);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Teacher other = (Teacher) obj;
		if (courses == null) {
			if (other.courses != null)
				return false;
		} else if (!courses.equals(other.courses))
			return false;
		if (university == null) {
			if (other.university != null)
				return false;
		} else if (!university.equals(other.university))
			return false;
		return true;
	}
}
