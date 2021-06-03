package ua.ollyrudenko.application.universities.modelRoot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

	@JsonProperty("_embedded")
	private UniversityRoot universityRoot;

	public UniversityRoot getUniversityRoot() {
		return universityRoot;
	}

	public void setUniversityRoot(UniversityRoot universityRoot) {
		this.universityRoot = universityRoot;
	}
}
