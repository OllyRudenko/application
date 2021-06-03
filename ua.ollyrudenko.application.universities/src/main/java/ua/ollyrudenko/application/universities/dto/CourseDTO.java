package ua.ollyrudenko.application.universities.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CourseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long course_id;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 2, max = 50, message = "The name must be between 2 and 50 symbols")
	private String name;

	@NotBlank(message = "The description can not be empty")
	@Size(max = 200, message = "The description must be between 2 and 200 symbols")
	private String courseDescription;

	private long university_id;

	public CourseDTO() {
		super();
	}

	public CourseDTO(
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The name must be between 2 and 50 symbols") String name,
			@NotBlank(message = "The description can not be empty") @Size(max = 200, message = "The description must be between 2 and 200 symbols") String courseDescription,
			long university_id) {
		super();
		this.name = name;
		this.courseDescription = courseDescription;
		this.university_id = university_id;
	}

	public CourseDTO(long course_id,
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The name must be between 2 and 50 symbols") String name,
			@NotBlank(message = "The description can not be empty") @Size(max = 200, message = "The description must be between 2 and 200 symbols") String courseDescription,
			long university_id) {
		super();
		this.course_id = course_id;
		this.name = name;
		this.courseDescription = courseDescription;
		this.university_id = university_id;
	}

	public long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(long course_id) {
		this.course_id = course_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourseDescription() {
		return courseDescription;
	}

	public void setCourseDescription(String courseDescription) {
		this.courseDescription = courseDescription;
	}

	public long getUniversity_id() {
		return university_id;
	}

	public void setUniversity_id(long university_id) {
		this.university_id = university_id;
	}

}
