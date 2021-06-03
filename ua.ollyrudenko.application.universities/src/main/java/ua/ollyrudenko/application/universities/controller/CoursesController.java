package ua.ollyrudenko.application.universities.controller;

import java.security.InvalidParameterException;

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
import ua.ollyrudenko.application.universities.service.UniversityService;



@Controller
@RequestMapping({ "{id}/courses" })
public class CoursesController {
	
	@Autowired
	UniversityService universityService; 

	
	@GetMapping
	public ModelAndView coursesIndex(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/courses_index");
		return modelAndView; 
	}

	@GetMapping ({ "/courselist" })
	public ModelAndView findAll(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("courses", universityService.findAllCourses(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/courselist");
		return modelAndView;
	}

	@PostMapping
	public ModelAndView add(@PathVariable(value = "id") long id, 
					  		@ModelAttribute @Valid Course newCourse,
					  		BindingResult bindingResult, 
					  		ModelAndView modelAndView) {
		modelAndView.setViewName("courses/courses_index");
		if (bindingResult.hasErrors()) {
			return modelAndView;
		}
		newCourse.setUniversity(universityService.findById(id));
		universityService.createCourse(newCourse, id);
		modelAndView.addObject("course", newCourse);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/course_add");
		return modelAndView;
	}
	
	@ModelAttribute("course")
	public Course createModel() { 
	    return new Course();
	}

	@GetMapping({ "/course_by_id" })
	public ModelAndView findById(@PathVariable(value = "id") long id, 
						   		 @RequestParam("id") long course_id, 
						   		 ModelAndView modelAndView) {
		modelAndView.addObject("course", universityService.findCourseById(course_id, id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/course_by_id");
		return modelAndView;
	}

	@GetMapping({ "/delete" })
	public ModelAndView delete(@PathVariable(value = "id") long id, 
						 	   @RequestParam("id") long course_id, 
						 	   ModelAndView modelAndView) {
		universityService.removeCourse(course_id, id);
		modelAndView.addObject("courses", universityService.findAllCourses(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/courselist");
		return modelAndView;
	}

	@PostMapping({ "/update" })
	public ModelAndView update(@PathVariable(value = "id") long id, 
						 	   @RequestParam long course_id, 
						 	   @RequestParam String name, 
						 	   @RequestParam String courseDescription,
						 	   ModelAndView modelAndView) {
		Course course = new Course(name, course_id, courseDescription, universityService.findById(id));
		universityService.updateCourse(course);
		modelAndView.addObject("course", course);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/course_update");
		return modelAndView;
	}

	@GetMapping({ "/courselist_by_group" })
	public ModelAndView filterCoursesByGroupId( @PathVariable(value = "id") long id, 
										 		@RequestParam long group_id, 
										 		ModelAndView modelAndView) {
		modelAndView.addObject("courses", universityService.filterCoursesByGroupId(group_id, id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("courses/courselist_by_group");
		return modelAndView;  
	}

	@GetMapping({ "/courselist_by_teacher" })
	public ModelAndView filterCoursesByTeacherId(@PathVariable(value = "id") long id, 
												 @RequestParam long teacher_id, 
												 ModelAndView modelAndView) {
		modelAndView.addObject("courses", universityService.filterCoursesByTeacherId(teacher_id, id));
		modelAndView.addObject("id", id); 
		modelAndView.setViewName("courses/courselist_by_teacher");
		return modelAndView;
	}
	
	@ExceptionHandler({RequestException.class, InvalidParameterException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleError(RequestException e, InvalidParameterException i) {
        return "redirect:/error.html";
    }
}