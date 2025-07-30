package br.com.wkallil.unittests.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.mapper.PersonMapper;
import br.com.wkallil.mapper.PersonMapperImpl;
import br.com.wkallil.models.Person;
import br.com.wkallil.unittests.mapper.mocks.MockPerson;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PersonMapperImpl.class})
public class PersonMapperTest {

    @Autowired
    PersonMapper mapper;

    MockPerson inputObject;

    @BeforeEach
    void setUp() {
        inputObject = new MockPerson();
    }

    @Test
    void testConvertEntityToDto() {
        Person entity = inputObject.mockEntity();
        PersonDTO result = mapper.toDto(entity);
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getFirstName(), result.getFirstName());
        assertEquals(entity.getLastName(), result.getLastName());
        assertEquals(entity.getAddress(), result.getAddress());
        assertEquals(entity.getGender(), result.getGender());
    }

    @Test
    void testConvertDtoToEntity() {
        PersonDTO dto = inputObject.mockDTO();
        Person result = mapper.toEntity(dto);
        
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getFirstName(), result.getFirstName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(dto.getGender(), result.getGender());
    }

    @Test
    void testConvertEntityListToDtoList() {
        List<Person> entityList = inputObject.mockEntityList();
        List<PersonDTO> result = mapper.toDtoList(entityList);
        
        assertNotNull(result);
        assertEquals(14, result.size());
        
        PersonDTO targetDto = result.getFirst();
        
        assertNotNull(targetDto);
        assertEquals(entityList.getFirst().getId(), targetDto.getId());
        assertEquals(entityList.getFirst().getFirstName(), targetDto.getFirstName());
        assertEquals(entityList.getFirst().getLastName(), targetDto.getLastName());
        assertEquals(entityList.getFirst().getAddress(), targetDto.getAddress());
        assertEquals(entityList.getFirst().getGender(), targetDto.getGender());
    }

    @Test
    void testConvertDtoListToEntityList() {
        List<PersonDTO> dtoList = inputObject.mockDTOList();
        List<Person> result = mapper.toEntityList(dtoList);
        
        assertNotNull(result);
        assertEquals(14, result.size());
        
        Person targetEntity = result.getFirst();
        
        assertNotNull(targetEntity);
        assertEquals(dtoList.getFirst().getId(), targetEntity.getId());
        assertEquals(dtoList.getFirst().getFirstName(), targetEntity.getFirstName());
        assertEquals(dtoList.getFirst().getLastName(), targetEntity.getLastName());
        assertEquals(dtoList.getFirst().getAddress(), targetEntity.getAddress());
        assertEquals(dtoList.getFirst().getGender(), targetEntity.getGender());
    }
}
