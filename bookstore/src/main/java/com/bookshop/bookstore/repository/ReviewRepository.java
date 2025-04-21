package com.bookshop.bookstore.repository;

import com.bookshop.bookstore.model.Review;
import com.bookshop.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook(Book book);
}
