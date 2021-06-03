package ua.ollyrudenko.application.universities.controller;

import java.security.InvalidParameterException;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import ua.ollyrudenko.application.universities.exception.RequestException;
import ua.ollyrudenko.application.universities.model.Course;
import ua.ollyrudenko.application.universities.model.Teacher;
import ua.ollyrudenko.application.universities.service.UniversityService;

@Controller
@RequestMapping({ "{id}/teachers" })
public class TeachersController {
	
	@Autowired
	UniversityService 		universityService;
	

	@GetMapping
	public ModelAndView teachersIndex(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teachers_index");
		return modelAndView;
	}

	@GetMapping({ "/teacherlist" })
	public ModelAndView findAll(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		Iterable<Teacher> teachers = (universityService.findById(id)).getTeachers();
		modelAndView.addObject("teachers", teachers);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacherlist");
		return modelAndView;
	}

	@PostMapping
	public ModelAndView add(@PathVariable(value = "id") long id, 
					  		@ModelAttribute @Valid Teacher newTeacher,
					  		BindingResult bindingResult, 
					  		ModelAndView modelAndView) {
		modelAndView.setViewName("teachers/teachers_index");
		if (bindingResult.hasErrors()) {
			return modelAndView;
		}
		newTeacher.setUniversity(universityService.findById(id));
		universityService.hireTeacher(newTeacher, id);
		modelAndView.addObject("teacher", newTeacher);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacher_add");
		return modelAndView;
	}
	
	@ModelAttribute("teacher")
	public Teacher createModel() {
	    return new Teacher();
	}

	@GetMapping({ "/teacher_by_id" })
	public ModelAndView findById(@PathVariable(value = "id") long id, 
								 @RequestParam("teacher_id") long teacher_id, 
								 ModelAndView modelAndView) {
		Teacher teacher = universityService.findTeacherById(teacher_id, id);
		modelAndView.addObject("teacher", teacher);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacher_by_id");
		return modelAndView;
	}

	@GetMapping({ "/fire" })
	public ModelAndView fire(@PathVariable(value = "id") long id, 
							 @RequestParam("teacher_id") long teacher_id, 
							 ModelAndView modelAndView) {
		universityService.fireTeacher(teacher_id, id);
		modelAndView.addObject("teachers", universityService.findAllTeachers(id));
		modelAndView.addObject("id", id); 
		modelAndView.setViewName("teachers/teacherlist");
		return modelAndView;
	}

	@PostMapping({ "/update" })
	public ModelAndView update( @PathVariable(value = "id") long id, 
						 		@RequestParam long teacher_id, 
						 		@RequestParam String firstName, 
						 		@RequestParam String lastName,
						 		ModelAndView modelAndView) {
		Teacher teacher = new Teacher(firstName, lastName, teacher_id, universityService.findById(id));
		universityService.updateTeacher(teacher);
		modelAndView.addObject("teacher", teacher);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacher_update");
		return modelAndView;
	}
	
	@PostMapping({ "/courses" })
	public ModelAndView addCourseToTeacher(	@PathVariable(value = "id") long id, 
									 		@RequestParam long teacher_id,
									 		@RequestParam long course_id,
									 		ModelAndView modelAndView){
		Teacher teacher = universityService.findTeacherById(teacher_id, id);
		Course course = universityService.findCourseById(course_id, id);
		universityService.addCourseToTeacher(teacher, course, id);
		Set<Course> courses = universityService.findTeacherById(teacher_id, id).getCourses();
		modelAndView.addObject("courses", courses);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacher_courses");
		return modelAndView;
	}
	
	@PostMapping({ "/courselist" })
	public ModelAndView removeCourseFromTeacher(@PathVariable(value = "id") long id, 
												@RequestParam long teacher_id,
												@RequestParam long course_id,
												ModelAndView modelAndView){
		Teacher teacher = universityService.findTeacherById(teacher_id, id);
		Course course = universityService.findCourseById(course_id, id);
		universityService.removeCourseFromTeacher(teacher, course, id);
		Set<Course> courses = universityService.findTeacherById(teacher_id, id).getCourses();
		modelAndView.addObject("courses", courses);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacher_courses");
		return modelAndView;
	}

	@GetMapping({ "/teacherlist_by_course" })
	public ModelAndView filterTeachersByCourseId( @PathVariable(value = "id") long id, 
												  @RequestParam long course_id, 
												  ModelAndView modelAndView) {
		Iterable<Teacher> teachers = universityService.filterTeachersByCourseId(course_id, id); 
		modelAndView.addObject("teachers", teachers);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("teachers/teacherlist_by_course");
		return modelAndView;
	}
	
	@ExceptionHandler({RequestException.class, InvalidParameterException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleError(RequestException e, InvalidParameterException i) {
        return "redirect:/error.html";
    }
}