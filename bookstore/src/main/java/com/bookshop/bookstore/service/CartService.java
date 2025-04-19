package com.bookshop.bookstore.service;

import com.bookshop.bookstore.model.Book;
import com.bookshop.bookstore.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    private final List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCart() {
        return cartItems;
    }

    public void addToCart(Book book, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getBook().getId().equals(book.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        cartItems.add(new CartItem(book, quantity));
    }

    public void removeFromCart(Long bookId) {
        cartItems.removeIf(item -> item.getBook().getId().equals(bookId));
    }

    public void clearCart() {
        cartItems.clear();
    }
    
    public double calculateTotal() {
        return cartItems.stream()
            .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
            .sum();
    }


    
}
