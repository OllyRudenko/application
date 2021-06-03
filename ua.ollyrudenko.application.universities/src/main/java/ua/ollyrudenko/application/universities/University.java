package ua.ollyrudenko.application.universities;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.ollyrudenko.application.universities.model.Course;
import ua.ollyrudenko.application.universities.model.Group;
import ua.ollyrudenko.application.universities.model.Lesson;
import ua.ollyrudenko.application.universities.model.Student;
import ua.ollyrudenko.application.universities.model.Teacher;

@Data
@NoArgsConstructor
@Entity
@Table(name = "universities")
@Relation(value = "university", collectionRelation = "universities")
@JsonIgnoreProperties(ignoreUnknown = true)
public class University implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private long id;

	@NotBlank(message = "This field can not be empty")
	@Size(min = 2, max = 50, message = "The name of University must be between 2 and 50 symbols")
	private String name;

	@OneToMany(mappedBy = "university", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty("students")
	private Set<Student> students = new LinkedHashSet<Student>();

	@OneToMany(mappedBy = "university", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty("teachers")
	private Set<Teacher> teachers = new LinkedHashSet<Teacher>();;

	@OneToMany(mappedBy = "university", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty("groups")
	private Set<Group> groups = new LinkedHashSet<Group>();

	@OneToMany(mappedBy = "university", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty("courses")
	private Set<Course> courses = new LinkedHashSet<Course>();

	@OneToMany(mappedBy = "university", orphanRemoval = true, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JsonProperty("schedule")
	private Set<Lesson> schedule = new LinkedHashSet<Lesson>();

	public University(String name) {
		super();
		this.name = name;
	}

	public University(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public University(long id, String name, Set<Student> students, Set<Teacher> teachers, Set<Group> groups,
			Set<Course> courses, Set<Lesson> schedule) {
		super();
		this.id = id;
		this.name = name;
		this.students = students;
		this.teachers = teachers;
		this.groups = groups;
		this.courses = courses;
		this.schedule = schedule;
	}

	public void hireTeacher(Teacher teacher) {
		if (teacher == null) {
			throw new InvalidParameterException();
		} else {
			this.getTeachers().add(teacher);
		}
	}

	public void fireTeacher(Teacher teacher) {
		if (teacher == null) {
			throw new InvalidParameterException();
		} else if (this.getTeachers().contains(teacher)) {
			this.getTeachers().remove(teacher);
		}
	}

	public List<Teacher> filterTeachersByCourseId(long course_id) {
		Course course = this.findCourseById(course_id);
		if (course == null) {
			throw new InvalidParameterException();
		}
		return this.getTeachers().stream().filter((t) -> (t.getCourses().contains(course)))
				.collect(Collectors.toList());
	}

	public Teacher findTeacherById(long teacher_id) {
		Teacher teacher = this.getTeachers().stream().filter(t -> (t.getId() == teacher_id)).findFirst().orElse(null);
		if (teacher == null || !(teacher.getUniversity().equals(this))) {
			throw new InvalidParameterException("Teacher with this id not found");
		}
		return teacher;
	}

	public boolean isTeacherBelongs(Teacher teacher) {
		if (teacher == null) {
			throw new InvalidParameterException();
		} else if (teacher.getUniversity().equals(this)) {
			return true;
		}
		return false;
	}

	public void addCourseToTeacher(Teacher teacher, Course course) {
		if (!(teacher.getUniversity().equals(this)) && !(course.getUniversity()).equals(this)) {
			throw new InvalidParameterException();
		}
		this.findTeacherById(teacher.getId()).getCourses().add(course);
		this.findCourseById(course.getCourse_id()).getTeachers().add(teacher);
	}

	public void removeCourseFromTeacher(Teacher teacher, Course course) {
		if (!(teacher.getUniversity().equals(this)) && !(course.getUniversity()).equals(this)) {
			throw new InvalidParameterException();
		}
		this.findTeacherById(teacher.getId()).getCourses().remove(course);
		this.findCourseById(course.getCourse_id()).getTeachers().remove(teacher);
	}

	public void updateTeacher(Teacher teacher) {
		Teacher teacherForUpdate = this.findTeacherById(teacher.getId());
		if (teacherForUpdate != null) {
			teacherForUpdate.setFirstName(teacher.getFirstName());
			teacherForUpdate.setLastName(teacher.getLastName());
		}
	}

	// ----------STUDENTS

	public void enrollStudent(Student student) {
		if (student == null) {
			throw new InvalidParameterException();
		} else {
			this.getStudents().add(student);
		}
	}

	public void expelStudent(long student_id) {
		Student student = this.findStudentById(student_id);
		if (this.getStudents().contains(student)) { // maybe IF is excess?
			this.getStudents().remove(student);
		}
	}

	public Student findStudentById(long student_id) {
		Student student = this.getStudents().stream().filter(s -> (s.getId() == student_id)).findFirst().orElse(null);
		if (student == null || !(student.getUniversity().equals(this))) {
			throw new InvalidParameterException("Student with this id not found");
		}
		return student;
	}

	public boolean isStudentBelongs(Student student) {
		if (student == null) {
			throw new InvalidParameterException();
		} else if (student.getUniversity().equals(this)) {
			return true;
		}
		return false;
	}

	public void updateStudent(Student student) {
		Student studentForUpdate = this.findStudentById(student.getId());
		studentForUpdate.setFirstName(student.getFirstName());
		studentForUpdate.setLastName(student.getLastName());
		studentForUpdate.setGroup(student.getGroup());
		studentForUpdate.setEntry(student.getEntry());
	}

	public List<Student> filterStudentsByGroupId(long group_id) {
		Group group = this.findGroupById(group_id);
		return this.getStudents().stream().filter((s) -> (s.getGroup().equals(group))).collect(Collectors.toList());
	}

	// -----------------GROUP

	public void addGroup(Group group) {
		if (group == null) {
			throw new InvalidParameterException();
		} else {
			this.getGroups().add(group);
		}
	}

	public void removeGroup(Group group) {
		if (group == null || !(group.getUniversity().equals(this))) {
			throw new InvalidParameterException();
		} else if (this.getGroups().contains(group)) {
			this.getGroups().remove(group);
		}
	}

	public Group findGroupById(long group_id) {
		Group group = this.getGroups().stream().filter(g -> (g.getGroup_id() == group_id)).findFirst().orElse(null);
		if (group == null || !(group.getUniversity().equals(this))) {
			throw new InvalidParameterException("The group with this ID does not exist");
		}
		return group;
	}

	public boolean isGroupBelongs(Group group) {
		if (group == null) {
			throw new InvalidParameterException();
		} else if (group.getUniversity().equals(this)) {
			return true;
		}
		return false;
	}

	public void addStudentToGroup(Student student, Group group) {
		if (!(this.isStudentBelongs(student)) && !(this.isGroupBelongs(group))) {
			throw new InvalidParameterException();
		}
		this.findStudentById(student.getId()).setGroup(group);
		this.findGroupById(group.getGroup_id()).getStudents().add(student);
	}

	public void updateGroup(Group group) {
		Group groupForUpdate = this.findGroupById(group.getGroup_id());
		groupForUpdate.setName(group.getName());
	}

	public List<Group> filterGroupsByCourseId(long course_id) {
		Course course = this.findCourseById(course_id);
		return this.getGroups().stream().filter((g) -> (g.getCourses().contains(course))).collect(Collectors.toList());
	}

	// --------------COURSE

	public void createCourse(Course course) {
		if (course == null) {
			throw new InvalidParameterException();
		} else {
			this.getCourses().add(course);
		}
	}

	public void removeCourse(Course course) {
		if (course == null) {
			throw new InvalidParameterException();
		} else if (this.getCourses().contains(course)) {
			this.getCourses().remove(course);
		}
	}

	public Course findCourseById(long course_id) {
		Course course = this.getCourses().stream().filter(c -> (c.getCourse_id() == course_id)).findFirst()
				.orElse(null);
		if (course == null) {
			throw new InvalidParameterException();
		}
		return course;
	}

	public void updateCourse(Course course) {
		Course courseForUpdate = this.findCourseById(course.getCourse_id());
		courseForUpdate.setName(course.getName());
		courseForUpdate.setCourseDescription(course.getCourseDescription());
	}

	public boolean isCourseBelongs(Course course) {
		if (course == null) {
			throw new InvalidParameterException();
		} else if (course.getUniversity().equals(this)) {
			return true;
		}
		return false;
	}

	public void addCourseToGroup(Group group, Course course) {
		if (!(isGroupBelongs(group)) && !(isCourseBelongs(course))) {
			throw new InvalidParameterException();
		}
		this.findGroupById(group.getGroup_id()).getCourses().add(course);
		this.findCourseById(course.getCourse_id()).getGroups().add(group);
	}

	public void removeCourseFromGroup(Group group, Course course) {
		if (!(isGroupBelongs(group)) && !(isCourseBelongs(course))) {
			throw new InvalidParameterException();
		}
		this.findGroupById(group.getGroup_id()).getCourses().remove(course);
		this.findCourseById(course.getCourse_id()).getGroups().remove(group);
	}

	public List<Course> filterCoursesByGroupId(long group_id) {
		Group group = this.findGroupById(group_id);
		return this.getCourses().stream().filter((c) -> (c.getGroups().contains(group))).collect(Collectors.toList());
	}

	public List<Course> filterCoursesByTeacherId(long teacher_id) {
		Teacher teacher = this.findTeacherById(teacher_id);
		return this.getCourses().stream().filter((c) -> (c.getTeachers().contains(teacher)))
				.collect(Collectors.toList());
	}

	// -------------------LESSONS

	public void removeLesson(long lesson_id) {
		Lesson lesson = this.findLessonById(lesson_id);
		if (this.getSchedule().contains(lesson)) {
			this.getSchedule().remove(lesson);
		}
	}

	public void replanLesson(Lesson lesson) {
		if (lesson == null) {
			throw new InvalidParameterException();
		}
		checkTeacherForUpdate(lesson.getTeacher(), lesson.getDate(), lesson.getTime(), lesson.getLesson_id());

		checkGroupForUpdate(lesson.getGroup(), lesson.getDate(), lesson.getTime(), lesson.getLesson_id());

		Lesson lessonForUpdate = this.findLessonById(lesson.getLesson_id());
		lessonForUpdate.setClassroom(lesson.getClassroom());
		lessonForUpdate.setCourse(lesson.getCourse());
		lessonForUpdate.setDate(lesson.getDate());
		lessonForUpdate.setGroup(lesson.getGroup());
		lessonForUpdate.setTeacher(lesson.getTeacher());
		lessonForUpdate.setTime(lesson.getTime());
	}

	public Lesson findLessonById(long lesson_id) {
		Lesson lesson = this.getSchedule().stream().filter(l -> (l.getLesson_id() == lesson_id)).findFirst()
				.orElse(null);
		if (lesson == null) {
			throw new InvalidParameterException();
		}
		return lesson;
	}

	public boolean isLessonBelongs(Lesson lesson) {
		if (lesson == null) {
			throw new InvalidParameterException();
		} else if (lesson.getUniversity().equals(this)) {
			return true;
		}
		return false;
	}

	public Set<Lesson> filterScheduleByGroup(Group group, Date start, Date end) {
		Set<Lesson> groupSchedule = new LinkedHashSet<Lesson>();
		if (group == null) {
			throw new InvalidParameterException();
		}
		for (Lesson lesson : this.schedule) {
			if (lesson.getGroup().equals(group) && (lesson.getDate().compareTo(start) >= 0)
					&& (lesson.getDate().compareTo(end) <= 0)) {
				groupSchedule.add(lesson);
			}
		}
		return groupSchedule;
	}

	public Set<Lesson> filterScheduleByTeacher(Teacher teacher, Date start, Date end) {
		Set<Lesson> teacherSchedule = new LinkedHashSet<Lesson>();
		for (Lesson lesson : this.schedule) {
			if (lesson.getTeacher().equals(teacher) && (lesson.getDate().compareTo(start) >= 0)
					&& (lesson.getDate().compareTo(end) <= 0)) {
				teacherSchedule.add(lesson);
			}
		}
		return teacherSchedule;
	}

	// -----------NEW Lessons ---------//
	public void planLesson(Course course, Teacher teacher, int classroom, Group group, Date date, Time time,
			University university) throws InvalidParameterException {
		if (course == null || teacher == null || group == null || date == null || time == null) {
			throw new InvalidParameterException();
		}
		checkTeacher(teacher, date, time);
		checkGroup(group, date, time);
		this.schedule.add(new Lesson(course, teacher, classroom, group, time, date, university));
	}

	private void checkTeacher(Teacher teacher, Date date, Time time) {
		Lesson teacherLesson;
		teacherLesson = findLessonByTeacher(teacher, date, time);
		if (teacherLesson != null) {
			throw new InvalidParameterException();
		}
	}

	private void checkTeacherForUpdate(Teacher teacher, Date date, Time time, long lesson_id) {
		Lesson teacherLesson;
		teacherLesson = findLessonByTeacher(teacher, date, time);
		if (teacherLesson != null && !(teacherLesson.getLesson_id() == lesson_id)) {
			throw new InvalidParameterException();
		}
	}

	private void checkGroup(Group group, Date date, Time time) {
		Lesson groupLesson;
		groupLesson = findLessonByGroup(group, date, time);
		if (groupLesson != null) {
			throw new InvalidParameterException();
		}
	}

	private void checkGroupForUpdate(Group group, Date date, Time time, long lesson_id) {
		Lesson groupLesson;
		groupLesson = findLessonByGroup(group, date, time);
		if (groupLesson != null && !(groupLesson.getLesson_id() == lesson_id)) {
			throw new InvalidParameterException();
		}
	}

	public Lesson findLessonByTeacher(Teacher teacher, Date date, Time time) {
		for (Lesson lesson : this.schedule) {
			if (lesson.getTeacher().equals(teacher) && (lesson.getDate().compareTo(date) == 0)
					&& (lesson.getTime().compareTo(time) == 0)) {
				return lesson;
			}
		}
		return null;
	}

	public Lesson findLessonByGroup(Group group, Date date, Time time) {
		for (Lesson lesson : this.schedule) {
			if (lesson.getGroup().equals(group) && (lesson.getDate().compareTo(date) == 0)
					&& (lesson.getTime().compareTo(time) == 0)) {
				return lesson;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		University other = (University) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
