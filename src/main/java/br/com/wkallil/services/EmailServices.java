package br.com.wkallil.services;

import br.com.wkallil.config.EmailConfig;
import br.com.wkallil.data.dto.v1.request.EmailRequestDTO;
import br.com.wkallil.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service

public class EmailServices {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimpleEmail(EmailRequestDTO emailRequestDTO) {
        emailSender.to(emailRequestDTO.getTo())
                .withSubject(emailRequestDTO.getSubject())
                .withMessage(emailRequestDTO.getBody())
                .send(emailConfigs);
    }

    public void sendEmailWithAttachment(String emailRequest, MultipartFile attachment) {
        File tempFile = null;
        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequest, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);

            emailSender.to(emailRequestDTO.getTo())
                    .withSubject(emailRequestDTO.getSubject())
                    .withMessage(emailRequestDTO.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfigs);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request JSON!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment!", e);
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

}
