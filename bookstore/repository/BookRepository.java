package com.bookshop.bookstore.repository;

import com.bookshop.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByCategoryContainingIgnoreCase(String category);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByPublisherContainingIgnoreCase(String publisher);
}
