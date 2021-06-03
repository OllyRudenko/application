package ua.ollyrudenko.application.universities.springDataJpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ua.ollyrudenko.application.universities.University;

@RepositoryRestResource(collectionResourceRel = "universities", path = "universities")
public interface UniversityRepository extends PagingAndSortingRepository<University, Long> {

	University findUniversityByName(String name);

}