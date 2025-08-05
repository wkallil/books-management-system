package br.com.wkallil.unittests.mapper;

import br.com.wkallil.data.dto.v1.BooksDTO;
import br.com.wkallil.mapper.BooksMapper;
import br.com.wkallil.mapper.BooksMapperImpl;
import br.com.wkallil.models.Books;
import br.com.wkallil.unittests.mapper.mocks.MockBooks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BooksMapperImpl.class})
public class BooksMapperTest {

    @Autowired
    BooksMapper mapper;

    MockBooks inputObject;

    @BeforeEach
    void setUp() {
        inputObject = new MockBooks();
    }

    @Test
    void testConvertEntityToDto() {
        Books entity = inputObject.mockEntity();
        BooksDTO result = mapper.toDto(entity);
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getAuthor(), result.getAuthor());
        assertEquals(entity.getTitle(), result.getTitle());
        assertEquals(entity.getLaunchDate(), result.getLaunchDate());
        assertEquals(entity.getPrice(), result.getPrice());
    }

    @Test
    void testConvertDtoToEntity() {
        BooksDTO dto = inputObject.mockDTO();
        Books result = mapper.toEntity(dto);
        
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getAuthor(), result.getAuthor());
        assertEquals(dto.getTitle(), result.getTitle());
        assertEquals(dto.getLaunchDate(), result.getLaunchDate());
        assertEquals(dto.getPrice(), result.getPrice());
    }

    @Test
    void testConvertEntityListToDtoList() {
        List<Books> entityList = inputObject.mockEntityList();
        List<BooksDTO> result = mapper.toDtoList(entityList);
        
        assertNotNull(result);
        assertEquals(14, result.size());
        
        BooksDTO targetDto = result.getFirst();
        
        assertNotNull(targetDto);
        assertEquals(entityList.getFirst().getId(), targetDto.getId());
        assertEquals(entityList.getFirst().getAuthor(), targetDto.getAuthor());
        assertEquals(entityList.getFirst().getTitle(), targetDto.getTitle());
        assertEquals(entityList.getFirst().getLaunchDate(), targetDto.getLaunchDate());
        assertEquals(entityList.getFirst().getPrice(), targetDto.getPrice());
    }

    @Test
    void testConvertDtoListToEntityList() {
        List<BooksDTO> dtoList = inputObject.mockDTOList();
        List<Books> result = mapper.toEntityList(dtoList);
        
        assertNotNull(result);
        assertEquals(14, result.size());
        
        Books targetEntity = result.getFirst();
        
        assertNotNull(targetEntity);
        assertEquals(dtoList.getFirst().getId(), targetEntity.getId());
        assertEquals(dtoList.getFirst().getAuthor(), targetEntity.getAuthor());
        assertEquals(dtoList.getFirst().getTitle(), targetEntity.getTitle());
        assertEquals(dtoList.getFirst().getLaunchDate(), targetEntity.getLaunchDate());
        assertEquals(dtoList.getFirst().getPrice(), targetEntity.getPrice());
    }
}
