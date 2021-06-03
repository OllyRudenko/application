package ua.ollyrudenko.application.universities.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.ollyrudenko.application.universities.University;
import ua.ollyrudenko.application.universities.modelRoot.Result;
import ua.ollyrudenko.application.universities.springDataJpa.UniversityRepository;
import ua.ollyrudenko.task09.university.testAnnotations.UniversitySystemTests;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@UniversitySystemTests
public class UniversitySystemTest {

	@Autowired
	UniversityRepository universityRepository;

	@Autowired
	WebApplicationContext context;

	MockMvc mockMvc;

	RequestBuilder requestBuilder;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void getAllUniversities_withStatusOk() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/rest/universities").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void getUniversityById_withStatusOk() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/rest/universities/{id}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void saveNewUniversityToDB_withStatusIsCreated() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/rest/universities")
						.content(asJsonString(new University("Test University")))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void updateUniversityFromDB_withStatusCreated() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.put("/rest/universities/{id}", 123)
						.content(asJsonString(new University(123, "Test UNI"))).contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is(201))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test UNI"));
	}

	@Test
	public void deleteUniversityFromDBbyId_withStatusIsNotFound() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/rest/universities/{id}", 3L)
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());

	}

	@Test
	public void deleteObjectFromDataBase_withStatusNoContent() throws Exception {
		University university = new University("TEMPORARY University");
		universityRepository.save(university);
		University universityFounded = (universityRepository.findUniversityByName("TEMPORARY University"));
		this.mockMvc.perform(MockMvcRequestBuilders.delete("/rest/universities/{id}", universityFounded.getId()))
				.andDo(print()).andExpect(status().is(204));
	}

	@Test
	public void saveObjectToDataBaseAndEqualsItNameWithOtherObjectName() {
		University university = new University();
		university.setName("National University of Ukraine");
		universityRepository.save(university);
		University universityFounded = (universityRepository.findUniversityByName("National University of Ukraine"));
		assertThat(universityFounded.getName().equals(university.getName()));
	}

	@Test
	public void deleteObjectFromDataBaseAndCatchException() {
		University university = new University("DELETE University");
		universityRepository.save(university);
		University universityFounded = (universityRepository.findUniversityByName("DELETE University"));
		universityRepository.deleteById(universityFounded.getId());
		Assert.assertThrows(RuntimeException.class,
				() -> (universityRepository.findById(universityFounded.getId())).get());
	}

	@Test
	public void saveUniversitiesToDB_takeThemByNameFromDB_getResult_equalsUniversitiesFromDBAndJson() throws Exception {
		University university = new University("Testing University");
		University university2 = new University("Best University");
		universityRepository.save(university);
		universityRepository.save(university2);
		University testingUniversityFounded = (universityRepository.findUniversityByName("Testing University"));
		University bestUniversityFounded = universityRepository.findUniversityByName("High University");

		MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders.get("/rest/universities").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(MockMvcResultMatchers.content().contentType("application/json")).andReturn();
		String content = result.getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		Result results = mapper.readValue(content, Result.class);

		assertThat(testingUniversityFounded.equals(results.getUniversityRoot().getUniversities().get(1)));
		assertThat(bestUniversityFounded.equals(results.getUniversityRoot().getUniversities().get(2)));
	}
}
