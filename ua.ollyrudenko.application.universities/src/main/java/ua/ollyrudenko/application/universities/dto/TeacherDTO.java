package ua.ollyrudenko.application.universities.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TeacherDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 2, max = 50, message = "The First Name must be between 2 and 20 symbols")
	protected String firstName;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 1, max = 50, message = "The last Name must be between 1 and 50 symbols")
	protected String lastName;

	private long university_id;

	public TeacherDTO() {
	}

	public TeacherDTO(
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The First Name must be between 2 and 20 symbols") String firstName,
			@NotBlank(message = "This field can not be empty") @Size(min = 1, max = 50, message = "The last Name must be between 1 and 50 symbols") String lastName,
			long university_id) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.university_id = university_id;
	}

	public TeacherDTO(long id,
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The First Name must be between 2 and 20 symbols") String firstName,
			@NotBlank(message = "This field can not be empty") @Size(min = 1, max = 50, message = "The last Name must be between 1 and 50 symbols") String lastName,
			long university_id) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.university_id = university_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public long getUniversity_id() {
		return university_id;
	}

	public void setUniversity_id(long university_id) {
		this.university_id = university_id;
	}

}
