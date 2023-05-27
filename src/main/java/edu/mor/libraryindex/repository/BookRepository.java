package edu.mor.libraryindex.repository;

import edu.mor.libraryindex.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitle(String title);
    List<Book> findByAuthors(String author);
    List<Book> findByPublishedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Book> findByCreatedAtBefore(LocalDateTime date);
    List<Book> findByDescriptionContainingIgnoreCase(String keyword);
    List<Book> findByIdIn(List<String> ids);
}
