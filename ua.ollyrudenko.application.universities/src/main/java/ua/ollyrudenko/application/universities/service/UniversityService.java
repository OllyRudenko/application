package ua.ollyrudenko.application.universities.service;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.ollyrudenko.application.universities.University;
import ua.ollyrudenko.application.universities.dto.CourseDTO;
import ua.ollyrudenko.application.universities.dto.GroupDTO;
import ua.ollyrudenko.application.universities.dto.LessonDTO;
import ua.ollyrudenko.application.universities.dto.StudentDTO;
import ua.ollyrudenko.application.universities.dto.TeacherDTO;
import ua.ollyrudenko.application.universities.dto.UniversityDTO;
import ua.ollyrudenko.application.universities.exception.ServiceException;
import ua.ollyrudenko.application.universities.model.Course;
import ua.ollyrudenko.application.universities.model.Group;
import ua.ollyrudenko.application.universities.model.Lesson;
import ua.ollyrudenko.application.universities.model.Student;
import ua.ollyrudenko.application.universities.model.Teacher;
import ua.ollyrudenko.application.universities.springDataJpa.UniversityRepository;


@Service
public class UniversityService {

	private final Logger 	logger = LogManager.getLogger(UniversityService.class);

	@Autowired
	UniversityRepository 		universityRepository;
	
	@Transactional
	public List<University> findAll() throws SQLException {
		try {
			return (List<University>) universityRepository.findAll();
		} catch (RuntimeException e) {
			logger.error("List of Universities not load.");
			throw new ServiceException("List of Universities not load.", e);
		}
	}

	@Transactional
	public University findById(long university_id) {
		try {
			University university = universityRepository.findById(university_id).get();
			return university;
		} catch (RuntimeException e) {
			logger.error("University by ID={} not found", university_id);
			throw new ServiceException("University by ID not found", e);
		}
	}

	@Transactional
	public University insert(University university) {
		try {
			return universityRepository.save(university);
		} catch (RuntimeException e) {
			logger.error("University {} didn't insert to table UNIVERSITIES", university.getName());
			throw new ServiceException("University didn't insert to table UNIVERSITIES", e);
		}
	}

	@Transactional
	public void delete(University university) {
		try {
			universityRepository.delete(university);
		} catch (RuntimeException e) {
			logger.error("University {} didn't delete from table UNIVERSITIES", university.getName());
			throw new ServiceException("University didn't delete from table UNIVERSITIES", e);
		} 
	}
	
	public UniversityDTO convertUniversityToUniversityDTO(University university) {
		UniversityDTO universityDTO = new UniversityDTO();
		universityDTO.setId(university.getId());
		universityDTO.setCoursesDTO(convertCoursesToCoursesDTO(this.findAllCourses(university.getId()), university.getId()));
		universityDTO.setGroupsDTO(convertGroupsToGroupsDTO(this.findAllGroups(university.getId()), university.getId()));
		universityDTO.setName(university.getName());
		universityDTO.setSchedule(convertLessonsToLessonsDTO(this.findAllLessons(university.getId()), university.getId()));
		universityDTO.setStudentsDTO(convertStudentsToStudentsDTO(this.findAllStudents(university.getId()), university.getId()));
		universityDTO.setTeachersDTO(convertTeachersToTeachersDTO(this.findAllTeachers(university.getId()), university.getId()));
		return universityDTO;
	}
	
	public List<UniversityDTO> convertUniversitiesToUniversitiesDTO(List<University> universities) {
		List<UniversityDTO> universitiesDTO = new ArrayList<UniversityDTO>();
		for(University u : universities) {
			universitiesDTO.add(convertUniversityToUniversityDTO(u));
		}
		return universitiesDTO;
	}

	/// TEACHERS

	@Transactional
	public List<Teacher> findAllTeachers(long university_id) {
		try {
			return (this.findById(university_id).getTeachers()).stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			logger.error("List of TEACHERS not found");
			throw new ServiceException("List of TEACHERS not found", e);
		}
	}

	@Transactional
	public void hireTeacher(Teacher teacher, long university_id) {
		University university = this.findById(university_id);
		university.hireTeacher(teacher);
		universityRepository.save(university);
		logger.info("HIRE TEACHER");
	}

	@Transactional
	public void fireTeacher(long teacher_id, long university_id) {
		University university = this.findById(university_id);
		university.fireTeacher(university.findTeacherById(teacher_id));
		universityRepository.save(university);
		logger.info("FIRE TEACHER");
	}

	@Transactional
	public List<Teacher> filterTeachersByCourseId(long course_id, long university_id) {
		return this.findById(university_id).filterTeachersByCourseId(course_id);
	}

	@Transactional
	public Teacher findTeacherById(long teacher_id, long university_id) {
		return this.findById(university_id).findTeacherById(teacher_id);
	}

	@Transactional
	public void updateTeacher(Teacher teacher) {
		University university = this.findById(teacher.getUniversity().getId());
		university.updateTeacher(teacher);
		universityRepository.save(university);
		logger.info("UPDATE TEACHER");
	}

	@Transactional
	public void addCourseToTeacher(Teacher teacher, Course course, long university_id) {
		University university = this.findById(university_id);
		university.addCourseToTeacher(teacher, course);
		universityRepository.save(university);
		logger.info("COURSE WAS ADD TO TEACHER");
	}

	@Transactional
	public void removeCourseFromTeacher(Teacher teacher, Course course, long university_id) {
		University university = this.findById(university_id);
		university.removeCourseFromTeacher(teacher, course);
		universityRepository.save(university);
		logger.info("COURSE WAS REMOVED FROM TEACHER");
	}
	
	public Teacher convertTeacherDTOtoTeacher(TeacherDTO teacherDTO, long university_id) {
		Teacher teacher = new Teacher();
		if(teacherDTO.getId() > 0) {
			teacher.setId(teacherDTO.getId());
		}
		teacher.setFirstName(teacherDTO.getFirstName()); 
		teacher.setLastName(teacherDTO.getLastName()); 
		teacher.setUniversity(this.findById(university_id));
		return teacher;
	}
	
	public TeacherDTO convertTeacherToTeacherDTO(Teacher teacher, long university_id) {
		return new TeacherDTO(	teacher.getId(),
								teacher.getFirstName(), 
								teacher.getLastName(), 
								university_id);
	}
	
	public List<TeacherDTO> convertTeachersToTeachersDTO(List<Teacher> teachers, long university_id){
		List<TeacherDTO> teacherDTO = new ArrayList<TeacherDTO>(); 
		for(Teacher t : teachers) {
			teacherDTO.add(convertTeacherToTeacherDTO(t, university_id));
		}
		return teacherDTO;
	}

	/////// STUDENTS

	@Transactional
	public List<Student> findAllStudents(long university_id) {
		try {
			return (this.findById(university_id).getStudents()).stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			logger.error("List of TEACHERS not found");
			throw new ServiceException("List of STUDENTS not found", e);
		}
	}

	@Transactional
	public void enrollStudent(Student student, long university_id) {
		University university = this.findById(university_id);
		university.enrollStudent(student);
		universityRepository.save(university);
		logger.info("ENROLL STUDENT");
	}
	
	@Transactional
	public void expelStudent(long student_id, long university_id) {
		University university = this.findById(university_id);
		university.expelStudent(student_id); 
		universityRepository.save(university);
		logger.info("EXPEL STUDENT");
	}

	@Transactional
	public Student findStudentById(long student_id, long university_id) {
		return this.findById(university_id).findStudentById(student_id);
	}

	@Transactional
	public void updateStudent(Student student) {
		University university = this.findById(student.getUniversity().getId()); // if null??
		university.updateStudent(student);
		universityRepository.save(university);
		logger.info("UPDATE STUDENT");
	}

	@Transactional
	public List<Student> filterStudentsByGroupId(long group_id, long university_id) {
		return this.findById(university_id).filterStudentsByGroupId(group_id);
	}
	
	public Student convertStudentDTOtoStudent(StudentDTO studentDTO, long university_id) {
		Student student = new Student();
		if(studentDTO.getId() > 0) {
			student.setId(studentDTO.getId());
		}
		student.setFirstName(studentDTO.getFirstName()); 
		student.setLastName(studentDTO.getLastName()); 
		student.setEntry(Date.valueOf(studentDTO.getEntry()));
		student.setGroup(this.findGroupById(studentDTO.getGroup_id(), university_id));
		student.setUniversity(this.findById(university_id));
		return student;
	}
	
	public StudentDTO convertStudentToStudentDTO(Student student, long university_id) {
		return new StudentDTO(	student.getId(),
								student.getFirstName(), 
								student.getLastName(), 
								student.getEntry().toString(),
								student.getGroup().getGroup_id(),
								university_id);
	}
	
	public List<StudentDTO> convertStudentsToStudentsDTO(List<Student> students, long university_id){
		List<StudentDTO> studentsDTO = new ArrayList<StudentDTO>(); 
		for(Student s : students) {
			studentsDTO.add(convertStudentToStudentDTO(s, university_id));
		}
		return studentsDTO;
	}

	///////// GROUPS

	@Transactional
	public List<Group> findAllGroups(long university_id) {
		try {
			return (this.findById(university_id).getGroups()).stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			logger.error("List of GROUPS not found");
			throw new ServiceException("List of GROUPS not found", e);
		}
	}

	@Transactional
	public void addGroup(Group group, long university_id) {
		University university = this.findById(university_id);
		university.addGroup(group);
		universityRepository.save(university);
		logger.info("ADD GROUP");
	}

	@Transactional
	public void removeGroup(long group_id, long university_id) {
		University university =  this.findById(university_id);
		university.removeGroup(university.findGroupById(group_id));
		universityRepository.save(university);
		logger.info("REMOVE GROUP");
	}

	@Transactional
	public void addStudentToGroup(Student student, Group group, long university_id) {
		University university = this.findById(university_id);
		university.addStudentToGroup(student, group); 
		universityRepository.save(university);
		logger.info("STUDENT WAS ADD TO GROUP");
	}
	
	@Transactional
	public Group findGroupById(long group_id, long university_id) {
		return this.findById(university_id).findGroupById(group_id);
	}

	@Transactional
	public void updateGroup(Group group) {
		University university = this.findById(group.getUniversity().getId());
		university.updateGroup(group);
		universityRepository.save(university);
		logger.info("UPDATE GROUP");
	}

	@Transactional
	public List<Group> filterGroupsByCourseId(long course_id, long university_id) {
		return this.findById(university_id).filterGroupsByCourseId(course_id); 
	}
	
	public Group convertGroupDTOtoGroup(GroupDTO groupDTO, long university_id) {
		Group group = new Group();
		if(groupDTO.getGroup_id() > 0) {
			group.setGroup_id(groupDTO.getGroup_id());
		}
		group.setName(groupDTO.getName()); 
		group.setUniversity(this.findById(university_id));
		return group;
	}
	
	public GroupDTO convertGroupToGroupDTO(Group group, long university_id) {
		return new GroupDTO(	group.getGroup_id(),
								group.getName(), 
								university_id);
	}
	
	public List<GroupDTO> convertGroupsToGroupsDTO(List<Group> groups, long university_id){
		List<GroupDTO> groupsDTO = new ArrayList<GroupDTO>(); 
		for(Group g : groups) {
			groupsDTO.add(convertGroupToGroupDTO(g, university_id));
		}
		return groupsDTO;
	}

	//////// COURSES

	@Transactional
	public List<Course> findAllCourses(long university_id) {
		try {
			return (this.findById(university_id).getCourses()).stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			logger.error("List of COURSES not found");
			throw new ServiceException("List of COURSES not found", e);
		}
	}

	@Transactional
	public void createCourse(Course course, long university_id) {
		University university = this.findById(university_id);
		university.createCourse(course);
		universityRepository.save(university);
		logger.info("CREATE COURSE");
	}

	@Transactional
	public void removeCourse(long course_id, long university_id) {
		University university = this.findById(university_id);
		university.removeCourse(university.findCourseById(course_id));
		universityRepository.save(university);
		logger.info("REMOVE COURSE");
	}

	@Transactional
	public void addCourseToGroup(Group group, Course course, long university_id) {
		University university = this.findById(university_id);
		university.addCourseToGroup(group, course);
		universityRepository.save(university);
		logger.info("COURSE WAS ADD TO GROUP");
	}

	@Transactional
	public void removeCourseFromGroup(Group group, Course course, long university_id) {
		University university = this.findById(university_id);
		university.removeCourseFromGroup(group, course);
		universityRepository.save(university);
		logger.info("COURSE WAS REMOVED FROM GROUP");
	}

	@Transactional
	public Course findCourseById(long course_id, long university_id) {
		return this.findById(university_id).findCourseById(course_id);
	}

	@Transactional
	public void updateCourse(Course course) {		
		University university = this.findById(course.getUniversity().getId());
		university.updateCourse(course);
		universityRepository.save(university);
		logger.info("UPDATE COURSE");
	}

	@Transactional
	public List<Course> filterCoursesByGroupId(long group_id, long university_id) {
		return this.findById(university_id).filterCoursesByGroupId(group_id);
	}

	@Transactional
	public List<Course> filterCoursesByTeacherId(long teacher_id, long university_id) {
		return this.findById(university_id).filterCoursesByTeacherId(teacher_id); 
	}
	
	public Course convertCourseDTOtoCourse(CourseDTO courseDTO, long university_id) {
		Course course = new Course();
		if(courseDTO.getCourse_id() > 0) {
			course.setCourse_id(courseDTO.getCourse_id());
		}
		course.setName(courseDTO.getName()); 
		course.setCourseDescription(courseDTO.getCourseDescription());
		course.setUniversity(this.findById(university_id));
		return course;
	}
	
	public CourseDTO convertCourseToCourseDTO(Course course, long university_id) {
		return new CourseDTO(	course.getCourse_id(),
								course.getName(), 
								course.getCourseDescription(),
								university_id);
	}
	
	public List<CourseDTO> convertCoursesToCoursesDTO(List<Course> courses, long university_id){
		List<CourseDTO> coursesDTO = new ArrayList<CourseDTO>(); 
		for(Course c : courses) {
			coursesDTO.add(convertCourseToCourseDTO(c, university_id));
		}
		return coursesDTO;
	}

	///////// LESSONS

	@Transactional
	public List<Lesson> findAllLessons(long university_id) {
		try {
			return (this.findById(university_id).getSchedule()).stream().collect(Collectors.toList());
		} catch (RuntimeException e) {
			logger.error("List of LESSONS not found");
			throw new ServiceException("List of LESSONS not found", e);
		}
	}

	@Transactional
	public void planLesson(Course course, Teacher teacher, int classroom, Group group, Date date, Time time,
			long university_id) throws InvalidParameterException {
		University university = this.findById(university_id);
		university.planLesson(course, teacher, classroom, group, date, time, university);
		universityRepository.save(university);
		logger.info("LESSON CREATED");
	}
	
	public Lesson convertLessontDTOtoLesson(LessonDTO lessonDTO, long university_id) {
		University university = this.findById(university_id);
		Lesson lesson = new Lesson(	university.findCourseById(lessonDTO.getCourse_id()), 
									university.findTeacherById(lessonDTO.getTeacher_id()), 
									lessonDTO.getClassroom(),
									university.findGroupById(lessonDTO.getGroup_id()),
									Time.valueOf(lessonDTO.getTime()),
									Date.valueOf(lessonDTO.getDate()),
									university);
		if(lessonDTO.getLesson_id() > 0) {
			lesson.setLesson_id(lessonDTO.getLesson_id());
		}
		return lesson;
	}
	
	public LessonDTO convertLessonToLessonDTO(Lesson lesson, long university_id) {
		return new LessonDTO(	lesson.getLesson_id(),
								lesson.getCourse().getCourse_id(),
								lesson.getTeacher().getId(), 
								lesson.getClassroom(),
								lesson.getGroup().getGroup_id(),
								lesson.getTime().toString(),
								lesson.getDate().toString(),
								university_id);
	}
	
	public List<LessonDTO> convertLessonsToLessonsDTO(List<Lesson> lessons, long university_id){
		List<LessonDTO> lessonsDTO = new ArrayList<LessonDTO>(); 
		for(Lesson l : lessons) {
			lessonsDTO.add(convertLessonToLessonDTO(l, university_id));
		}
		return lessonsDTO;
	}
	
	@Transactional
	public void replanLesson(Lesson lesson, long university_id) {
		University university = this.findById(university_id);
		university.replanLesson(lesson);
		universityRepository.save(university);
		logger.info("REMOVE LESSON");
	}

	@Transactional
	public void removeLesson(long lesson_id, long university_id) {
		University university = this.findById(university_id);
		university.removeLesson(lesson_id);
		universityRepository.save(university);
		logger.info("REMOVE LESSON");
	}

	@Transactional
	public Lesson findLessonById(long lesson_id, long university_id) {
		return this.findById(university_id).findLessonById(lesson_id);
	}

	@Transactional
	public List<Lesson> filterScheduleByGroup(Group group, Date start, Date end, long university_id) {
		University university = this.findById(university_id);
		return (university.filterScheduleByGroup(group, start, end)).stream().collect(Collectors.toList());
	}

	@Transactional
	public List<Lesson> filterScheduleByTeacher(Teacher teacher, Date start, Date end, long university_id) {
		University university = this.findById(university_id);
		return (university.filterScheduleByTeacher(teacher, start, end)).stream().collect(Collectors.toList());
	}

}
