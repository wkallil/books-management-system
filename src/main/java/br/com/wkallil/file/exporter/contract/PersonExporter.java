package br.com.wkallil.file.exporter.contract;

import br.com.wkallil.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PersonExporter {

    Resource exporterPeople(List<PersonDTO> people) throws Exception;
    Resource exporterPerson(PersonDTO person) throws Exception;
}
