package br.com.wkallil.file.exporter.factory;

import br.com.wkallil.exceptions.BadRequestException;
import br.com.wkallil.file.exporter.MediaTypes;
import br.com.wkallil.file.exporter.contract.FileExporter;
import br.com.wkallil.file.exporter.impl.CsvExporter;
import br.com.wkallil.file.exporter.impl.PdfExporter;
import br.com.wkallil.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileExporter getExporter(String acceptHeader) throws BadRequestException {
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            logger.info("XLSX Importer selected");
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            logger.info("CSV Importer selected");
            return context.getBean(CsvExporter.class);
        }  else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            logger.info("PDF Importer selected");
            return context.getBean(PdfExporter.class);
        } else {
            logger.error("Invalid File Extension");
            throw new BadRequestException("Invalid File Extension");
        }
    }
}
