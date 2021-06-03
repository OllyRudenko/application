package ua.ollyrudenko.application.universities.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.hateoas.RepresentationModel;

public class LessonDTO extends RepresentationModel<LessonDTO> implements Serializable {

	private static final long serialVersionUID = 1L;

	private long lesson_id;

	@Min(1)
	private long course_id;

	@Min(1)
	private long teacher_id;

	@Min(1)
	private int classroom;

	@Min(1)
	private long group_id;

	@NotBlank(message = "This field can not be empty")
	private String time;

	@NotBlank(message = "This field can not be empty")
	private String date;

	private long university_id;

	public LessonDTO() {
	}

	public LessonDTO(@Min(1) long course_id, @Min(1) long teacher_id, @Min(1) int classroom, @Min(1) long group_id,
			@NotBlank(message = "This field can not be empty") String time,
			@NotBlank(message = "This field can not be empty") String date) {
		super();
		this.course_id = course_id;
		this.teacher_id = teacher_id;
		this.classroom = classroom;
		this.group_id = group_id;
		this.time = time;
		this.date = date;
	}

	public LessonDTO(@Min(1) long course_id, @Min(1) long teacher_id, @Min(1) int classroom, @Min(1) long group_id,
			@NotBlank(message = "This field can not be empty") String time,
			@NotBlank(message = "This field can not be empty") String date, long university_id) {
		super();
		this.course_id = course_id;
		this.teacher_id = teacher_id;
		this.classroom = classroom;
		this.group_id = group_id;
		this.time = time;
		this.date = date;
		this.university_id = university_id;
	}

	public LessonDTO(long lesson_id, @Min(1) long course_id, @Min(1) long teacher_id, @Min(1) int classroom,
			@Min(1) long group_id, @NotBlank(message = "This field can not be empty") String time,
			@NotBlank(message = "This field can not be empty") String date, long university_id) {
		super();
		this.lesson_id = lesson_id;
		this.course_id = course_id;
		this.teacher_id = teacher_id;
		this.classroom = classroom;
		this.group_id = group_id;
		this.time = time;
		this.date = date;
		this.university_id = university_id;
	}

	public long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(long course_id) {
		this.course_id = course_id;
	}

	public long getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(long teacher_id) {
		this.teacher_id = teacher_id;
	}

	public int getClassroom() {
		return classroom;
	}

	public void setClassroom(int classroom) {
		this.classroom = classroom;
	}

	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getLesson_id() {
		return lesson_id;
	}

	public void setLesson_id(long lesson_id) {
		this.lesson_id = lesson_id;
	}

	public long getUniversity_id() {
		return university_id;
	}

	public void setUniversity_id(long university_id) {
		this.university_id = university_id;
	}

}
