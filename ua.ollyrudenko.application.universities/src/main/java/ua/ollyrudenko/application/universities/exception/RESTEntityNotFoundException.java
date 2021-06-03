package ua.ollyrudenko.application.universities.exception;

public class RESTEntityNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	RESTEntityNotFoundException(Long id) {
		super("Could not find entity " + id);
	}
}
