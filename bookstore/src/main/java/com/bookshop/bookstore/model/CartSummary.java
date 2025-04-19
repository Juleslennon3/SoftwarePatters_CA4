package com.bookshop.bookstore.model;

import java.util.List;

public class CartSummary {
    private List<CartItem> items;
    private double total;

    public CartSummary(List<CartItem> items, double total) {
        this.items = items;
        this.total = total;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
