package com.bookshop.bookstore.repository;
import com.bookshop.bookstore.model.User;
import java.util.List;

import com.bookshop.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUser(User user);
}

