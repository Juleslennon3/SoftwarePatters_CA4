package com.bookshop.bookstore.sort;

import com.bookshop.bookstore.model.Book;
import java.util.List;

public interface BookSortStrategy {
    List<Book> sort(List<Book> books);
}
