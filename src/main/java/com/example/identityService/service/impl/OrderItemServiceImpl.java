package com.example.identityService.service.impl;

import com.example.identityService.dto.request.OrderItemRequest;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.OrderItem;
import com.example.identityService.entity.Product;
import com.example.identityService.enums.OrderItemStatus;
import com.example.identityService.repository.OrderItemRepository;
import com.example.identityService.service.OrderItemService;
import com.example.identityService.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {
    OrderItemRepository orderItemRepository;
    ProductService productService;

    @Override
    public OrderItem getById(String orderItemId) {
        return null;
    }

    @Override
    public List<OrderItem> getByOrderId(String orderId) {
        return List.of();
    }

    @Override
    public OrderItem create(OrderItemRequest request, Order order) {
        Product product = productService.getProductById(request.getProductId());

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .productId(request.getProductId())
                .productPrice(product.getPrice())
                .linkProduct(request.getLinkProduct())
                .quantity(request.getQuantity())
                .price(product.getPrice() * request.getQuantity())
                .status(OrderItemStatus.Preparing.name())
                .build();

        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem update(String orderItemId, OrderItemRequest request) {
        return null;
    }

    @Override
    public OrderItem delete(String orderItemId) {
        return null;
    }
}
