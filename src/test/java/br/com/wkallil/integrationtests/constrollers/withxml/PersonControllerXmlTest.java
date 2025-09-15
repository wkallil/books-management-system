package br.com.wkallil.integrationtests.constrollers.withxml;

import br.com.wkallil.configs.TestConfigs;
import br.com.wkallil.integrationtests.dto.AccountCredentialsDTO;
import br.com.wkallil.integrationtests.dto.PersonDTO;
import br.com.wkallil.integrationtests.dto.TokenDTO;
import br.com.wkallil.integrationtests.dto.pagedmodels.PagedModelPerson;
import br.com.wkallil.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerXmlTest extends AbstractIntegrationTest {

    @Value("${test.username}")
    private String username;

    @Value("${test.password}")
    private String password;

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static PersonDTO personDTO;
    private static TokenDTO tokenDTO;

    @BeforeAll
    static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        personDTO = new PersonDTO();
        tokenDTO = new TokenDTO();
    }

    @Test
    @Order(1)
    void signin() {
        AccountCredentialsDTO credentials = new AccountCredentialsDTO(username, password);

        tokenDTO = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);

        Assertions.assertNotNull(tokenDTO.getAccessToken());
        Assertions.assertNotNull(tokenDTO.getRefreshToken());
    }

    @Test
    @Order(2)
    void create() throws JsonProcessingException {
        mockPersonDTO();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person/create")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(personDTO)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
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
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
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
    @Order(4)
    void update() throws JsonProcessingException {
        personDTO.setLastName("Ruiva 2");

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person/update")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .body(personDTO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
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
    @Order(5)
    void disablePerson() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person/disable")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
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
    @Order(6)
    void delete() {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person/delete")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given(specification)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .pathParam("id", personDTO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(7)
    void findAll() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person/all")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParam("page", 0)
                .queryParam("size", 12)
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        var pagedModel = objectMapper.readValue(content, PagedModelPerson.class);
        assertNotNull(pagedModel);
        assertNotNull(pagedModel.getContent());
        var people = pagedModel.getContent();

        assertNotNull(people);
        assertTrue(people.size() > 1);

        var firstPerson = people.getFirst();
        assertNotNull(firstPerson.getId());
        assertNotNull(firstPerson.getFirstName());
        assertNotNull(firstPerson.getLastName());
        assertNotNull(firstPerson.getAddress());
        assertNotNull(firstPerson.getGender());
        assertNotNull(firstPerson.getEnabled());

        assertEquals("Abba",firstPerson.getFirstName());
        assertEquals("Garfield",firstPerson.getLastName());
        assertEquals("Apt 777",firstPerson.getAddress());
        assertEquals("Male",firstPerson.getGender());
        assertFalse(firstPerson.getEnabled());

        var fivePerson = people.get(5);
        assertNotNull(fivePerson.getId());
        assertNotNull(fivePerson.getFirstName());
        assertNotNull(fivePerson.getLastName());
        assertNotNull(fivePerson.getAddress());
        assertNotNull(fivePerson.getGender());
        assertNotNull(fivePerson.getEnabled());

        assertEquals("Adelaide",fivePerson.getFirstName());
        assertEquals("Sammon",fivePerson.getLastName());
        assertEquals("13th Floor",fivePerson.getAddress());
        assertEquals("Female",fivePerson.getGender());
        assertFalse(fivePerson.getEnabled());

        assertNotEquals(firstPerson.getId(), fivePerson.getId());
        assertNotEquals(firstPerson.getFirstName(), fivePerson.getFirstName());
    }
    @Test
    @Order(8)
    void findPeopleByName() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ALLOWED)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken())
                .setBasePath("/api/v1/person/findPeopleByName")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParam("page", 0)
                .queryParam("size", 12)
                .pathParam("firstName", "and")
                .when()
                .get("{firstName}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        var pagedModel = objectMapper.readValue(content, PagedModelPerson.class);
        assertNotNull(pagedModel);
        assertNotNull(pagedModel.getContent());
        var people = pagedModel.getContent();

        assertNotNull(people);
        assertTrue(people.size() > 1);

        var firstPerson = people.getFirst();
        assertNotNull(firstPerson.getId());
        assertNotNull(firstPerson.getFirstName());
        assertNotNull(firstPerson.getLastName());
        assertNotNull(firstPerson.getAddress());
        assertNotNull(firstPerson.getGender());
        assertNotNull(firstPerson.getEnabled());

        assertEquals("Alessandra",firstPerson.getFirstName());
        assertEquals("Gepson",firstPerson.getLastName());
        assertEquals("Apt 1865",firstPerson.getAddress());
        assertEquals("Female",firstPerson.getGender());
        assertTrue(firstPerson.getEnabled());

        var fivePerson = people.get(5);
        assertNotNull(fivePerson.getId());
        assertNotNull(fivePerson.getFirstName());
        assertNotNull(fivePerson.getLastName());
        assertNotNull(fivePerson.getAddress());
        assertNotNull(fivePerson.getGender());
        assertNotNull(fivePerson.getEnabled());

        assertEquals("Bertrand",fivePerson.getFirstName());
        assertEquals("Duferie",fivePerson.getLastName());
        assertEquals("Apt 123",fivePerson.getAddress());
        assertEquals("Male",fivePerson.getGender());
        assertFalse(fivePerson.getEnabled());

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
