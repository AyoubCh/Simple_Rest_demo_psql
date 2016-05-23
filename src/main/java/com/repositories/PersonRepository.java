package com.repositories;

import com.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

/*import java.util.List;

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	 List<Person> findByLastName(@Param("name") String name);

	 Person findByFirstName(@Param("name") String name);

	 Person findById(@Param("id") long id);

}*/


public interface PersonRepository extends JpaRepository<Person, Long> {

	Collection<Person> findByLastName(String name);

	Person findByFirstName(String name);

	Person findById( long id);
}
