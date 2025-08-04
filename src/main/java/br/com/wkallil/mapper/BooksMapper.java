package br.com.wkallil.mapper;

import br.com.wkallil.data.dto.v1.BooksDTO;
import br.com.wkallil.models.Books;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BooksMapper {

    BooksDTO toDto(Books books);

    Books toEntity(BooksDTO booksDTO);

    List<BooksDTO> toDtoList(List<Books> booksList);

    List<Books> toEntityList(List<BooksDTO> booksDTOList);

}
