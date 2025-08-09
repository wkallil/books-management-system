package br.com.wkallil.mapper;

import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.models.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "enabled", source = "enabled")
    PersonDTO toDto(Person person);

    @Mapping(target = "enabled", source = "enabled")
    Person toEntity(PersonDTO personDTO);

    List<PersonDTO> toDtoList(List<Person> peopleList);

    List<Person> toEntityList(List<PersonDTO> peopleDTOList);
}
