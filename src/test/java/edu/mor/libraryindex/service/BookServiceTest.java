package edu.mor.libraryindex.service;

import edu.mor.libraryindex.dto.BookCreateDto;
import edu.mor.libraryindex.model.Book;
import edu.mor.libraryindex.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    @Mock
    private BookRepository mockBookRepository;

    private BookService bookService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(mockBookRepository);
    }

    @Test
    void getAllShouldReturnAllBookIfBooksExist() {
        // given
        Book book1 = new Book("title1", "desc1", Arrays.asList("author1"), LocalDateTime.now());
        Book book2 = new Book("title2", "desc2", Arrays.asList("author2"), LocalDateTime.now());
        List<Book> books = Arrays.asList(book1, book2);

        when(mockBookRepository.findAll()).thenReturn(books);

        // when
        List<Book> result = bookService.getAll();

        // then
        assertEquals(2, result.size());
        assertEquals("title1", result.get(0).getTitle());
        assertEquals("title2", result.get(1).getTitle());
    }

    @Test
    void findByIdShouldReturnBookWhenBookExists() {
        // given
        Book book = new Book("title", "desc", Arrays.asList("author"), LocalDateTime.now());
        when(mockBookRepository.findById("1")).thenReturn(Optional.of(book));

        // when
        Book result = bookService.findById("1");

        // then
        assertNotNull(result);
        assertEquals("title", result.getTitle());
        assertEquals("desc", result.getDescription());
        assertEquals(Arrays.asList("author"), result.getAuthors());
    }

    @Test
    void findByIdShouldReturnThrowNotFoundWhenBookDoesNotExist() {
        // given
        when(mockBookRepository.findById("1")).thenReturn(Optional.empty());

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> bookService.findById("1"));

        // then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void createShouldReturnNewBook() {
        // given
        BookCreateDto dto = new BookCreateDto("title", "desc", Arrays.asList("author"), LocalDateTime.now());
        Book book = new Book("title", "desc", Arrays.asList("author"), LocalDateTime.now());
        when(mockBookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(book);

        // when
        Book result = bookService.create(dto);

        // then
        assertNotNull(result);
        assertEquals("title", result.getTitle());
        assertEquals("desc", result.getDescription());
        assertEquals(Arrays.asList("author"), result.getAuthors());
    }

    @Test
    void updateShouldReturnUpdatedBookWhenBookExists() {
        // given
        BookCreateDto dto = new BookCreateDto("title2", "desc2", Arrays.asList("author2"), LocalDateTime.now());
        Book existingBook = new Book("title1", "desc1", Arrays.asList("author1"), LocalDateTime.now());
        when(mockBookRepository.findById("1")).thenReturn(Optional.of(existingBook));
        when(mockBookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(existingBook);

        // when
        Book result = bookService.update("1", dto);

        // then
        assertNotNull(result);
        assertEquals("title2", result.getTitle());
        assertEquals("desc2", result.getDescription());
        assertEquals(Arrays.asList("author2"), result.getAuthors());
    }

    @Test
    void updateShouldReturnNullWhenBookDoesNotExist() {
        // given
        BookCreateDto dto = new BookCreateDto("title", "desc", Arrays.asList("author"), LocalDateTime.now());
        when(mockBookRepository.findById("1")).thenReturn(Optional.empty());

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> bookService.update("1", dto));

        // then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void deleteById_shouldReturnId_whenBookExists() {
        // given
        when(mockBookRepository.findById("1")).thenReturn(Optional.of(new Book()));

        // when
        String result = bookService.deleteById("1");

        // then
        assertEquals("1", result);
        verify(mockBookRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteByIdShouldThrowNotFoundWhenBookDoesNotExist() {
        // given
        when(mockBookRepository.findById("1")).thenReturn(Optional.empty());

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> bookService.deleteById("1"));

        // then
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(mockBookRepository, never()).deleteById("1");
    }

    @Test
    void findByTitleShouldReturnListOfBooksWhenTitleExists() {
        // given
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(mockBookRepository.findByTitle("test title")).thenReturn(expectedBooks);

        // when
        List<Book> result = bookService.findByTitle("test title");

        // then
        assertEquals(expectedBooks, result);
    }

    @Test
    void findByTitleShouldReturnEmptyListWhenTitleDoesNotExist() {
        // given
        when(mockBookRepository.findByTitle("test title")).thenReturn(Collections.emptyList());

        // when
        List<Book> result = bookService.findByTitle("test title");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByAuthorsShouldReturnListOfBooksWhenAuthorExists() {
        // given
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(mockBookRepository.findByAuthors("test author")).thenReturn(expectedBooks);

        // when
        List<Book> result = bookService.findByAuthors("test author");

        // then
        assertEquals(expectedBooks, result);
    }

    @Test
    void findByAuthorsShouldReturnEmptyListWhenAuthorDoesNotExist() {
        // given
        when(mockBookRepository.findByAuthors("test author")).thenReturn(Collections.emptyList());

        // when
        List<Book> result = bookService.findByAuthors("test author");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublishedBetweenShouldReturnListOfBooksWhenBooksExist() {
        // given
        LocalDateTime startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 12, 31, 23, 59);
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(mockBookRepository.findByPublishedAtBetween(startDate, endDate)).thenReturn(expectedBooks);

        // when
        List<Book> result = bookService.findByPublishedBetween(startDate, endDate);

        // then
        assertEquals(expectedBooks, result);
    }

    @Test
    void findByPublishedBetweenShouldReturnEmptyListWhenBooksDoNotExist() {
        // given
        LocalDateTime startDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 12, 31, 23, 59);
        when(mockBookRepository.findByPublishedAtBetween(startDate, endDate)).thenReturn(Collections.emptyList());

        // when
        List<Book> result = bookService.findByPublishedBetween(startDate, endDate);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByCreatedBeforeShouldReturnListOfBooksWhenBooksExist() {
        // given
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 0, 0);
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(mockBookRepository.findByCreatedAtBefore(date)).thenReturn(expectedBooks);

        // when
        List<Book> result = bookService.findByCreatedBefore(date);

        // then
        assertEquals(expectedBooks, result);
    }

    @Test
    void findByCreatedBeforeShouldReturnEmptyListWhenBooksDoNotExist() {
        // given
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 0, 0);
        when(mockBookRepository.findByCreatedAtBefore(date)).thenReturn(Collections.emptyList());

        // when
        List<Book> result = bookService.findByCreatedBefore(date);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByDescriptionContainingShouldReturnListOfBooksWhenBooksExist() {
        // given
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(mockBookRepository.findByDescriptionContainingIgnoreCase("test description")).thenReturn(expectedBooks);

        // when
        List<Book> result = bookService.findByDescriptionContaining("test description");

        // then
        assertEquals(expectedBooks, result);
    }

    @Test
    void findByDescriptionContainingShouldReturnEmptyListWhenBooksDoNotExist() {
        // given
        when(mockBookRepository.findByDescriptionContainingIgnoreCase("test description")).thenReturn(Collections.emptyList());

        // when
        List<Book> result = bookService.findByDescriptionContaining("test description");

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdsShouldReturnListOfBooksWhenBooksExist() {
        // given
        List<Book> expectedBooks = Arrays.asList(new Book());
        when(mockBookRepository.findByIdIn(Arrays.asList("id1"))).thenReturn(expectedBooks);

        // when
        List<Book> result = bookService.findByIds(Arrays.asList("id1"));

        // then
        assertEquals(expectedBooks, result);
    }

    @Test
    void findByIdsShouldReturnEmptyListWhenBooksDoNotExist() {
        // given
        when(mockBookRepository.findByIdIn(Arrays.asList("id1"))).thenReturn(Collections.emptyList());

        // when
        List<Book> result = bookService.findByIds(Arrays.asList("id1"));

        // then
        assertTrue(result.isEmpty());
    }
}