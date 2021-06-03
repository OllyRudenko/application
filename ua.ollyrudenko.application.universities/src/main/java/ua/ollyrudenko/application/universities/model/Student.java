package ua.ollyrudenko.application.universities.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ollyrudenko.application.universities.University;

@Data
@NoArgsConstructor
@Entity
@Table(name = "students")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Student extends Person implements Serializable {

	private static final long 	serialVersionUID = 1L;

	@NotNull			(message="This field can not be empty")
	@Basic
	@JsonFormat			(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date 		entry;

	@Valid
	@NotNull			(message="please, set group")
	@ManyToOne			(fetch = FetchType.EAGER) 
	private Group 		group;

	@ManyToOne
	@JoinColumn			(name = "university_id")
	@JsonIgnore
	private University 	university;

	public Student(String firstName, String lastName, Date entry, Group group) {
		super(firstName, lastName);
		this.entry = entry;
		this.group = group;
	}
	
	public Student(String firstName, String lastName, Date entry, Group group, University university) {
		super(firstName, lastName);
		this.entry = entry;
		this.group = group;
		this.university = university;
	}

	public Student(long id, String firstName, String lastName, Date entry, Group group) {
		super(firstName, lastName, id);
		this.entry = entry;
		this.group = group;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((entry == null) ? 0 : entry.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((university == null) ? 0 : university.hashCode());
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
		Student other = (Student) obj;
		if (entry == null) {
			if (other.entry != null)
				return false;
		} else if (!entry.equals(other.entry))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (university == null) {
			if (other.university != null)
				return false;
		} else if (!university.equals(other.university))
			return false;
		return true;
	}

}
