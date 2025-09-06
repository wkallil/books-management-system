package br.com.wkallil.file.exporter.impl;

import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.file.exporter.contract.PersonExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvExporter implements PersonExporter {
    @Override
    public Resource exporterPeople(List<PersonDTO> people) throws Exception {
        ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.DEFAULT
                .builder()
                .setHeader("ID", "FIRST NAME", "LAST NAME", "ADDRESS", "GENDER", "ENABLED")
                .setSkipHeaderRecord(false)
                .get();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)){
            for (PersonDTO person : people) {
                csvPrinter.printRecord(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getGender(),
                        person.getEnabled()
                );
            }
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

    @Override
    public Resource exporterPerson(PersonDTO person) throws Exception {
        return null;
    }
}
