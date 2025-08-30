package br.com.wkallil.services;


import br.com.wkallil.constrollers.PersonController;
import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.exceptions.BadRequestException;
import br.com.wkallil.exceptions.FileStorageException;
import br.com.wkallil.exceptions.RequiredObjectIsNullException;
import br.com.wkallil.exceptions.ResourceNotFoundException;
import br.com.wkallil.file.importer.contract.FileImporter;
import br.com.wkallil.file.importer.factory.FileImporterFactory;
import br.com.wkallil.mapper.PersonMapper;
import br.com.wkallil.models.Person;
import br.com.wkallil.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private final Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");
        Person person = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );

        var dto = personMapper.toDto(person);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all people!");

        var peoplePageable = repository.findAll(pageable);

        return buildPagedModel(pageable, peoplePageable);
    }

    public PagedModel<EntityModel<PersonDTO>> findPeopleByName(String firstName, Pageable pageable) {
        logger.info("Finding People by name!");

        var peoplePageable = repository.findPeopleByName(firstName, pageable);

        return buildPagedModel(pageable, peoplePageable);
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person person = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );
        repository.delete(person);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one person!");

        repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );
        repository.disablePerson(id);

        var entity = repository.findById(id).get();
        var dto = personMapper.toDto(entity);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null) {
            logger.info("Person is null!");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating one person!");

        Person entity = personMapper.toEntity(person);
        var dto = personMapper.toDto(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from File!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File");

        try(InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null!"));

            FileImporter importer = this.importer.getImporter(filename);

            List<Person> entities = importer.importFile(inputStream)
                    .stream()
                    .map(dto -> repository.save(personMapper.toEntity(dto)))
                    .toList();

            return entities.stream()
                    .map(entity -> {
                        var dto = personMapper.toDto(entity);
                        addHateoasLinks(dto);
                        return dto;
                    })
                    .toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing file!");
        }
    }

    public PersonDTO update(PersonDTO person) {

        if (person == null) {
            logger.info("Person is null!");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating one person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );


        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = personMapper.toDto(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> peoplePageable) {
        var peopleWithLinks = peoplePageable.map(person -> {
            var dto = personMapper.toDto(person);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                                PersonController.class)
                        .findAll(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                String.valueOf(pageable.getSort())
                        )
                )
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findPeopleByName(dto.getFirstName(), 0, 12, "asc")).withRel("findPeopleByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
    }
}
