package edu.mor.libraryindex.service;

import edu.mor.libraryindex.dto.BookCreateDto;
import edu.mor.libraryindex.model.Book;
import edu.mor.libraryindex.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll() {
        return this.bookRepository.findAll();
    }

    public Book findById(String id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with specified id not found!");
        }
        return book.get();
    }

    public Book create(BookCreateDto dto) {
        Book book = new Book(dto.title(), dto.description(), dto.authors(), dto.publishedAt());
        return bookRepository.save(book);
    }

    public Book update(String id, BookCreateDto dto) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with specified id not found!");
        }
        Book book = existingBook.get();
        book.setTitle(dto.title());
        book.setDescription(dto.description());
        book.setAuthors(dto.authors());
        book.setPublishedAt(dto.publishedAt());
        return bookRepository.save(book);
    }

    public String deleteById(String id) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with specified id not found!");
        }
        bookRepository.deleteById(id);
        return id;
    }

    public List<Book> findByTitle(String title) {
        return this.bookRepository.findByTitle(title);
    }

    public List<Book> findByAuthors(String author) {
        return this.bookRepository.findByAuthors(author);
    }

    public List<Book> findByPublishedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return this.bookRepository.findByPublishedAtBetween(startDate, endDate);
    }

    public List<Book> findByCreatedBefore(LocalDateTime date) {
        return this.bookRepository.findByCreatedAtBefore(date);
    }

    public List<Book> findByDescriptionContaining(String keyword) {
        return this.bookRepository.findByDescriptionContainingIgnoreCase(keyword);
    }

    public List<Book> findByIds(List<String> ids) {
        return this.bookRepository.findByIdIn(ids);
    }
}
