package br.com.wkallil.services;


import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.exceptions.ResourceNotFoundException;
import br.com.wkallil.mapper.PersonMapper;
import br.com.wkallil.models.Person;
import br.com.wkallil.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

        return personMapper.toDto(person);
    }

    public List<PersonDTO> findAll() {
        logger.info("Finding all people!");
        var people = repository.findAll();

        return personMapper.toDtoList(people);
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
        logger.info("Creating one person!");

        Person entity = personMapper.toEntity(person);
        return personMapper.toDto(repository.save(entity));
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );


        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return personMapper.toDto(repository.save(entity));
    }
}
