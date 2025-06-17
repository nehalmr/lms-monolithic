package com.library.repository;

import com.library.entity.Book;
import com.library.testdata.BookTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("BookRepository Tests")
class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;

    @BeforeEach
    void setUp() {
        testBook1 = BookTestDataBuilder.aBook()
                .withId(null)
                .withTitle("Java Programming")
                .withAuthor("John Doe")
                .withGenre("Programming")
                .withAvailableCopies(5)
                .build();

        testBook2 = BookTestDataBuilder.aBook()
                .withId(null)
                .withTitle("Python Programming")
                .withAuthor("Jane Smith")
                .withGenre("Programming")
                .withAvailableCopies(0)
                .build();

        entityManager.persistAndFlush(testBook1);
        entityManager.persistAndFlush(testBook2);
    }

    @Test
    @DisplayName("Should find books by title containing keyword ignoring case")
    void shouldFindBooksByTitleContainingKeywordIgnoringCase() {
        // When
        List<Book> result = bookRepository.findByTitleContainingIgnoreCase("java");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
    }

    @Test
    @DisplayName("Should find books by author containing keyword ignoring case")
    void shouldFindBooksByAuthorContainingKeywordIgnoringCase() {
        // When
        List<Book> result = bookRepository.findByAuthorContainingIgnoreCase("jane");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("Should find books by genre containing keyword ignoring case")
    void shouldFindBooksByGenreContainingKeywordIgnoringCase() {
        // When
        List<Book> result = bookRepository.findByGenreContainingIgnoreCase("programming");

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Should find books with available copies greater than specified number")
    void shouldFindBooksWithAvailableCopiesGreaterThanSpecifiedNumber() {
        // When
        List<Book> result = bookRepository.findByAvailableCopiesGreaterThan(0);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
    }

    @Test
    @DisplayName("Should search books by keyword in title, author, or genre")
    void shouldSearchBooksByKeywordInTitleAuthorOrGenre() {
        // When
        List<Book> result = bookRepository.searchBooks("java");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Java Programming");
    }
}
