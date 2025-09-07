package br.com.wkallil.constrollers;


import br.com.wkallil.constrollers.docs.EmailControllerDocs;
import br.com.wkallil.data.dto.v1.request.EmailRequestDTO;
import br.com.wkallil.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService service;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        service.sendSimpleEmail(emailRequestDTO);
        return new ResponseEntity<>("e-mail sent with success!", HttpStatus.OK);
    }


    @PostMapping(value = "/attachment")
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(
            @RequestParam("emailRequest") String emailRequest,
            @RequestParam("attachment") MultipartFile attachment) {
        service.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("e-mail with attachment sent successfully!", HttpStatus.OK);
    }
}
