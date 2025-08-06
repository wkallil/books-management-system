package br.com.wkallil.unittests.services;


import br.com.wkallil.constrollers.PersonController;
import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.exceptions.RequiredObjectIsNullException;
import br.com.wkallil.exceptions.ResourceNotFoundException;
import br.com.wkallil.mapper.PersonMapper;
import br.com.wkallil.models.Person;
import br.com.wkallil.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {

    private final Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper personMapper;

    public PersonDTO findById(Long id) {
        logger.info("Finding one person!");
        Person person =  repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );

        var dto = personMapper.toDto(person);
        addHateoasLinks(dto);
        return dto;
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all people!");
        var people = repository.findAll();

        var dto = personMapper.toDtoList(people);
        dto.forEach(this::addHateoasLinks);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person person = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );
        repository.delete(person);
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

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
    }

}
