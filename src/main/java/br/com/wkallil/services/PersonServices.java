package br.com.wkallil.services;


import br.com.wkallil.exceptions.ResourceNotFoundException;
import br.com.wkallil.models.Person;
import br.com.wkallil.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;


    public Person findById(Long id) {
        logger.info("Finding one person!");

        return repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );
    }

    public List<Person> findAll() {
        logger.info("Finding all people!");

        return repository.findAll();
    }

    public void delete(Long id) {
        logger.info("Deleting one person!");

        Person person = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );
        repository.delete(person);
    }

    public Person create(Person person) {
        logger.info("Creating one person!");

        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating one person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return repository.save(person);
    }
}
