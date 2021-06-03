package ua.ollyrudenko.application.universities.controller;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.Time;

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

import ua.ollyrudenko.application.universities.dto.LessonDTO;
import ua.ollyrudenko.application.universities.exception.RequestException;
import ua.ollyrudenko.application.universities.model.Lesson;
import ua.ollyrudenko.application.universities.service.UniversityService;

@Controller
@RequestMapping({ "{id}/lessons" })
public class LessonsController {
	
	@Autowired
	UniversityService 		universityService;
	
	
	@GetMapping
	public ModelAndView lessonsIndex(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lessons_index");
		return modelAndView;
	}

	@GetMapping({ "/lessonlist" })
	public ModelAndView findAll(@PathVariable(value = "id") long id, ModelAndView modelAndView) {
		Iterable<Lesson> lessons = universityService.findAllLessons(id);
		modelAndView.addObject("lessons", lessons);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lessonlist");
		return modelAndView;
	}

	@PostMapping
	public ModelAndView add(@PathVariable(value = "id") long id, 
					  		@ModelAttribute ("lessondto") @Valid LessonDTO lessonDTO,
					  		BindingResult bindingResult, 
					  		ModelAndView modelAndView) {
		modelAndView.setViewName("lessons/lessons_index");
		if (bindingResult.hasErrors()) {
			return modelAndView; 
		}
		Lesson newLesson = universityService.convertLessontDTOtoLesson(lessonDTO, id);
		newLesson.setUniversity(universityService.findById(id));
		universityService
		.planLesson(universityService.findCourseById(lessonDTO.getCourse_id(), id), 
   					universityService.findTeacherById(lessonDTO.getTeacher_id(), id),
   					lessonDTO.getClassroom(), 
   					universityService.findGroupById(lessonDTO.getGroup_id(), id),   			   
   					Date.valueOf(lessonDTO.getDate()),
   					Time.valueOf(lessonDTO.getTime()), 
   					id);
		modelAndView.addObject("lesson", newLesson);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lesson_add");
		return modelAndView;
	}
	
	@ModelAttribute("lessondto")
	public LessonDTO createModel() {
	    return new LessonDTO();
	}

	@GetMapping({ "/lesson_by_id" })
	public ModelAndView findById( @PathVariable(value = "id") long id, 
								  @RequestParam("id") long lesson_id, 
								  ModelAndView modelAndView) {
		Lesson lesson = universityService.findLessonById(lesson_id, id);
		modelAndView.addObject("lesson", lesson);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lesson_by_id");
		return modelAndView;
	}

	@GetMapping({ "/delete" })
	public ModelAndView delete(	@PathVariable(value = "id") long id, 
						 		@RequestParam("id") long lesson_id, 
						 		ModelAndView modelAndView) {
		universityService.removeLesson(lesson_id, id);
		modelAndView.addObject("lessons", universityService.findAllLessons(id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lessonlist");
		return modelAndView;
	}

	@PostMapping({ "/update" })
	public ModelAndView update(	@PathVariable(value = "id") long id, 
						 		@RequestParam long lesson_id, 
						 		@RequestParam long course_id, 
						 		@RequestParam long teacher_id,
						 		@RequestParam int classroom, 
						 		@RequestParam long group_id, 
						 		@RequestParam String time,
						 		@RequestParam String date, 
						 		ModelAndView modelAndView) {
		Lesson updateLesson = new Lesson(lesson_id, 
										 universityService.findCourseById(course_id, id), 
										 universityService.findTeacherById(teacher_id, id),
										 classroom, 
										 universityService.findGroupById(group_id, id),   
										 Time.valueOf(time),
										 Date.valueOf(date), 
										 universityService.findById(id));

		universityService.replanLesson(updateLesson, id);
		modelAndView.addObject("lesson", universityService.findLessonById(lesson_id, id));
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lesson_update");
		return modelAndView;
	}

	@GetMapping({ "/lessonlist_by_group" })
	public ModelAndView lessonListByGroup(	@PathVariable(value = "id") long id, 
											@RequestParam long group_id, 
											@RequestParam String start, 
											@RequestParam String end,
											ModelAndView modelAndView) {
		Iterable<Lesson> lessons = 	universityService
									.filterScheduleByGroup(	universityService.findGroupById(group_id, 
															id), 
															Date.valueOf(start), 
															Date.valueOf(end), 
															id);
		modelAndView.addObject("lessons", lessons);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lessonlist_by_group");
		return modelAndView;
	}

	@GetMapping({ "/lessonlist_by_teacher" })
	public ModelAndView lessonListByTeacher(@PathVariable(value = "id") long id, 
									  		@RequestParam long teacher_id,
									  		@RequestParam String start, 
									  		@RequestParam String end,
									  		ModelAndView modelAndView) {
		Iterable<Lesson> lessons = 	universityService
									.filterScheduleByTeacher(universityService.findTeacherById(teacher_id, id), 
															 Date.valueOf(start), 
															 Date.valueOf(end), 
															 id);
		modelAndView.addObject("lessons", lessons);
		modelAndView.addObject("id", id);
		modelAndView.setViewName("lessons/lessonlist_by_teacher");
		return modelAndView;
	}
	
	@ExceptionHandler({RequestException.class, InvalidParameterException.class})
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleError(RequestException e, InvalidParameterException i) {
        return "redirect:/error.html";
    }
}