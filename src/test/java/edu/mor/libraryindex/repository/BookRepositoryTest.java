package edu.mor.libraryindex.repository;

import edu.mor.libraryindex.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Test
    public void findByTitleShouldReturnFoundBooksByTitles() {
        Book book1 = new Book("Java for Beginners", "A beginner's guide to Java programming",
            Arrays.asList("John Doe"), LocalDateTime.now());
        Book book2 = new Book("Java for Experts", "An expert's guide to Java programming",
            Arrays.asList("John Doe", "Jane Smith"), LocalDateTime.now());
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<Book> books = bookRepository.findByTitle("Java for Beginners");
        Assertions.assertEquals(1, books.size());
        Assertions.assertEquals("Java for Beginners", books.get(0).getTitle());
    }

    @Test
    public void findByAuthorsShouldReturnFoundBooksByAuthors() {
        Book book1 = new Book("Java for Beginners", "A beginner's guide to Java programming",
            Arrays.asList("John Doe"), LocalDateTime.now());
        Book book2 = new Book("Java for Experts", "An expert's guide to Java programming",
            Arrays.asList("John Doe", "Jane Smith"), LocalDateTime.now());
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<Book> books = bookRepository.findByAuthors("John Doe");
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().allMatch(book -> book.getAuthors().contains("John Doe")));
    }

    @Test
    public void findByPublishedAtBetweenShouldReturnFoundBooksByPublishedAtBetweenTwoDates() {
        LocalDateTime startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 12, 31, 23, 59);
        Book book1 = new Book("Java for Beginners", "A beginner's guide to Java programming",
            Arrays.asList("John Doe"), LocalDateTime.of(2022, 3, 1, 0, 0));
        Book book2 = new Book("Java for Experts", "An expert's guide to Java programming",
            Arrays.asList("John Doe", "Jane Smith"), LocalDateTime.of(2022, 5, 1, 0, 0));
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<Book> books = bookRepository.findByPublishedAtBetween(startDate, endDate);
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().allMatch(book -> book.getPublishedAt().isAfter(startDate)
            && book.getPublishedAt().isBefore(endDate)));
    }

    @Test
    public void findByCreatedAtBeforeShouldReturnFoundBooksByCreatedAtBeforeCertainDate() {
        LocalDateTime date = LocalDateTime.now();
        Book book1 = new Book("1", "Java for Beginners", "A beginner's guide to Java programming",
            Arrays.asList("John Doe"), LocalDateTime.now(), LocalDateTime.of(2022, 3, 1, 0, 0));
        Book book2 = new Book("2", "Java for Experts", "An expert's guide to Java programming",
            Arrays.asList("John Doe", "Jane Smith"), LocalDateTime.now(), LocalDateTime.of(2022, 5, 1, 0, 0));
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<Book> books = bookRepository.findByCreatedAtBefore(date);
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().allMatch(book -> book.getCreatedAt().isBefore(date)));
    }

    @Test
    public void testFindByDescriptionShouldReturnFoundBooksWhichDescriptionContainSomeString() {
        Book book1 = new Book("1", "Java for Beginners", "A beginner's guide to Java programming",
            Arrays.asList("John Doe"), LocalDateTime.now(), LocalDateTime.now());
        Book book2 = new Book("2", "Java for Experts", "An expert's guide to Java programming",
            Arrays.asList("John Doe", "Jane Smith"), LocalDateTime.now(), LocalDateTime.now());
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<Book> books = bookRepository.findByDescriptionContainingIgnoreCase("programming");
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().allMatch(book -> book.getDescription().toLowerCase().contains("programming")));
    }

    @Test
    public void findByIdInShouldReturnFoundBooksWhichIdIsInProvidedList() {
        Book book1 = new Book("1", "Java for Beginners", "A beginner's guide to Java programming",
            Arrays.asList("John Doe"), LocalDateTime.now(), LocalDateTime.now());
        Book book2 = new Book("2", "Java for Experts", "An expert's guide to Java programming",
            Arrays.asList("John Doe", "Jane Smith"), LocalDateTime.now(), LocalDateTime.now());
        bookRepository.saveAll(Arrays.asList(book1, book2));

        List<Book> books = bookRepository.findByIdIn(Arrays.asList("1", "2"));
        Assertions.assertEquals(2, books.size());
        Assertions.assertTrue(books.stream().allMatch(book -> Arrays.asList("1", "2").contains(book.getId())));
    }
}