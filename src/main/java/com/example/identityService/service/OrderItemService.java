package com.example.identityService.service;

import com.example.identityService.dto.request.OrderItemRequest;
import com.example.identityService.entity.Order;
import com.example.identityService.entity.OrderItem;
import com.example.identityService.entity.Product;

import java.util.List;

public interface OrderItemService {
    OrderItem getById(String orderItemId);
    List<OrderItem> getByOrderId(String orderId);
    OrderItem create(OrderItemRequest request, Order order);
    OrderItem update(String orderItemId, OrderItemRequest request);
    OrderItem delete(String orderItemId);
}
