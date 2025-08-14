package br.com.wkallil.services;


import br.com.wkallil.constrollers.BooksController;
import br.com.wkallil.constrollers.PersonController;
import br.com.wkallil.data.dto.v1.BooksDTO;
import br.com.wkallil.exceptions.RequiredObjectIsNullException;
import br.com.wkallil.exceptions.ResourceNotFoundException;
import br.com.wkallil.mapper.BooksMapper;
import br.com.wkallil.models.Books;
import br.com.wkallil.repositories.BooksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BooksServices {

    private final Logger logger = LoggerFactory.getLogger(BooksServices.class.getName());

    @Autowired
    private BooksRepository repository;

    @Autowired
    private BooksMapper booksMapper;

    @Autowired
    PagedResourcesAssembler<BooksDTO> assembler;

    public BooksDTO findById(Long id) {
        logger.info("Finding one Book!");
        Books books =  repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );

        var dto = booksMapper.toDto(books);
        addHateoasLinks(dto);
        return dto;
    }

    public PagedModel<EntityModel<BooksDTO>> findAll(Pageable pageable){

        logger.info("Finding all Books!");

        var booksPageable = repository.findAll(pageable);

        var booksWithLinks = booksPageable.map(person -> {
            var dto = booksMapper.toDto(person);
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
        return assembler.toModel(booksWithLinks, findAllLink);
    }

    public void delete(Long id) {
        logger.info("Deleting one Book!");

        Books books = repository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );
        repository.delete(books);
    }

    public BooksDTO create(BooksDTO books) {

        if (books == null) {
            logger.info("Book is null!");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Creating one Book!");

        Books entity = booksMapper.toEntity(books);
        var dto = booksMapper.toDto(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    public BooksDTO update(BooksDTO books) {

        if (books == null) {
            logger.info("Book is null!");
            throw new RequiredObjectIsNullException();
        }

        logger.info("Updating one Book!");

        Books entity = repository.findById(books.getId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No records found for this ID!")
                );


        entity.setAuthor(books.getAuthor());
        entity.setTitle(books.getTitle());
        entity.setLaunchDate(books.getLaunchDate());
        entity.setPrice(books.getPrice());

        var dto = booksMapper.toDto(repository.save(entity));
        addHateoasLinks(dto);
        return dto;
    }

    private void addHateoasLinks(BooksDTO dto) {
        dto.add(linkTo(methodOn(BooksController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BooksController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BooksController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(BooksController.class).findAll(0, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BooksController.class).update(dto)).withRel("update").withType("PUT"));
    }

}
