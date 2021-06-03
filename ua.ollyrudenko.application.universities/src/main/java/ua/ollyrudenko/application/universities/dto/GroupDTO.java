package ua.ollyrudenko.application.universities.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long group_id;

	@NotBlank(message = "This field can't be empty")
	@Size(min = 5, max = 5)
	@Pattern(regexp = "^([\\p{Upper}{2}]*-[\\d{2}]*)+$", message = "Name contains two Upper letters, hyphen and two digits (example AA-00)")
	private String name;

	private long university_id;

	public GroupDTO() {
		super();
	}

	public GroupDTO(
			@NotBlank(message = "This field can't be empty") @Size(min = 5, max = 5) @Pattern(regexp = "^([\\p{Upper}{2}]*-[\\d{2}]*)+$", message = "Name contains two Upper letters, hyphen and two digits (example AA-00)") String name,
			long university_id) {
		super();
		this.name = name;
		this.university_id = university_id;
	}

	public GroupDTO(long group_id,
			@NotBlank(message = "This field can't be empty") @Size(min = 5, max = 5) @Pattern(regexp = "^([\\p{Upper}{2}]*-[\\d{2}]*)+$", message = "Name contains two Upper letters, hyphen and two digits (example AA-00)") String name,
			long university_id) {
		super();
		this.group_id = group_id;
		this.name = name;
		this.university_id = university_id;
	}

	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUniversity_id() {
		return university_id;
	}

	public void setUniversity_id(long university_id) {
		this.university_id = university_id;
	}

}
