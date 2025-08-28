package br.com.wkallil.file.importer.factory;

import br.com.wkallil.exceptions.BadRequestException;
import br.com.wkallil.file.importer.contract.FileImporter;
import br.com.wkallil.file.importer.impl.CsvImporter;
import br.com.wkallil.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext context;

    public FileImporter getImporter(String fileName) throws BadRequestException {
        if (fileName.endsWith(".xlsx")) {
            logger.info("XLSX Importer selected");
            return context.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            logger.info("CSV Importer selected");
            return context.getBean(CsvImporter.class);
        } else {
            logger.error("Invalid File Extension");
            throw new BadRequestException("Invalid File Extension");
        }
    }
}
