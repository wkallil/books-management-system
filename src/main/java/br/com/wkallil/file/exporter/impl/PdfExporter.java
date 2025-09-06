package br.com.wkallil.file.exporter.impl;


import br.com.wkallil.data.dto.v1.PersonDTO;
import br.com.wkallil.file.exporter.contract.FileExporter;
import br.com.wkallil.services.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Component
public class PdfExporter implements FileExporter {

    @Autowired
    private QRCodeService service;

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

    @Override
    public Resource exporterPerson(PersonDTO person) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if (mainTemplateStream == null) {
            throw new RuntimeException("template not found: /templates/person.jrxml");
        }
        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if (subReportStream == null) {
            throw new RuntimeException("template not found: /templates/books.jrxml");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        InputStream qrCodeStream = service.generateQRCode(person.getProfileUrl(), 200, 200);

        JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(person.getBooks());

        String path = getClass().getResource("/templates/books.jasper").getPath();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUB_REPORT_DATA_SOURCE", subDataSource);
        parameters.put("SUB_REPORT_DIR", path);
        parameters.put("BOOK_SUB_REPORT", subReport);
        parameters.put("QR_CODEIMAGE", qrCodeStream);

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters ,mainDataSource);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        }
    }
}
