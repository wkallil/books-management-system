package br.com.wkallil.unittests.mapper.mocks;

import br.com.wkallil.data.dto.v1.BooksDTO;
import br.com.wkallil.models.Books;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBooks {


    public Books mockEntity() {
        return mockEntity(0);
    }
    
    public BooksDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Books> mockEntityList() {
        List<Books> books = new ArrayList<Books>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BooksDTO> mockDTOList() {
        List<BooksDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }
    
    public Books mockEntity(Integer number) {
        Books book = new Books();
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate("Launch Date Test" + new Date());
        book.setId(number.longValue());
        book.setPrice(10.0 + number);
        return book;
    }

    public BooksDTO mockDTO(Integer number) {
        BooksDTO book = new BooksDTO();
        book.setAuthor("Author Test" + number);
        book.setTitle("Title Test" + number);
        book.setLaunchDate("Launch Date Test" + new Date());
        book.setId(number.longValue());
        book.setPrice(10.0 + number);
        return book;
    }

}