package ua.ollyrudenko.application.universities.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UniversityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 2, max = 50, message = "The name of University must be between 2 and 50 symbols")
	private String name;

	private List<StudentDTO> studentsDTO = new ArrayList<StudentDTO>();

	private List<TeacherDTO> teachersDTO = new ArrayList<TeacherDTO>();;

	private List<GroupDTO> groupsDTO = new ArrayList<GroupDTO>();

	private List<CourseDTO> coursesDTO = new ArrayList<CourseDTO>();

	private List<LessonDTO> schedule = new ArrayList<LessonDTO>();

	public UniversityDTO() {
	}

	public UniversityDTO(long id,
			@NotBlank(message = "This field can not be empty") @Size(min = 2, max = 50, message = "The name of University must be between 2 and 50 symbols") String name,
			List<StudentDTO> studentsDTO, List<TeacherDTO> teachersDTO, List<GroupDTO> groupsDTO,
			List<CourseDTO> coursesDTO, List<LessonDTO> schedule) {
		super();
		this.id = id;
		this.name = name;
		this.studentsDTO = studentsDTO;
		this.teachersDTO = teachersDTO;
		this.groupsDTO = groupsDTO;
		this.coursesDTO = coursesDTO;
		this.schedule = schedule;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<StudentDTO> getStudentsDTO() {
		return studentsDTO;
	}

	public void setStudentsDTO(List<StudentDTO> studentsDTO) {
		this.studentsDTO = studentsDTO;
	}

	public List<TeacherDTO> getTeachersDTO() {
		return teachersDTO;
	}

	public void setTeachersDTO(List<TeacherDTO> teachersDTO) {
		this.teachersDTO = teachersDTO;
	}

	public List<GroupDTO> getGroupsDTO() {
		return groupsDTO;
	}

	public void setGroupsDTO(List<GroupDTO> groupsDTO) {
		this.groupsDTO = groupsDTO;
	}

	public List<CourseDTO> getCoursesDTO() {
		return coursesDTO;
	}

	public void setCoursesDTO(List<CourseDTO> coursesDTO) {
		this.coursesDTO = coursesDTO;
	}

	public List<LessonDTO> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<LessonDTO> schedule) {
		this.schedule = schedule;
	}

}
