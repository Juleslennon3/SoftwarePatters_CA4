package com.bookshop.bookstore.controller;

import com.bookshop.bookstore.model.*;
import com.bookshop.bookstore.repository.OrderRepository;
import com.bookshop.bookstore.repository.OrderItemRepository;
import com.bookshop.bookstore.repository.UserRepository;
import com.bookshop.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * ✅ Place an order using the user's email
     */
    @PostMapping("/checkout")
    public ResponseEntity<String> placeOrder(@RequestParam String email) {
        // Find the user
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found");
        }

        // Check if cart is empty
        List<CartItem> cartItems = cartService.getCart();
        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        // Create the order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(cartService.calculateTotal());
        orderRepository.save(order);

        // Save all order items
        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem(order, item.getBook(), item.getQuantity());
            orderItemRepository.save(orderItem);
        }

        // Clear cart after successful order
        cartService.clearCart();

        return ResponseEntity.ok("Order placed successfully");
    }

    /**
     * ✅ (Optional) View all orders
     */
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * ✅ (Optional) View single order by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
 // ✅ View all orders for a user
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrdersByUser(@RequestParam String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build(); // or return 404
        }

        List<Order> orders = orderRepository.findByUser(user);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getOrdersByUserEmail(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(orderRepository.findByUser(user)))
                .orElseGet(() -> ResponseEntity.badRequest().body("User not found"));
    }


}
