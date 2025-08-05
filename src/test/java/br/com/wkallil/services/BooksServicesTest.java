package br.com.wkallil.services;

import br.com.wkallil.data.dto.v1.BooksDTO;
import br.com.wkallil.exceptions.RequiredObjectIsNullException;
import br.com.wkallil.mapper.BooksMapper;
import br.com.wkallil.models.Books;
import br.com.wkallil.repositories.BooksRepository;
import br.com.wkallil.unittests.mapper.mocks.MockBooks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BooksServicesTest {

    MockBooks input;

    @InjectMocks
    private BooksServices service;

    @Mock
    private BooksMapper mapper;

    @Mock
    BooksRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBooks();
    }

    @Test
    void findById() {
        Books books = input.mockEntity(1);
        books.setId(1L);

        BooksDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(books));
        when(mapper.toDto(books)).thenReturn(dto);

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/books/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/books/all")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/books/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/books/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/books/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Launch Date Test" + new Date(), result.getLaunchDate());
        assertEquals(11.0, result.getPrice());

    }

    @Test
    void findAll() {
        List<Books> list = input.mockEntityList();
        List<BooksDTO> dtoList = input.mockDTOList();

        when(repository.findAll()).thenReturn(list);
        when(mapper.toDtoList(list)).thenReturn(dtoList);

        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var booksOne = people.get(1);

        assertNotNull(booksOne);
        assertNotNull(booksOne.getId());
        assertNotNull(booksOne.getLinks());

        assertTrue(booksOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/books/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(booksOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/books/all")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(booksOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/books/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(booksOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/books/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(booksOne.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/books/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Author Test1", booksOne.getAuthor());
        assertEquals("Title Test1", booksOne.getTitle());
        assertEquals("Launch Date Test" + new Date(), booksOne.getLaunchDate());
        assertEquals(11.0, booksOne.getPrice());

        var booksFive = people.get(5);

        assertNotNull(booksFive);
        assertNotNull(booksFive.getId());
        assertNotNull(booksFive.getLinks());

        assertTrue(booksFive.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/books/5")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(booksFive.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/books/all")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(booksFive.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/books/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(booksFive.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/books/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(booksFive.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/books/delete/5")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Author Test5", booksFive.getAuthor());
        assertEquals("Title Test5", booksFive.getTitle());
        assertEquals("Launch Date Test" + new Date(), booksFive.getLaunchDate());
        assertEquals(15.0, booksFive.getPrice());

        var booksTen = people.get(10);

        assertNotNull(booksTen);
        assertNotNull(booksTen.getId());
        assertNotNull(booksTen.getLinks());

        assertTrue(booksTen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/books/10")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(booksTen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/books/all")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(booksTen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/books/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(booksTen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/books/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(booksTen.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/books/delete/10")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Author Test10", booksTen.getAuthor());
        assertEquals("Title Test10", booksTen.getTitle());
        assertEquals("Launch Date Test" + new Date(), booksTen.getLaunchDate());
        assertEquals(20.0, booksTen.getPrice());
    }

    @Test
    void delete() {
        Books books = input.mockEntity(1);
        books.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(books));

        service.delete(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Books.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void create() {
        Books books = input.mockEntity(1);
        books.setId(1L);

        BooksDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(books);
        when(repository.save(books)).thenReturn(books);
        when(mapper.toDto(books)).thenReturn(dto);

        var result = service.create(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/books/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/books/all")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/books/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/books/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/books/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Launch Date Test" + new Date(), result.getLaunchDate());
        assertEquals(11.0, result.getPrice());
    }

    @Test
    void testCreateWithNullBooks() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void update() {
        Books books = input.mockEntity(1);
        books.setId(1L);

        BooksDTO dto = input.mockDTO(1);
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(books));
        when(repository.save(books)).thenReturn(books);
        when(mapper.toDto(books)).thenReturn(dto);

        var result = service.update(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith("/api/v1/books/1")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/v1/books/all")
                        && Objects.equals(link.getType(), "GET")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/v1/books/create")
                        && Objects.equals(link.getType(), "POST")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/v1/books/update")
                        && Objects.equals(link.getType(), "PUT")
                ));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith("/api/v1/books/delete/1")
                        && Objects.equals(link.getType(), "DELETE")
                ));

        assertEquals("Author Test1", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals("Launch Date Test" + new Date(), result.getLaunchDate());
        assertEquals(11.0, result.getPrice());
    }

    @Test
    void testUpdateWithNullBooks() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}