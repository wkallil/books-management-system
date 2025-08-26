package br.com.wkallil.repositories;

import br.com.wkallil.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.wkallil.models.Person;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;

    private static Person person;

    @BeforeAll
    static void setUp() {
        person = new Person();
    }

    @Test
    @Order(0)
    void findPeopleByName() {

        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));

        person = repository.findPeopleByName("and", pageable).getContent().getFirst();

        assertNotNull(person);

        assertEquals("Alessandra",person.getFirstName());
        assertEquals("Gepson",person.getLastName());
        assertEquals("Apt 1865",person.getAddress());
        assertEquals("Female",person.getGender());
        assertTrue(person.getEnabled());
    }

    @Test
    @Order(1)
    void disablePerson() {

        Long id = person.getId();

        repository.disablePerson(id);

        var result = repository.findById(id);
        person = result.get();

        assertEquals("Alessandra",person.getFirstName());
        assertEquals("Gepson",person.getLastName());
        assertEquals("Apt 1865",person.getAddress());
        assertEquals("Female",person.getGender());
        assertFalse(person.getEnabled());
    }
}