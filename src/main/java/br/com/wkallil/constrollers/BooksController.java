package br.com.wkallil.constrollers;

import br.com.wkallil.constrollers.docs.BooksControllerDocs;
import br.com.wkallil.data.dto.v1.BooksDTO;
import br.com.wkallil.unittests.services.BooksServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Endpoints for managing books")
public class BooksController implements BooksControllerDocs {

    @Autowired
    private BooksServices services;

    @GetMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_YAML_VALUE
            }
    )
    @Override
    public BooksDTO findById(@PathVariable("id") Long id) {
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
    public List<BooksDTO> findAll() {
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
    public BooksDTO create(@RequestBody BooksDTO book) {
        return services.create(book);
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
    public BooksDTO update(@RequestBody BooksDTO book) {
        return services.update(book);
    }

    @DeleteMapping(value = "/delete/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}
