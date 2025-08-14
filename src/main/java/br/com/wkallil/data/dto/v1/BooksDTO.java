package br.com.wkallil.data.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder({"id", "author", "title", "launch_date", "price"})
@Relation(collectionRelation = "books")
public class BooksDTO extends RepresentationModel<BooksDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String author;

    @JsonProperty("launch_date")
    private String launchDate;

    private String title;

    private Double price;

    public BooksDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String launchDate) {
        this.launchDate = launchDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BooksDTO books = (BooksDTO) o;
        return Objects.equals(getId(), books.getId()) && Objects.equals(getAuthor(), books.getAuthor()) && Objects.equals(getLaunchDate(), books.getLaunchDate()) && Objects.equals(getTitle(), books.getTitle()) && Objects.equals(getPrice(), books.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getAuthor(), getLaunchDate(), getTitle(), getPrice());
    }
}
