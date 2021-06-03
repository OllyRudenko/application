package ua.ollyrudenko.application.universities.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ollyrudenko.application.universities.University;

@Data
@NoArgsConstructor
@Entity
@Table(name = "lessons")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lesson implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long 		lesson_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "course_id")
	private Course 		course;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher 	teacher;

	private int 		classroom;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	private Group 		group;

	@Basic
	@JsonFormat(pattern = "HH:mm:ss")
	private Time 		time;

	@Basic
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date 		date;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "university_id")
	@JsonIgnore
	private University	university;

	public Lesson(long lesson_id, Course course, Teacher teacher, int classroom, Group group, Time time, Date date,
			University university) {
		super();
		this.lesson_id = lesson_id;
		this.course = course;
		this.teacher = teacher;
		this.classroom = classroom;
		this.group = group;
		this.time = time;
		this.date = date;
		this.university = university;
	}

	public Lesson(Course course, Teacher teacher, int classroom, Group group, Time time, Date date,
			University university) {
		super();
		this.course = course;
		this.teacher = teacher;
		this.classroom = classroom;
		this.group = group;
		this.time = time;
		this.date = date;
		this.university = university;
	}

	public Lesson(Course course, Teacher teacher, int classroom, Group group, Time time, Date date) {
		super();
		this.course = course;
		this.teacher = teacher;
		this.classroom = classroom;
		this.group = group;
		this.time = time;
		this.date = date;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + classroom;
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Lesson other = (Lesson) obj;
		if (classroom != other.classroom)
			return false;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (teacher == null) {
			if (other.teacher != null)
				return false;
		} else if (!teacher.equals(other.teacher))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		return true;
	}

}
