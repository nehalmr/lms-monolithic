package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import com.library.testdata.BookTestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = BookTestDataBuilder.aBook()
                .withId(1L)
                .withTitle("Test Book")
                .withAuthor("Test Author")
                .withAvailableCopies(5)
                .withTotalCopies(5)
                .build();
    }

    @Nested
    @DisplayName("Get All Books")
    class GetAllBooksTests {

        @Test
        @DisplayName("Should return all books when books exist")
        void shouldReturnAllBooksWhenBooksExist() {
            // Given
            List<Book> expectedBooks = Arrays.asList(testBook, 
                BookTestDataBuilder.aBook().withId(2L).withTitle("Another Book").build());
            when(bookRepository.findAll()).thenReturn(expectedBooks);

            // When
            List<Book> actualBooks = bookService.getAllBooks();

            // Then
            assertThat(actualBooks).hasSize(2);
            assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
            verify(bookRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no books exist")
        void shouldReturnEmptyListWhenNoBooksExist() {
            // Given
            when(bookRepository.findAll()).thenReturn(Arrays.asList());

            // When
            List<Book> actualBooks = bookService.getAllBooks();

            // Then
            assertThat(actualBooks).isEmpty();
            verify(bookRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Get Book By ID")
    class GetBookByIdTests {

        @Test
        @DisplayName("Should return book when book exists")
        void shouldReturnBookWhenBookExists() {
            // Given
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

            // When
            Optional<Book> result = bookService.getBookById(1L);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(testBook);
            verify(bookRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return empty when book does not exist")
        void shouldReturnEmptyWhenBookDoesNotExist() {
            // Given
            when(bookRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            Optional<Book> result = bookService.getBookById(999L);

            // Then
            assertThat(result).isEmpty();
            verify(bookRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Save Book")
    class SaveBookTests {

        @Test
        @DisplayName("Should save book with total copies when available copies not set")
        void shouldSaveBookWithTotalCopiesWhenAvailableCopiesNotSet() {
            // Given
            Book bookToSave = BookTestDataBuilder.aBook()
                    .withAvailableCopies(3)
                    .withTotalCopies(null)
                    .build();
            Book expectedSavedBook = BookTestDataBuilder.aBook()
                    .withAvailableCopies(3)
                    .withTotalCopies(3)
                    .build();
            
            when(bookRepository.save(any(Book.class))).thenReturn(expectedSavedBook);

            // When
            Book result = bookService.saveBook(bookToSave);

            // Then
            assertThat(result.getTotalCopies()).isEqualTo(3);
            verify(bookRepository).save(bookToSave);
        }

        @Test
        @DisplayName("Should save book without modifying total copies when already set")
        void shouldSaveBookWithoutModifyingTotalCopiesWhenAlreadySet() {
            // Given
            when(bookRepository.save(testBook)).thenReturn(testBook);

            // When
            Book result = bookService.saveBook(testBook);

            // Then
            assertThat(result.getTotalCopies()).isEqualTo(5);
            verify(bookRepository).save(testBook);
        }
    }

    @Nested
    @DisplayName("Search Books")
    class SearchBooksTests {

        @Test
        @DisplayName("Should return matching books when keyword matches")
        void shouldReturnMatchingBooksWhenKeywordMatches() {
            // Given
            String keyword = "Test";
            List<Book> expectedBooks = Arrays.asList(testBook);
            when(bookRepository.searchBooks(keyword)).thenReturn(expectedBooks);

            // When
            List<Book> result = bookService.searchBooks(keyword);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(testBook);
            verify(bookRepository).searchBooks(keyword);
        }

        @Test
        @DisplayName("Should return empty list when no books match keyword")
        void shouldReturnEmptyListWhenNoBooksMatchKeyword() {
            // Given
            String keyword = "NonExistent";
            when(bookRepository.searchBooks(keyword)).thenReturn(Arrays.asList());

            // When
            List<Book> result = bookService.searchBooks(keyword);

            // Then
            assertThat(result).isEmpty();
            verify(bookRepository).searchBooks(keyword);
        }
    }

    @Nested
    @DisplayName("Book Availability")
    class BookAvailabilityTests {

        @Test
        @DisplayName("Should return true when book is available")
        void shouldReturnTrueWhenBookIsAvailable() {
            // Given
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

            // When
            boolean result = bookService.isBookAvailable(1L);

            // Then
            assertThat(result).isTrue();
            verify(bookRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return false when book has no available copies")
        void shouldReturnFalseWhenBookHasNoAvailableCopies() {
            // Given
            Book unavailableBook = BookTestDataBuilder.aBook()
                    .withAvailableCopies(0)
                    .build();
            when(bookRepository.findById(1L)).thenReturn(Optional.of(unavailableBook));

            // When
            boolean result = bookService.isBookAvailable(1L);

            // Then
            assertThat(result).isFalse();
            verify(bookRepository).findById(1L);
        }

        @Test
        @DisplayName("Should return false when book does not exist")
        void shouldReturnFalseWhenBookDoesNotExist() {
            // Given
            when(bookRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            boolean result = bookService.isBookAvailable(999L);

            // Then
            assertThat(result).isFalse();
            verify(bookRepository).findById(999L);
        }
    }

    @Nested
    @DisplayName("Decrease Available Copies")
    class DecreaseAvailableCopiesTests {

        @Test
        @DisplayName("Should decrease available copies when book exists and has available copies")
        void shouldDecreaseAvailableCopiesWhenBookExistsAndHasAvailableCopies() {
            // Given
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

            // When
            bookService.decreaseAvailableCopies(1L);

            // Then
            assertThat(testBook.getAvailableCopies()).isEqualTo(4);
            verify(bookRepository).findById(1L);
            verify(bookRepository).save(testBook);
        }

        @Test
        @DisplayName("Should not decrease available copies when book has no available copies")
        void shouldNotDecreaseAvailableCopiesWhenBookHasNoAvailableCopies() {
            // Given
            Book unavailableBook = BookTestDataBuilder.aBook()
                    .withAvailableCopies(0)
                    .build();
            when(bookRepository.findById(1L)).thenReturn(Optional.of(unavailableBook));

            // When
            bookService.decreaseAvailableCopies(1L);

            // Then
            assertThat(unavailableBook.getAvailableCopies()).isEqualTo(0);
            verify(bookRepository).findById(1L);
            verify(bookRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should not decrease available copies when book does not exist")
        void shouldNotDecreaseAvailableCopiesWhenBookDoesNotExist() {
            // Given
            when(bookRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            bookService.decreaseAvailableCopies(999L);

            // Then
            verify(bookRepository).findById(999L);
            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Increase Available Copies")
    class IncreaseAvailableCopiesTests {

        @Test
        @DisplayName("Should increase available copies when book exists")
        void shouldIncreaseAvailableCopiesWhenBookExists() {
            // Given
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

            // When
            bookService.increaseAvailableCopies(1L);

            // Then
            assertThat(testBook.getAvailableCopies()).isEqualTo(6);
            verify(bookRepository).findById(1L);
            verify(bookRepository).save(testBook);
        }

        @Test
        @DisplayName("Should not increase available copies when book does not exist")
        void shouldNotIncreaseAvailableCopiesWhenBookDoesNotExist() {
            // Given
            when(bookRepository.findById(999L)).thenReturn(Optional.empty());

            // When
            bookService.increaseAvailableCopies(999L);

            // Then
            verify(bookRepository).findById(999L);
            verify(bookRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Delete Book")
    class DeleteBookTests {

        @Test
        @DisplayName("Should delete book when book exists")
        void shouldDeleteBookWhenBookExists() {
            // When
            bookService.deleteBook(1L);

            // Then
            verify(bookRepository).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Get Available Books")
    class GetAvailableBooksTests {

        @Test
        @DisplayName("Should return only available books")
        void shouldReturnOnlyAvailableBooks() {
            // Given
            List<Book> availableBooks = Arrays.asList(testBook);
            when(bookRepository.findByAvailableCopiesGreaterThan(0)).thenReturn(availableBooks);

            // When
            List<Book> result = bookService.getAvailableBooks();

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(testBook);
            verify(bookRepository).findByAvailableCopiesGreaterThan(0);
        }
    }
}
