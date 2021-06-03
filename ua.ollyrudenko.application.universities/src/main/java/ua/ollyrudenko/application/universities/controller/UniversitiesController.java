package ua.ollyrudenko.application.universities.controller;

import java.security.InvalidParameterException;
import java.sql.SQLException;

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

import ua.ollyrudenko.application.universities.University;
import ua.ollyrudenko.application.universities.exception.RequestException;
import ua.ollyrudenko.application.universities.service.UniversityService;



@Controller
@RequestMapping("/")
public class UniversitiesController {

	@Autowired
	UniversityService universityService;

	@GetMapping
	public ModelAndView start(ModelAndView modelAndView) throws SQLException {
		Iterable<University> universities = universityService.findAll();
		modelAndView.setViewName("index");
		modelAndView.addObject("universities", universities);
		return modelAndView;
	}

	@GetMapping("/{id}")
	public ModelAndView universityPage(@PathVariable("id") long id, ModelAndView modelAndView) {
		modelAndView.setViewName("university/university_index");
		modelAndView.addObject("university", universityService.findById(id));
		modelAndView.addObject("id", id);
		return modelAndView;
	}

	@PostMapping
	public ModelAndView add(@RequestParam String name, @ModelAttribute @Valid University newUniversity,
			BindingResult bindingResult, ModelAndView modelAndView) throws SQLException {
		modelAndView.setViewName("index");
		if (bindingResult.hasErrors()) {
			return modelAndView;
		}
		universityService.insert(newUniversity);
		modelAndView.addObject("universities", universityService.findAll());
		return modelAndView;
	}

	@ModelAttribute("university")
	public University createModel() {
		return new University();
	}

	@GetMapping({ "/delete" })
	public ModelAndView delete(@RequestParam("id") long id, ModelAndView modelAndView) throws SQLException {
		University university = universityService.findById(id);
		universityService.delete(university);
		modelAndView.addObject("universities", universityService.findAll());
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@PostMapping({ "/update" })
	public ModelAndView update(@RequestParam("id") long id, @RequestParam("name") String name,
			ModelAndView modelAndView) throws SQLException {
		University university = universityService.findById(id);
		university.setName(name);
		universityService.insert(university);
		modelAndView.addObject("universities", universityService.findAll());
		modelAndView.setViewName("index");
		return modelAndView;
	}

	@ExceptionHandler({ RequestException.class, InvalidParameterException.class })
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String handleError(RequestException e, InvalidParameterException i) {
		return "redirect:/error.html";
	}
}
