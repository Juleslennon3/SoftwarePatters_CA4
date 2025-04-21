package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.model.CartItem;
import com.bookshop.bookstore.model.CartSummary;
import com.bookshop.bookstore.repository.BookRepository;
import com.bookshop.bookstore.service.CartService;
import com.bookshop.bookstore.sort.BookSortStrategy;
import com.bookshop.bookstore.sort.SortByPrice;
import com.bookshop.bookstore.sort.SortByTitle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    //  FIRST: sorted books
    @GetMapping("/sort")
    public List<Book> getSortedBooks(@RequestParam String sortBy,
                                     @RequestParam(defaultValue = "asc") String order) {
        List<Book> books = bookRepository.findAll();
        BookSortStrategy sortStrategy;

        switch (sortBy.toLowerCase()) {
            case "title" -> sortStrategy = new SortByTitle();
            default -> {
                return books;
            }
        }

        List<Book> sorted = sortStrategy.sort(books);
        if ("desc".equalsIgnoreCase(order)) {
            Collections.reverse(sorted);
        }

        return sorted;
    }

    

    //  All books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    //  Book by ID
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id);
    }

    //  Create book
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    //  Update book
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

    //  Delete book
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
    }

    //  Search books
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String query, @RequestParam String type) {
        return switch (type.toLowerCase()) {
            case "title" -> bookRepository.findByTitleContainingIgnoreCase(query);
            case "category" -> bookRepository.findByCategoryContainingIgnoreCase(query);
            case "author" -> bookRepository.findByAuthorContainingIgnoreCase(query);
            case "publisher" -> bookRepository.findByPublisherContainingIgnoreCase(query);
            default -> List.of();
        };
    }
    
    @Autowired
    private CartService cartService;


    @PostMapping("/cart/add")
    public ResponseEntity<String> addToCart(@RequestParam Long bookId, @RequestParam int quantity) {
        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            cartService.addToCart(book.get(), quantity);
            return ResponseEntity.ok("Book added to cart");
        } else {
            return ResponseEntity.badRequest().body("Book not found");
        }
    }

    @GetMapping("/cart")
    public List<CartItem> viewCart() {
        return cartService.getCart();
    }

    @DeleteMapping("/cart/remove/{bookId}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long bookId) {
        cartService.removeFromCart(bookId);
        return ResponseEntity.ok("Book removed from cart");
    }

    @DeleteMapping("/cart/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared");
    }
    
    @GetMapping("/cart/summary")
    public CartSummary getCartSummary() {
        List<CartItem> items = cartService.getCart();
        double total = cartService.calculateTotal();
        return new CartSummary(items, total);
    }


}
