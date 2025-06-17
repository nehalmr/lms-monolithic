package com.library.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.entity.Book;
import com.library.repository.BookRepository;
import com.library.testdata.BookTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Book Integration Tests")
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create, retrieve, update, and delete book successfully")
    void shouldPerformFullCrudOperationsSuccessfully() throws Exception {
        // Create a book
        Book bookToCreate = BookTestDataBuilder.aBook()
                .withId(null)
                .withTitle("Integration Test Book")
                .withAuthor("Integration Author")
                .withIsbn("978-1234567890")
                .build();

        String createResponse = mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Integration Test Book")))
                .andExpect(jsonPath("$.author", is("Integration Author")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book createdBook = objectMapper.readValue(createResponse, Book.class);
        Long bookId = createdBook.getBookId();

        // Retrieve the book
        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Integration Test Book")))
                .andExpect(jsonPath("$.author", is("Integration Author")));

        // Update the book
        createdBook.setTitle("Updated Integration Test Book");
        mockMvc.perform(put("/api/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Integration Test Book")));

        // Delete the book
        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNoContent());

        // Verify book is deleted
        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should search books by keyword")
    void shouldSearchBooksByKeyword() throws Exception {
        // Create test books
        Book book1 = BookTestDataBuilder.aBook()
                .withId(null)
                .withTitle("Java Programming")
                .withAuthor("John Doe")
                .build();
        
        Book book2 = BookTestDataBuilder.aBook()
                .withId(null)
                .withTitle("Python Programming")
                .withAuthor("Jane Smith")
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        // Search by title keyword
        mockMvc.perform(get("/api/books/search")
                .param("keyword", "Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Java Programming")));

        // Search by author keyword
        mockMvc.perform(get("/api/books/search")
                .param("keyword", "Jane"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is("Jane Smith")));
    }
}
