package ua.ollyrudenko.application.universities.SpringBootTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ua.ollyrudenko.application.universities.University;
import ua.ollyrudenko.application.universities.controller.UniversitiesController;
import ua.ollyrudenko.application.universities.service.UniversityService;

@ComponentScan("ua.ollyrudenko.application.universities.controller")
@RunWith(SpringRunner.class)
@WebMvcTest(UniversitiesController.class)
public class UniversityControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UniversityService service;

	@Test
	public void getAllUniversitiesWithGet_toViewPageIndex_andModelUniversities_withStatusOk() throws Exception {
		University university = new University("Controllers University");
		List<University> allUniversities = Arrays.asList(university);
		Mockito.when(service.findAll()).thenReturn(allUniversities);

		mockMvc.perform(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(view().name("index"))
				.andExpect(model().attribute("universities", allUniversities))
				.andExpect(status().isOk());
	}
	
	@Test
	public void getUniversityByIdWithGET_toViewPage_universitySlashUnivesity_index_andModelUniversity_withStatusOk() throws Exception {
		University university = new University(1, "First University of Ukraine");
		Mockito.when(service.findById(1)).thenReturn(university);

		mockMvc.perform(MockMvcRequestBuilders.get("/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(view().name("university/university_index"))
				.andExpect(model().attribute("university", university))
				.andExpect(status().isOk());
	}
	
	@Test
	public void addUniversityWithPOST_toViewPageIndex_andModelUniversities_withStatusOk() throws Exception {
		University university = new University(1, "Second University of Ukraine");
		List<University> allUniversities = Arrays.asList(university);
		Mockito.when(service.findAll()).thenReturn(allUniversities);

		mockMvc.perform(MockMvcRequestBuilders.post("/").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", university.getName()))
				.andExpect(view().name("index"))
				.andExpect(model().attribute("universities", allUniversities))
				.andExpect(status().isOk());
	}
	
	@Test
	public void deleteUniversityWithDELETE_toViewPageIndex_andModelUniversities_withStatusOk() throws Exception {
		University university = new University(1, "Third University of Ukraine");
		List<University> allUniversities = Arrays.asList(university);
		Mockito.when(service.findAll()).thenReturn(allUniversities);
		Mockito.doNothing().when(service).delete(university);

		mockMvc.perform(MockMvcRequestBuilders.get("/delete").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "1"))
				.andExpect(view().name("index"))
				.andExpect(model().attribute("universities", allUniversities))
				.andExpect(status().isOk());
	}
	
	@Test
	public void updateUniversityWithPOST_toViewPageIndex_andModelUniversities_withStatusOk() throws Exception {
		University university = new University(8, "Updated University of Ukraine");
		List<University> allUniversities = Arrays.asList(university);
		Mockito.when(service.findAll()).thenReturn(allUniversities);
		Mockito.when(service.findById(university.getId())).thenReturn(university);
		Mockito.when(service.insert(university)).thenReturn(university);

		mockMvc.perform(MockMvcRequestBuilders.post("/update").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "8")
				.param("name", "Updated University of Ukraine"))
				.andExpect(view().name("index"))
				.andExpect(model().attribute("universities", service.findAll()))
				.andExpect(status().isOk());
	}

}
