package br.com.wkallil.integrationtests.constrollers.withjson;

import br.com.wkallil.configs.TestConfigs;
import br.com.wkallil.integrationtests.dto.PersonDTO;
import br.com.wkallil.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO personDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personDTO = new PersonDTO();
    }

    @Test
    @Order(0)
    void create() throws JsonProcessingException {
        mockPersonDTO();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/create")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPersonDTO = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPersonDTO;

        assertNotNull(createdPersonDTO.getId());
        assertNotNull(createdPersonDTO.getFirstName());
        assertNotNull(createdPersonDTO.getLastName());
        assertNotNull(createdPersonDTO.getAddress());
        assertNotNull(createdPersonDTO.getGender());
        assertNotNull(createdPersonDTO.getEnabled());

        assertTrue(createdPersonDTO.getId() > 0);

        assertEquals("Gabriela", createdPersonDTO.getFirstName());
        assertEquals("Ruiva", createdPersonDTO.getLastName());
        assertEquals("Olinda - PE", createdPersonDTO.getAddress());
        assertEquals("Female", createdPersonDTO.getGender());
        assertTrue(createdPersonDTO.getEnabled());

    }

    @Test
    @Order(1)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO createdPersonDTO = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPersonDTO;

        assertNotNull(createdPersonDTO.getId());
        assertNotNull(createdPersonDTO.getFirstName());
        assertNotNull(createdPersonDTO.getLastName());
        assertNotNull(createdPersonDTO.getAddress());
        assertNotNull(createdPersonDTO.getGender());
        assertNotNull(createdPersonDTO.getEnabled());

        assertTrue(createdPersonDTO.getId() > 0);

        assertEquals("Gabriela", createdPersonDTO.getFirstName());
        assertEquals("Ruiva", createdPersonDTO.getLastName());
        assertEquals("Olinda - PE", createdPersonDTO.getAddress());
        assertEquals("Female", createdPersonDTO.getGender());
        assertTrue(createdPersonDTO.getEnabled());
    }


    @Test
    @Order(2)
    void update() throws JsonProcessingException {
        personDTO.setLastName("Ruiva 2");

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/update")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO updatedPersonDTO = objectMapper.readValue(content, PersonDTO.class);
        personDTO = updatedPersonDTO;

        assertNotNull(updatedPersonDTO.getId());
        assertNotNull(updatedPersonDTO.getFirstName());
        assertNotNull(updatedPersonDTO.getLastName());
        assertNotNull(updatedPersonDTO.getAddress());
        assertNotNull(updatedPersonDTO.getGender());
        assertNotNull(updatedPersonDTO.getEnabled());

        assertEquals(personDTO.getId(), updatedPersonDTO.getId());
        assertEquals("Gabriela", updatedPersonDTO.getFirstName());
        assertEquals("Ruiva 2", updatedPersonDTO.getLastName());
        assertEquals("Olinda - PE", updatedPersonDTO.getAddress());
        assertEquals("Female", updatedPersonDTO.getGender());
        assertTrue(updatedPersonDTO.getEnabled());
    }

    @Test
    @Order(3)
    void disablePerson() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/disable")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonDTO disabledPersonDTO = objectMapper.readValue(content, PersonDTO.class);
        personDTO = disabledPersonDTO;

        assertNotNull(disabledPersonDTO.getId());
        assertNotNull(disabledPersonDTO.getFirstName());
        assertNotNull(disabledPersonDTO.getLastName());
        assertNotNull(disabledPersonDTO.getAddress());
        assertNotNull(disabledPersonDTO.getGender());
        assertNotNull(disabledPersonDTO.getEnabled());

        assertEquals(personDTO.getId(), disabledPersonDTO.getId());
        assertEquals("Gabriela", disabledPersonDTO.getFirstName());
        assertEquals("Ruiva 2", disabledPersonDTO.getLastName());
        assertEquals("Olinda - PE", disabledPersonDTO.getAddress());
        assertFalse(disabledPersonDTO.getEnabled());
    }

    @Test
    @Order(4)
    void delete() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/delete")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void findAll() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/all")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

        assertNotNull(people);
        assertTrue(people.size() > 1);

        var firstPerson = people.getFirst();
        assertNotNull(firstPerson.getId());
        assertNotNull(firstPerson.getFirstName());
        assertNotNull(firstPerson.getLastName());
        assertNotNull(firstPerson.getAddress());
        assertNotNull(firstPerson.getGender());
        assertNotNull(firstPerson.getEnabled());

        assertEquals("Ana",firstPerson.getFirstName());
        assertEquals("Silva",firstPerson.getLastName());
        assertEquals("Rua das Flores, 123",firstPerson.getAddress());
        assertEquals("Female",firstPerson.getGender());
        assertTrue(firstPerson.getEnabled());

        var fivePerson = people.get(5);
        assertNotNull(fivePerson.getId());
        assertNotNull(fivePerson.getFirstName());
        assertNotNull(fivePerson.getLastName());
        assertNotNull(fivePerson.getAddress());
        assertNotNull(fivePerson.getGender());
        assertNotNull(fivePerson.getEnabled());

        assertEquals("Fábio",fivePerson.getFirstName());
        assertEquals("Rodrigues",fivePerson.getLastName());
        assertEquals("Rua Nova, 303",fivePerson.getAddress());
        assertEquals("Male",fivePerson.getGender());
        assertTrue(fivePerson.getEnabled());

        assertNotEquals(firstPerson.getId(), fivePerson.getId());
        assertNotEquals(firstPerson.getFirstName(), fivePerson.getFirstName());
    }


    private void mockPersonDTO() {
        personDTO.setFirstName("Gabriela");
        personDTO.setLastName("Ruiva");
        personDTO.setAddress("Olinda - PE");
        personDTO.setGender("Female");
        personDTO.setEnabled(true);
    }
}
