package ua.ollyrudenko.application.universities.init;

import java.sql.Date;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import ua.ollyrudenko.application.universities.University;
import ua.ollyrudenko.application.universities.model.Course;
import ua.ollyrudenko.application.universities.model.Group;
import ua.ollyrudenko.application.universities.model.Student;
import ua.ollyrudenko.application.universities.model.Teacher;
import ua.ollyrudenko.application.universities.springDataJpa.UniversityRepository;

@Component
public class UniversityDataBaseInit implements ApplicationRunner {

	UniversityRepository universityRepository;

	@Autowired
	public UniversityDataBaseInit(UniversityRepository universityRepository) {
		this.universityRepository = universityRepository;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		University university = new University();
		university.setName("High University");
		universityRepository.save(university);

		Group group = new Group("AA-01", university);

		Course courseEnglish = new Course("en", "English", university);
		Course courseMath = new Course("math", "Mathematic", university);

		Teacher teacherEn = new Teacher("Ivan", "Mazepa", university);
		teacherEn.getCourses().add(courseEnglish);
		Teacher teacherMath = new Teacher("Nestor", "Mahno", university);
		teacherMath.getCourses().add(courseMath);

		university.addGroup(group);
		university.enrollStudent(new Student("Sergey", "Taran", Date.valueOf("2021-03-01"), group, university));
		university.enrollStudent(new Student("Maxim", "Perepelitsa", Date.valueOf("2021-03-01"), group, university));
		university.enrollStudent(new Student("Karina", "Submarina", Date.valueOf("2021-03-01"), group, university));
		university.enrollStudent(new Student("Tatyiana", "Monatik", Date.valueOf("2021-03-01"), group, university));

		university.createCourse(courseMath);
		university.createCourse(courseEnglish);

		university.hireTeacher(teacherMath);
		university.hireTeacher(teacherEn);

		university.planLesson(courseMath, teacherMath, 1, group, Date.valueOf("2021-03-03"), Time.valueOf("09:00:00"),
				university);
		university.planLesson(courseMath, teacherEn, 1, group, Date.valueOf("2021-03-03"), Time.valueOf("11:00:00"),
				university);
		universityRepository.save(university);

	}

}
