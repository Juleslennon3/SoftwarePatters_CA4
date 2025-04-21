package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.*;
import com.bookshop.bookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addReview(
            @RequestParam Long bookId,
            @RequestParam String email,
            @RequestParam int rating,
            @RequestParam String comment
    ) {
        Book book = bookRepository.findById(bookId).orElse(null);
        User user = userRepository.findByEmail(email).orElse(null);

        if (book == null || user == null) {
            return ResponseEntity.badRequest().body("Book or user not found");
        }

        Review review = new Review(rating, comment, user, book);
        reviewRepository.save(review);
        return ResponseEntity.ok("Review added successfully");
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBook(@PathVariable Long bookId) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(reviewRepository.findByBook(book));
    }
}
