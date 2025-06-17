package com.library.testdata;

import com.library.entity.Book;

public class BookTestDataBuilder {
    private Long bookId = 1L;
    private String title = "Test Book";
    private String author = "Test Author";
    private String genre = "Fiction";
    private String isbn = "978-0123456789";
    private Integer yearPublished = 2024;
    private Integer availableCopies = 5;
    private Integer totalCopies = 5;
    
    public static BookTestDataBuilder aBook() {
        return new BookTestDataBuilder();
    }
    
    public BookTestDataBuilder withId(Long bookId) {
        this.bookId = bookId;
        return this;
    }
    
    public BookTestDataBuilder withTitle(String title) {
        this.title = title;
        return this;
    }
    
    public BookTestDataBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }
    
    public BookTestDataBuilder withGenre(String genre) {
        this.genre = genre;
        return this;
    }
    
    public BookTestDataBuilder withIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }
    
    public BookTestDataBuilder withYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
        return this;
    }
    
    public BookTestDataBuilder withAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
        return this;
    }
    
    public BookTestDataBuilder withTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
        return this;
    }
    
    public BookTestDataBuilder withNoAvailableCopies() {
        this.availableCopies = 0;
        return this;
    }
    
    public Book build() {
        return new Book(bookId, title, author, genre, isbn, yearPublished, availableCopies, totalCopies);
    }
}
