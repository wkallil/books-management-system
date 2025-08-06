package br.com.wkallil.constrollers;

import br.com.wkallil.constrollers.docs.PersonControllerDocs;
import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.unittests.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/person")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonServices services;

    @GetMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO findById(@PathVariable("id") Long id) {
        return services.findById(id);
    }

    @GetMapping(value = "/all",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public List<PersonDTO> findAll() {
        return services.findAll();
    }

    @PostMapping(
            value = "/create",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO person) {
        return services.create(person);
    }

    @PutMapping(
            value = "/update",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            },
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO person) {
        return services.update(person);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}
