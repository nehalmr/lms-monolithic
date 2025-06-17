package com.library.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Schema(description = "Book entity representing a book in the library")
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Schema(description = "Unique identifier of the book", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    
    @Schema(description = "Title of the book", example = "The Great Gatsby", required = true)
    @Column(nullable = false)
    private String title;
    
    @Schema(description = "Author of the book", example = "F. Scott Fitzgerald", required = true)
    @Column(nullable = false)
    private String author;
    
    @Schema(description = "Genre of the book", example = "Fiction")
    private String genre;
    
    @Schema(description = "ISBN number of the book", example = "978-0-7432-7356-5")
    @Column(unique = true)
    private String isbn;
    
    @Schema(description = "Year the book was published", example = "1925")
    private Integer yearPublished;
    
    @Schema(description = "Number of copies currently available for borrowing", example = "3", required = true)
    @Column(nullable = false)
    private Integer availableCopies;
    
    @Schema(description = "Total number of copies owned by the library", example = "5", required = true)
    @Column(nullable = false)
    private Integer totalCopies;
}
