package edu.mor.libraryindex.controller;

import edu.mor.libraryindex.dto.BookCreateDto;
import edu.mor.libraryindex.model.Book;
import edu.mor.libraryindex.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public List<Book> getAll() {
        return this.bookService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable String id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/")
    public ResponseEntity<Book> create(@RequestBody BookCreateDto book) {
        Book savedBook = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable String id, @RequestBody BookCreateDto book) {
        Book updatedBook = bookService.update(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
