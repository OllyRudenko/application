package ua.ollyrudenko.application.universities.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
public class Person  implements Serializable {
	
	private static final long 	serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	@Getter @Setter
	private long 		id;

	@NotBlank			(message="This field can not be empty")
	@Size				(min=2, max=50, message="The First Name must be between 2 and 20 symbols")
	@Getter @Setter
	protected String 	firstName;

	@NotBlank			(message="This field can not be empty")
	@Size				(min=1, max=50, message="The last Name must be between 1 and 50 symbols")
	@Getter @Setter
	protected String 	lastName;
	

	public Person() {
	}

	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person(String firstName, String lastName, long id) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		String fullName = getFirstName() + getLastName();
		char[] seq = fullName.toCharArray();
		for (int i = 0; i < seq.length; i++) {
			result = prime * result + (int) seq[i];
		}
		return result;
	}
}
