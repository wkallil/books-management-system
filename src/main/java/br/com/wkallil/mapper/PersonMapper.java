package br.com.wkallil.mapper;

import br.com.wkallil.data.dto.PersonDTO;
import br.com.wkallil.models.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDTO toDto(Person person);

    Person toEntity(PersonDTO personDTO);

    List<PersonDTO> toDtoList(List<Person> peopleList);

    List<Person> toEntityList(List<PersonDTO> peopleDTOList);

}
