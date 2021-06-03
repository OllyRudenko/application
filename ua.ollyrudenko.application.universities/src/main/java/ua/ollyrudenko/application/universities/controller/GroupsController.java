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
import ua.ollyrudenko.application.universities.model.Group;
import ua.ollyrudenko.application.universities.service.UniversityService;

@Controller
@RequestMapping({ "{id}/groups" })
public class GroupsController {
	
		
	@Autowired
	UniversityService universityService;

	@GetMapping
	public ModelAndView groupsIndex(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/groups_index");
		return modelAndView;
	}

	@GetMapping({ "/grouplist" })
	public ModelAndView findAll(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("groups", universityService.findAllGroups(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/grouplist");
		return modelAndView;
	}

	@PostMapping
	public ModelAndView add(@PathVariable(value = "id") long id, 
					  		@ModelAttribute @Valid Group newGroup,
					  		BindingResult bindingResult, 
					  		ModelAndView modelAndView) {
		modelAndView.setViewName("groups/groups_index");
		if (bindingResult.hasErrors()) {
			return modelAndView;
		}
		newGroup.setUniversity(universityService.findById(id));
		universityService.addGroup(newGroup, id);
		modelAndView.addObject("group", newGroup);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/group_add");
		return modelAndView;
	}

	@ModelAttribute("group")
	public Group createModel() {
	    return new Group();
	}
	
	@GetMapping({ "/group_by_id" }) 
	public ModelAndView findById(	@PathVariable(value = "id") long id, 
									@RequestParam("id") long group_id, 
									ModelAndView modelAndView) {
		modelAndView.addObject("group", universityService.findGroupById(group_id, id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/group_by_id");
		return modelAndView;
	}

	@GetMapping({ "/delete" })
	public ModelAndView delete(	@PathVariable(value = "id") long id, 
								@RequestParam("id") long group_id, 
								ModelAndView modelAndView) {
		universityService.removeGroup(group_id, id);
		modelAndView.addObject("groups", universityService.findAllGroups(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/grouplist");
		return modelAndView;
	}

	@PostMapping({ "/update" })
	public ModelAndView update( @PathVariable(value = "id") long id, 
						 		@RequestParam long group_id, 
						 		@RequestParam String name, 
						 		ModelAndView modelAndView) {
		Group group = new Group(name, group_id, universityService.findById(id));
		universityService.updateGroup(group);
		modelAndView.addObject("group", group);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/group_update");
		return modelAndView;
	}
	
	@PostMapping({ "/courses" })
	public ModelAndView addCourseToGroup(@PathVariable(value = "id") long id, 
								   		 @RequestParam long group_id,
								   		 @RequestParam long course_id,
								   		 ModelAndView modelAndView){
		Group group = universityService.findGroupById(group_id, id);
		Course course = universityService.findCourseById(course_id, id);
		universityService.addCourseToGroup(group, course, id);
		Set<Course> courses = universityService.findGroupById(group_id, id).getCourses();
		modelAndView.addObject("courses", courses);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/group_courses");
		return modelAndView;
	}
	
	@PostMapping({ "/courselist" })
	public ModelAndView removeCourse(	@PathVariable(value = "id") long id, 
										@RequestParam long group_id,
										@RequestParam long course_id,
										ModelAndView modelAndView){
		Group group = universityService.findGroupById(group_id, id);
		Course course = universityService.findCourseById(course_id, id);
		universityService.removeCourseFromGroup(group, course, id);
		Set<Course> courses = universityService.findGroupById(group_id, id).getCourses();
		modelAndView.addObject("courses", courses);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/group_courses");
		return modelAndView;
	}

	@GetMapping({ "/grouplist_by_course" })
	public ModelAndView groupListByCourse(	@PathVariable(value = "id") long id, 
											@RequestParam long course_id, 
											ModelAndView modelAndView) {
		Iterable<Group> groups = universityService.filterGroupsByCourseId(course_id, id);
		modelAndView.addObject("groups", groups);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("groups/grouplist_by_course");
		return modelAndView;
	}
	
	@ExceptionHandler({RequestException.class, InvalidParameterException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleError(RequestException e, InvalidParameterException i) {
        return "redirect:/error.html";
    }
}
