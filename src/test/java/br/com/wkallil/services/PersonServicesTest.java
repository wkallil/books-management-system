package br.com.wkallil.services;

import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.exceptions.RequiredObjectIsNullException;
import br.com.wkallil.mapper.PersonMapper;
import br.com.wkallil.models.Person;
import br.com.wkallil.repositories.PersonRepository;
import br.com.wkallil.unittests.mapper.mocks.MockPerson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {

    MockPerson input;

    @InjectMocks
    private PersonServices service;

    @Mock
    private PersonMapper mapper;

    @Mock
    PersonRepository repository;

    @Mock
    private PagedResourcesAssembler<PersonDTO> assembler;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
    }

    @Test
    void findById() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(mapper.toDto(person)).thenReturn(dto);

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/person/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/person/all?page=0&size=12&direction=asc")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/person/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/person/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/person/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());

    }

    @Test
    void findAll() {
        List<Person> list = input.mockEntityList();
        List<PersonDTO> dtoList = input.mockDTOList();

        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));
        Page<Person> page = new PageImpl<>(list, pageable, list.size());

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(any(Person.class))).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            int index = list.indexOf(person);
            return dtoList.get(index);
        });

        List<EntityModel<PersonDTO>> entityModels = dtoList.stream()
                .map(EntityModel::of)
                .toList();

        PagedModel<EntityModel<PersonDTO>> pagedModel = PagedModel.of(
                entityModels,
                new PagedModel.PageMetadata(12, 0, list.size())
        );

        when(assembler.toModel(ArgumentMatchers.any(), ArgumentMatchers.<Link>any()))
                .thenReturn(pagedModel);

        var result = service.findAll(pageable);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertFalse(result.getContent().isEmpty());

        var personOne = result.getContent().stream().toList().get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getContent());
        assertNotNull(personOne.getLinks());

        assertEquals("Address Test1", personOne.getContent().getAddress());
        assertEquals("First Name Test1", personOne.getContent().getFirstName());
        assertEquals("Last Name Test1", personOne.getContent().getLastName());
        assertEquals("Female", personOne.getContent().getGender());

        var personFive = result.getContent().stream().toList().get(5);

        assertNotNull(personFive);
        assertNotNull(personFive.getContent());
        assertNotNull(personFive.getLinks());

        assertEquals("Address Test5", personFive.getContent().getAddress());
        assertEquals("First Name Test5", personFive.getContent().getFirstName());
        assertEquals("Last Name Test5", personFive.getContent().getLastName());
        assertEquals("Female", personFive.getContent().getGender());

        var personTen = result.getContent().stream().toList().get(10);

        assertNotNull(personTen);
        assertNotNull(personTen.getContent());
        assertNotNull(personTen.getLinks());

        assertEquals("Address Test10", personTen.getContent().getAddress());
        assertEquals("First Name Test10", personTen.getContent().getFirstName());
        assertEquals("Last Name Test10", personTen.getContent().getLastName());
        assertEquals("Male", personTen.getContent().getGender());
    }

    @Test
    void findPeopleByName() {
        List<Person> list = input.mockEntityList();
        List<PersonDTO> dtoList = input.mockDTOList();

        Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));
        Page<Person> page = new PageImpl<>(list, pageable, list.size());

        when(repository.findPeopleByName("Address", pageable)).thenReturn(page);
        when(mapper.toDto(any(Person.class))).thenAnswer(invocation -> {
            Person person = invocation.getArgument(0);
            int index = list.indexOf(person);
            return dtoList.get(index);
        });

        List<EntityModel<PersonDTO>> entityModels = dtoList.stream()
                .map(EntityModel::of)
                .toList();

        PagedModel<EntityModel<PersonDTO>> pagedModel = PagedModel.of(
                entityModels,
                new PagedModel.PageMetadata(12, 0, list.size())
        );

        when(assembler.toModel(ArgumentMatchers.any(), ArgumentMatchers.<Link>any()))
                .thenReturn(pagedModel);

        var result = service.findPeopleByName("Address",pageable);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertFalse(result.getContent().isEmpty());

        var personOne = result.getContent().stream().toList().get(1);

        assertNotNull(personOne);
        assertNotNull(personOne.getContent());
        assertNotNull(personOne.getLinks());

        assertEquals("Address Test1", personOne.getContent().getAddress());
        assertEquals("First Name Test1", personOne.getContent().getFirstName());
        assertEquals("Last Name Test1", personOne.getContent().getLastName());
        assertEquals("Female", personOne.getContent().getGender());

        var personFive = result.getContent().stream().toList().get(5);

        assertNotNull(personFive);
        assertNotNull(personFive.getContent());
        assertNotNull(personFive.getLinks());

        assertEquals("Address Test5", personFive.getContent().getAddress());
        assertEquals("First Name Test5", personFive.getContent().getFirstName());
        assertEquals("Last Name Test5", personFive.getContent().getLastName());
        assertEquals("Female", personFive.getContent().getGender());

        var personTen = result.getContent().stream().toList().get(10);

        assertNotNull(personTen);
        assertNotNull(personTen.getContent());
        assertNotNull(personTen.getLinks());

        assertEquals("Address Test10", personTen.getContent().getAddress());
        assertEquals("First Name Test10", personTen.getContent().getFirstName());
        assertEquals("Last Name Test10", personTen.getContent().getLastName());
        assertEquals("Male", personTen.getContent().getGender());
    }

    @Test
    void delete() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));

        service.delete(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Person.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void create() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(person);
        when(repository.save(person)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(dto);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/person/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/person/all?page=0&size=12&direction=asc")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/person/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/person/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/person/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        PersonDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        when(repository.save(person)).thenReturn(person);
        when(mapper.toDto(person)).thenReturn(dto);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/person/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/person/all?page=0&size=12&direction=asc")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/person/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/person/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/person/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Address Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}