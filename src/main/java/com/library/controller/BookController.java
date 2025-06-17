package com.library.controller;

import com.library.entity.Book;
import com.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Books", description = "Book management APIs")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @Operation(
        summary = "Retrieve all books",
        description = "Get a list of all books in the library system with their availability status"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all books"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }
    
    @Operation(
        summary = "Retrieve a book by ID",
        description = "Get a specific book by its unique identifier"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the book"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "400", description = "Invalid book ID supplied")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(
        @Parameter(description = "ID of the book to retrieve", required = true, example = "1")
        @PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(
        summary = "Create a new book",
        description = "Add a new book to the library collection"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Book created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid book data provided"),
        @ApiResponse(responseCode = "409", description = "Book with same ISBN already exists")
    })
    @PostMapping
    public ResponseEntity<Book> createBook(
        @Parameter(description = "Book object to be created", required = true)
        @RequestBody Book book) {
        Book createdBook = bookService.saveBook(book);
        return ResponseEntity.status(201).body(createdBook);
    }
    
    @Operation(
        summary = "Update an existing book",
        description = "Update book information by ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book updated successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "400", description = "Invalid book data provided")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
        @Parameter(description = "ID of the book to update", required = true, example = "1")
        @PathVariable Long id,
        @Parameter(description = "Updated book object", required = true)
        @RequestBody Book book) {
        if (!bookService.getBookById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        book.setBookId(id);
        return ResponseEntity.ok(bookService.saveBook(book));
    }
    
    @Operation(
        summary = "Delete a book",
        description = "Remove a book from the library collection"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete book with active borrowings")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(
        @Parameter(description = "ID of the book to delete", required = true, example = "1")
        @PathVariable Long id) {
        if (!bookService.getBookById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(
        summary = "Search books",
        description = "Search books by title, author, or genre using keyword"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search completed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping("/search")
    public List<Book> searchBooks(
        @Parameter(description = "Search keyword for title, author, or genre", required = true, example = "Harry Potter")
        @RequestParam String keyword) {
        return bookService.searchBooks(keyword);
    }
    
    @Operation(
        summary = "Get available books",
        description = "Retrieve all books that are currently available for borrowing"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved available books")
    })
    @GetMapping("/available")
    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }
}
