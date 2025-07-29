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

import br.com.wkallil.data.dto.PersonDTO;
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
        
        PersonDTO targetDto = result.get(0);
        
        assertNotNull(targetDto);
        assertEquals(entityList.get(0).getId(), targetDto.getId());
        assertEquals(entityList.get(0).getFirstName(), targetDto.getFirstName());
        assertEquals(entityList.get(0).getLastName(), targetDto.getLastName());
        assertEquals(entityList.get(0).getAddress(), targetDto.getAddress());
        assertEquals(entityList.get(0).getGender(), targetDto.getGender());
    }

    @Test
    void testConvertDtoListToEntityList() {
        List<PersonDTO> dtoList = inputObject.mockDTOList();
        List<Person> result = mapper.toEntityList(dtoList);
        
        assertNotNull(result);
        assertEquals(14, result.size());
        
        Person targetEntity = result.get(0);
        
        assertNotNull(targetEntity);
        assertEquals(dtoList.get(0).getId(), targetEntity.getId());
        assertEquals(dtoList.get(0).getFirstName(), targetEntity.getFirstName());
        assertEquals(dtoList.get(0).getLastName(), targetEntity.getLastName());
        assertEquals(dtoList.get(0).getAddress(), targetEntity.getAddress());
        assertEquals(dtoList.get(0).getGender(), targetEntity.getGender());
    }
}
