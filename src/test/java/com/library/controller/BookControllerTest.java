package com.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.Book;
import com.library.service.BookService;
import com.library.testdata.BookTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@DisplayName("BookController Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = BookTestDataBuilder.aBook()
                .withId(1L)
                .withTitle("Test Book")
                .withAuthor("Test Author")
                .withGenre("Fiction")
                .withIsbn("978-0123456789")
                .withYearPublished(2024)
                .withAvailableCopies(5)
                .withTotalCopies(5)
                .build();
    }

    @Nested
    @DisplayName("GET /api/books")
    class GetAllBooksTests {

        @Test
        @DisplayName("Should return all books with 200 status")
        void shouldReturnAllBooksWithOkStatus() throws Exception {
            // Given
            List<Book> books = Arrays.asList(testBook);
            when(bookService.getAllBooks()).thenReturn(books);

            // When & Then
            mockMvc.perform(get("/api/books"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].bookId", is(1)))
                    .andExpect(jsonPath("$[0].title", is("Test Book")))
                    .andExpect(jsonPath("$[0].author", is("Test Author")))
                    .andExpect(jsonPath("$[0].genre", is("Fiction")))
                    .andExpect(jsonPath("$[0].isbn", is("978-0123456789")))
                    .andExpect(jsonPath("$[0].yearPublished", is(2024)))
                    .andExpect(jsonPath("$[0].availableCopies", is(5)))
                    .andExpect(jsonPath("$[0].totalCopies", is(5)));

            verify(bookService).getAllBooks();
        }

        @Test
        @DisplayName("Should return empty array when no books exist")
        void shouldReturnEmptyArrayWhenNoBooksExist() throws Exception {
            // Given
            when(bookService.getAllBooks()).thenReturn(Arrays.asList());

            // When & Then
            mockMvc.perform(get("/api/books"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(bookService).getAllBooks();
        }
    }

    @Nested
    @DisplayName("GET /api/books/{id}")
    class GetBookByIdTests {

        @Test
        @DisplayName("Should return book when book exists")
        void shouldReturnBookWhenBookExists() throws Exception {
            // Given
            when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

            // When & Then
            mockMvc.perform(get("/api/books/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.bookId", is(1)))
                    .andExpect(jsonPath("$.title", is("Test Book")));

            verify(bookService).getBookById(1L);
        }

        @Test
        @DisplayName("Should return 404 when book does not exist")
        void shouldReturn404WhenBookDoesNotExist() throws Exception {
            // Given
            when(bookService.getBookById(999L)).thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get("/api/books/999"))
                    .andExpect(status().isNotFound());

            verify(bookService).getBookById(999L);
        }
    }

    @Nested
    @DisplayName("POST /api/books")
    class CreateBookTests {

        @Test
        @DisplayName("Should create book and return 201 status")
        void shouldCreateBookAndReturn201Status() throws Exception {
            // Given
            Book bookToCreate = BookTestDataBuilder.aBook()
                    .withId(null)
                    .withTitle("New Book")
                    .build();
            Book createdBook = BookTestDataBuilder.aBook()
                    .withId(2L)
                    .withTitle("New Book")
                    .build();

            when(bookService.saveBook(any(Book.class))).thenReturn(createdBook);

            // When & Then
            mockMvc.perform(post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bookToCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.bookId", is(2)))
                    .andExpect(jsonPath("$.title", is("New Book")));

            verify(bookService).saveBook(any(Book.class));
        }

        @Test
        @DisplayName("Should return 400 when book data is invalid")
        void shouldReturn400WhenBookDataIsInvalid() throws Exception {
            // Given
            String invalidBookJson = "{}";

            // When & Then
            mockMvc.perform(post("/api/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidBookJson))
                    .andExpect(status().isBadRequest());

            verify(bookService, never()).saveBook(any());
        }
    }

    @Nested
    @DisplayName("PUT /api/books/{id}")
    class UpdateBookTests {

        @Test
        @DisplayName("Should update book when book exists")
        void shouldUpdateBookWhenBookExists() throws Exception {
            // Given
            Book updatedBook = BookTestDataBuilder.aBook()
                    .withId(1L)
                    .withTitle("Updated Book")
                    .build();

            when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));
            when(bookService.saveBook(any(Book.class))).thenReturn(updatedBook);

            // When & Then
            mockMvc.perform(put("/api/books/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatedBook)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title", is("Updated Book")));

            verify(bookService).getBookById(1L);
            verify(bookService).saveBook(any(Book.class));
        }

        @Test
        @DisplayName("Should return 404 when book does not exist")
        void shouldReturn404WhenBookDoesNotExist() throws Exception {
            // Given
            when(bookService.getBookById(999L)).thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(put("/api/books/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testBook)))
                    .andExpect(status().isNotFound());

            verify(bookService).getBookById(999L);
            verify(bookService, never()).saveBook(any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/books/{id}")
    class DeleteBookTests {

        @Test
        @DisplayName("Should delete book when book exists")
        void shouldDeleteBookWhenBookExists() throws Exception {
            // Given
            when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

            // When & Then
            mockMvc.perform(delete("/api/books/1"))
                    .andExpect(status().isNoContent());

            verify(bookService).getBookById(1L);
            verify(bookService).deleteBook(1L);
        }

        @Test
        @DisplayName("Should return 404 when book does not exist")
        void shouldReturn404WhenBookDoesNotExist() throws Exception {
            // Given
            when(bookService.getBookById(999L)).thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(delete("/api/books/999"))
                    .andExpect(status().isNotFound());

            verify(bookService).getBookById(999L);
            verify(bookService, never()).deleteBook(any());
        }
    }

    @Nested
    @DisplayName("GET /api/books/search")
    class SearchBooksTests {

        @Test
        @DisplayName("Should return matching books")
        void shouldReturnMatchingBooks() throws Exception {
            // Given
            String keyword = "Test";
            List<Book> matchingBooks = Arrays.asList(testBook);
            when(bookService.searchBooks(keyword)).thenReturn(matchingBooks);

            // When & Then
            mockMvc.perform(get("/api/books/search")
                    .param("keyword", keyword))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].title", is("Test Book")));

            verify(bookService).searchBooks(keyword);
        }
    }

    @Nested
    @DisplayName("GET /api/books/available")
    class GetAvailableBooksTests {

        @Test
        @DisplayName("Should return available books")
        void shouldReturnAvailableBooks() throws Exception {
            // Given
            List<Book> availableBooks = Arrays.asList(testBook);
            when(bookService.getAvailableBooks()).thenReturn(availableBooks);

            // When & Then
            mockMvc.perform(get("/api/books/available"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].availableCopies", is(5)));

            verify(bookService).getAvailableBooks();
        }
    }
}
