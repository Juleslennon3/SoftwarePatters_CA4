package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/books")
@CrossOrigin // allows frontend (if any) to access this API
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // GET all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // GET a book by ID
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id);
    }

    // POST a new book
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // PUT update an existing book
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPublisher(updatedBook.getPublisher());
            book.setPrice(updatedBook.getPrice());
            book.setCategory(updatedBook.getCategory());
            book.setIsbn(updatedBook.getIsbn());
            book.setImageUrl(updatedBook.getImageUrl());
            return bookRepository.save(book);
        }).orElseGet(() -> {
            updatedBook.setId(id);
            return bookRepository.save(updatedBook);
        });
    }

    // DELETE a book
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

    // Search by title/category/author/publisher
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query, @RequestParam String type) {
        return switch (type.toLowerCase()) {
            case "title" -> bookRepository.findByTitleContainingIgnoreCase(query);
            case "category" -> bookRepository.findByCategoryContainingIgnoreCase(query);
            case "author" -> bookRepository.findByAuthorContainingIgnoreCase(query);
            case "publisher" -> bookRepository.findByPublisherContainingIgnoreCase(query);
            default -> List.of(); // empty list if invalid type
        };
    }
}
