package ua.ollyrudenko.application.universities.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class StudentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 2, max = 50, message = "The First Name must be between 2 and 20 symbols")
	protected String firstName;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 1, max = 50, message = "The last Name must be between 1 and 50 symbols")
	protected String lastName;

	@NotBlank(message = "This field can not be empty")
	private String entry;

	@Min(1)
	private long group_id;

	private long university_id;

	public StudentDTO() {
	};

	public StudentDTO(
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The First Name must be between 2 and 20 symbols") String firstName,
			@NotBlank(message = "This field can not be empty") @Size(min = 1, max = 50, message = "The last Name must be between 1 and 50 symbols") String lastName,
			@NotNull(message = "This field can not be empty") String entry,
			@Valid @NotNull(message = "please, set group") long group_id) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.entry = entry;
		this.group_id = group_id;
	}

	public StudentDTO(long id,
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The First Name must be between 2 and 20 symbols") String firstName,
			@NotBlank(message = "This field can not be empty") @Size(min = 1, max = 50, message = "The last Name must be between 1 and 50 symbols") String lastName,
			@NotNull(message = "This field can not be empty") String entry,
			@Valid @NotNull(message = "please, set group") long group_id, long university_id) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.entry = entry;
		this.group_id = group_id;
		this.university_id = university_id;
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

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public long getUniversity_id() {
		return university_id;
	}

	public void setUniversity_id(long university_id) {
		this.university_id = university_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entry == null) ? 0 : entry.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + (int) (group_id ^ (group_id >>> 32));
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
		StudentDTO other = (StudentDTO) obj;
		if (entry == null) {
			if (other.entry != null)
				return false;
		} else if (!entry.equals(other.entry))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (group_id != other.group_id)
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

}
