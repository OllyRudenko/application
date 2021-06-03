package ua.ollyrudenko.application.universities.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RESTEntityNotFoundAdvice {
	@ResponseBody
	@ExceptionHandler(RESTEntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String employeeNotFoundHandler(RESTEntityNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String serviceExceptionHandler(ServiceException ex) {
		return ex.getMessage();
	}
}
