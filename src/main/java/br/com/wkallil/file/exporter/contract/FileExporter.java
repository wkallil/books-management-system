package br.com.wkallil.file.exporter.contract;

import br.com.wkallil.data.dto.v1.PersonDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface FileExporter {

    Resource exporterFile(List<PersonDTO> people) throws Exception;
    Resource exporterPerson(PersonDTO person) throws Exception;
}
