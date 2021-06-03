package ua.ollyrudenko.application.universities.modelRoot;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import ua.ollyrudenko.application.universities.University;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UniversityRoot {

	public UniversityRoot() {
	}

	@JsonProperty("universities")
	private List<University> universities = new ArrayList<University>();

	public List<University> getUniversities() {
		return universities;
	}

	public void setUniversities(List<University> universities) {
		this.universities = universities;
	}
}
