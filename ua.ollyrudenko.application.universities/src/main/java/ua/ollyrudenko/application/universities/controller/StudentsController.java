package ua.ollyrudenko.application.universities.controller;

import java.security.InvalidParameterException;
import java.sql.Date;

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

import ua.ollyrudenko.application.universities.dto.StudentDTO;
import ua.ollyrudenko.application.universities.exception.RequestException;
import ua.ollyrudenko.application.universities.model.Student;
import ua.ollyrudenko.application.universities.service.UniversityService;

@Controller
@RequestMapping({ "{id}/students" })
public class StudentsController {
	
	@Autowired
	UniversityService 		universityService;

	
	@GetMapping
	public ModelAndView studentsIndex(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/students_index");
		return modelAndView;
	}

	@GetMapping({ "/studentlist" })
	public ModelAndView findAll(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("students", universityService.findAllStudents(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/studentlist");
		return modelAndView;
	}

	@PostMapping
	public ModelAndView add(@PathVariable(value = "id") long id,
					  		@ModelAttribute ("studentdto") @Valid StudentDTO studentDTO,
					  		BindingResult bindingResult, 
					  		ModelAndView modelAndView) {
		modelAndView.setViewName("students/students_index");
		if (bindingResult.hasErrors()) {
			return modelAndView; 
		}
		Student newStudent = universityService.convertStudentDTOtoStudent(studentDTO, id);
		newStudent.setUniversity(universityService.findById(id));
		universityService.enrollStudent(newStudent, id);
		modelAndView.addObject("student", newStudent);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/student_add");
		return modelAndView;
	}

	@ModelAttribute("studentdto")
	public StudentDTO createModel() {
	    return new StudentDTO();
	}
	
	@GetMapping({ "/student_by_id" })
	public ModelAndView findById(	@PathVariable(value = "id") long id, 
									@RequestParam("id") long student_id, 
									ModelAndView modelAndView) {
		modelAndView.addObject("student", universityService.findStudentById(student_id, id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/student_by_id");
		return modelAndView;
	}

	@GetMapping({ "/expel" })
	public ModelAndView expell(@PathVariable(value = "id") long id, 
						 	   @RequestParam("id") long student_id, 
						 	   ModelAndView modelAndView) {
		universityService.expelStudent(student_id, id);
		modelAndView.addObject("students", universityService.findAllStudents(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/studentlist");
		return modelAndView;
	}

	@PostMapping({ "/update" })
	public ModelAndView update(	@PathVariable(value = "id") long id, 
						 		@RequestParam long student_id, 
						 		@RequestParam String firstName, 
						 		@RequestParam String lastName,
						 		@RequestParam long group_id, 
						 		@RequestParam String entry, 
						 		ModelAndView modelAndView) {
		Student student = new Student(student_id,
									  firstName, 
									  lastName, 
									  Date.valueOf(entry),
									  universityService.findGroupById(group_id, id));
		student.setUniversity(universityService.findById(id));
		universityService.updateStudent(student);
		modelAndView.addObject("student", student);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/student_update");
		return modelAndView;
	}

	@GetMapping({ "/studentlist_by_group" })
	public ModelAndView studentListByGroup( @PathVariable(value = "id") long id, 
									 		@RequestParam long group_id, 
									 		ModelAndView modelAndView) {
		Iterable<Student> students = universityService.filterStudentsByGroupId(group_id, id);
		modelAndView.addObject("students", students);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("students/studentlist_by_group");
		return modelAndView;
	}
	
	@ExceptionHandler({RequestException.class, InvalidParameterException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleError(RequestException e, InvalidParameterException i) {
        return "redirect:/error.html";
    }
}