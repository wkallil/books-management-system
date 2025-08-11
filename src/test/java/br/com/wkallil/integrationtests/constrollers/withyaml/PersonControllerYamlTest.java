package br.com.wkallil.integrationtests.constrollers.withyaml;

import br.com.wkallil.configs.TestConfigs;
import br.com.wkallil.integrationtests.constrollers.withyaml.mapper.YAMLMapper;
import br.com.wkallil.integrationtests.dto.PersonDTO;
import br.com.wkallil.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YAMLMapper objectMapper;
    private static PersonDTO personDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new YAMLMapper();

        personDTO = new PersonDTO();
    }

    @Test
    @Order(0)
    void create() {
        mockPersonDTO();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/create")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdPersonDTO = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(personDTO, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

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
    void findById() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var createdPersonDTO = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

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
    void update() {
        personDTO.setLastName("Ruiva 2");

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/update")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var updatedPersonDTO = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .body(personDTO, objectMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

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
    void disablePerson() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/disable")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var disabledPersonDTO = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO.class, objectMapper);

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

        given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void findAll() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .setBasePath("/api/v1/person/all")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given()
                .config(RestAssuredConfig.config()
                        .encoderConfig(
                                EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
                )
                .spec(specification)
                .accept(MediaType.APPLICATION_YAML_VALUE)
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_YAML_VALUE)
                .extract()
                .body()
                .as(PersonDTO[].class, objectMapper);

        var people = Arrays.asList(content);

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
