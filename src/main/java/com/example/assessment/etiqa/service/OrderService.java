package com.example.assessment.etiqa.service;

import com.example.assessment.etiqa.dto.OrderItemRequestDTO;
import com.example.assessment.etiqa.dto.OrderItemResponseDTO;
import com.example.assessment.etiqa.dto.OrderResponseDTO;
import com.example.assessment.etiqa.exception.NotFoundException;
import com.example.assessment.etiqa.model.Customer;
import com.example.assessment.etiqa.model.Order;
import com.example.assessment.etiqa.model.OrderItem;
import com.example.assessment.etiqa.model.Product;
import com.example.assessment.etiqa.repository.CustomerRepository;
import com.example.assessment.etiqa.repository.OrderItemRepository;
import com.example.assessment.etiqa.repository.OrderRepository;
import com.example.assessment.etiqa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public OrderResponseDTO placeOrder(Long customerId, List<OrderItemRequestDTO> orderItemRequestDTOs) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
            log.error("Customer not found with id : {}", customerId);
            return new NotFoundException("Customer not found with id : " + customerId);
        });

        List<OrderItem> orderItems = mapToOrderItems(orderItemRequestDTOs);
        validateOrderItemsQuantity(orderItems);


        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();
            product.setBookQuantity(product.getBookQuantity() - orderItem.getQuantity());
            productRepository.save(product);
        }


        Order order = new Order();
        order.setCustomer(customer);
        order.setTotalPrice(calculateTotalPrice(orderItems));
        order.setStatus("PENDING");
        order.setOrderDate(new java.util.Date());

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        order.setOrderItems(orderItems);

        orderRepository.save(order);

        return mapToOrderResponseDTO(order);
    }

    public List<OrderResponseDTO> findOrderByCustomerId(Long customerId) {
        List<Order> order = orderRepository.findByCustomerId(customerId);
        return order.stream().map(this::mapToOrderResponseDTO).collect(Collectors.toList());
    }

    public BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItems) {
            totalPrice = totalPrice.add(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
        }

        return totalPrice;
    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order) {
        List<OrderItemResponseDTO> orderItemResponseDTOs = order.getOrderItems().stream()
                .map(entity -> {
                    OrderItemResponseDTO dto = new OrderItemResponseDTO();
                    dto.setProductId(entity.getProduct().getId());
                    dto.setProductName(entity.getProduct().getBookTitle());
                    dto.setQuantity(entity.getQuantity());
                    dto.setPrice(entity.getPrice());
                    return dto;
                }).collect(Collectors.toList());
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setCustomerId(order.getCustomer().getId());
        orderResponseDTO.setOrderItems(orderItemResponseDTOs);
        orderResponseDTO.setTotalPrice(order.getTotalPrice());
        orderResponseDTO.setStatus(order.getStatus());

        return orderResponseDTO;
    }

    private List<OrderItem> mapToOrderItems(List<OrderItemRequestDTO> orderItemRequestDTOs) {
        return orderItemRequestDTOs.stream().map(dto -> {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> {
                        log.error("Product not found with id : {}", dto.getProductId());
                        return new NotFoundException("Product not found with id : " + dto.getProductId());
                    });
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setPrice(product.getBookPrice());
            orderItem.setQuantity(dto.getQuantity());
            return orderItem;
        }).collect(Collectors.toList());
    }

    private void validateOrderItemsQuantity (List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getQuantity() <= 0) {
                log.error("Quantity for item bought with id {} cannot be less than zero.", orderItem.getProduct().getId());
                throw new IllegalArgumentException("Quantity for item bought with id " + orderItem.getProduct().getId() + " cannot be less than zero.");
            }

            if (orderItem.getProduct().getBookQuantity() < orderItem.getQuantity()) {
                log.error("There are not enough stocks available for this product id : {}", orderItem.getProduct().getId());
                throw new IllegalArgumentException("There are not enough stocks available for this product id : " + orderItem.getProduct().getId());
            }
        }
    }

}
