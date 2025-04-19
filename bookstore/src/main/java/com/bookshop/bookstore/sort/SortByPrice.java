package com.bookshop.bookstore.sort;

import com.bookshop.bookstore.model.Book;
import java.util.Comparator;
import java.util.List;

public class SortByPrice implements BookSortStrategy {
    @Override
    public List<Book> sort(List<Book> books) {
        books.sort(Comparator.comparingDouble(Book::getPrice));
        return books;
    }
}
