package edu.mor.libraryindex.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String id;
    private String title;
    private String description;
    private List<String> authors;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;

    public Book(String title, String description, List<String> authors, LocalDateTime publishedAt) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.publishedAt = publishedAt.withNano(0);
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, Objects.hash(authors), publishedAt, createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
            Objects.equals(title, book.title) &&
            Objects.equals(description, book.description) &&
            Objects.equals(authors, book.authors) &&
            Objects.equals(publishedAt, book.publishedAt) &&
            Objects.equals(createdAt, book.createdAt);
    }
}
