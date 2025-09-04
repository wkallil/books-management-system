package br.com.wkallil.file.exporter.impl;


import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.file.exporter.contract.FileExporter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements FileExporter {
    @Override
    public Resource exporterFile(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if (inputStream == null) {
            throw new RuntimeException("template not found: /templates/people.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters ,dataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}
