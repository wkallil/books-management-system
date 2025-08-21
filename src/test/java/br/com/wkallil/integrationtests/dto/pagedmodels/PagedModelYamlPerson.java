package br.com.wkallil.integrationtests.dto.pagedmodels;

import br.com.wkallil.integrationtests.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class PagedModelYamlPerson implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("content")
    private List<PersonDTO> content;


    public PagedModelYamlPerson() {}

    public List<PersonDTO> getContent() {
        return content;
    }

    public void setContent(List<PersonDTO> content) {
        this.content = content;
    }
}
