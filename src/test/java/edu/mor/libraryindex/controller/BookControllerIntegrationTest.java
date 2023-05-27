package edu.mor.libraryindex.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mor.libraryindex.dto.BookCreateDto;
import edu.mor.libraryindex.model.Book;
import edu.mor.libraryindex.repository.BookRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void givenThatBooksExist_whenRequestAllBooks_thenReturnBooks() throws Exception {
        Book book1 = new Book("Title 1", "Description 1", Arrays.asList("Author 1"), LocalDateTime.now());
        Book book2 = new Book("Title 2", "Description 2", Arrays.asList("Author 2"), LocalDateTime.now());
        bookRepository.saveAll(Arrays.asList(book1, book2));

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/books/"))
            .andExpect(status().isOk())
            .andReturn();

        List<Book> books = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Book>>() {
        });

        assertEquals(2, books.size());
        assertEquals(book1, books.get(0));
        assertEquals(book2, books.get(1));
    }

    @Test
    void givenThatBookExists_whenRequestBookById_thenReturnBook() throws Exception {
        Book book = new Book("Title", "Description", Arrays.asList("Author"), LocalDateTime.now());
        book = bookRepository.save(book);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/books/{id}", book.getId()))
            .andExpect(status().isOk())
            .andReturn();

        Book actualBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(book, actualBook);
    }

    @Test
    void givenThatBookDoesNotExist_whenRequestBookById_thenReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/{id}", "nonExistingId"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void whenRequestCreateBook_thenReturnCreatedBook() throws Exception {
        BookCreateDto bookCreateDto = new BookCreateDto("Title", "Description", Arrays.asList("Author"), LocalDateTime.now().withNano(0));

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/books/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookCreateDto)))
            .andExpect(status().isCreated())
            .andReturn();

        Book actualBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);

        assertNotNull(actualBook.getId());
        assertEquals(bookCreateDto.title(), actualBook.getTitle());
        assertEquals(bookCreateDto.description(), actualBook.getDescription());
        assertEquals(bookCreateDto.authors(), actualBook.getAuthors());
        assertEquals(bookCreateDto.publishedAt(), actualBook.getPublishedAt());
    }

    @Test
    void givenThatBookExists_whenRequestUpdateBookById_thenReturnUpdatedBook() throws Exception {
        Book book = new Book("Title", "Description", Arrays.asList("Author"), LocalDateTime.now());
        book = bookRepository.save(book);

        BookCreateDto updatedBook = new BookCreateDto("Updated Title", "Updated Description", Arrays.asList("Updated Author"), LocalDateTime.now());

        MvcResult mvcResult = mockMvc.perform(put("/api/v1/books/{id}", book.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(status().isOk())
            .andReturn();

        Book actualBook = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Book.class);

        assertEquals(book.getId(), actualBook.getId());
        assertEquals(updatedBook.title(), actualBook.getTitle());
        assertEquals(updatedBook.description(), actualBook.getDescription());
        assertEquals(updatedBook.authors(), actualBook.getAuthors());
        assertEquals(updatedBook.publishedAt(), actualBook.getPublishedAt());
    }


    @Test
    void givenThatBookDoesNotExist_whenRequestUpdateBookById_thenReturnNotFound() throws Exception {
        Book book = new Book("Title", "Description", Arrays.asList("Author"), LocalDateTime.now());
        bookRepository.save(book);

        BookCreateDto updatedBook = new BookCreateDto("Updated Title", "Updated Description", Arrays.asList("Updated Author"), LocalDateTime.now());


        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/{id}", "nonExistingId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenThatBookExists_whenRequestDeleteBookById_thenReturnNoContentResponse() throws Exception {
        Book book = new Book("Title", "Description", Arrays.asList("Author"), LocalDateTime.now());
        book = bookRepository.save(book);

        mockMvc.perform(delete("/api/v1/books/{id}", book.getId()))
            .andExpect(status().isNoContent());
    }


    @Test
    void givenThatBookDoesNotExist_whenRequestDeleteBookById_thenReturnNotFound() throws Exception {
        Book book = new Book("Title", "Description", Arrays.asList("Author"), LocalDateTime.now());
        bookRepository.save(book);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/{id}", "nonExistingId"))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}