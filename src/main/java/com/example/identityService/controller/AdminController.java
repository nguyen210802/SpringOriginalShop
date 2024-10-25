package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;
import com.example.identityService.entity.Order;
import com.example.identityService.enums.OrderStatus;
import com.example.identityService.service.AdminService;
import com.example.identityService.service.OrderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    Map<String, Object> map;

    public AdminController(AdminService adminServiceImpl, OrderService orderServiceImpl) {
        this.map = Map.of("admin", adminServiceImpl, "order", orderServiceImpl);
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<UserResponse>> getUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        AdminService adminService = (AdminService)this.map.get("admin");

        return ApiResponse.<PageResponse<UserResponse>>builder()
                .result(adminService.getAll(page, size))
                .build();
    }

    @GetMapping()
    public ApiResponse<UserResponse> getUser(@RequestParam String id){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<UserResponse>builder()
                .result(adminService.getUserById(id))
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> updateUser(@RequestParam String id, @RequestBody UserRequest request){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<UserResponse>builder()
                .result(adminService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteUser(@RequestParam String id){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<String>builder()
                .result(adminService.deleteUser(id))
                .build();
    }

    @PutMapping("order/preparing")
    public ApiResponse<Order> preparing(@RequestParam String orderId){
        AdminService adminService = (AdminService) this.map.get("admin");
        return ApiResponse.<Order>builder()
               .result(adminService.updateOrderStatus(orderId, OrderStatus.Preparing.name()))
               .build();
    }
    @PutMapping("order/shipping")
    public ApiResponse<Order> shipping(@RequestParam String orderId){
        AdminService adminService = (AdminService) this.map.get("admin");
        return ApiResponse.<Order>builder()
                .result(adminService.updateOrderStatus(orderId, OrderStatus.Shipping.name()))
                .build();
    }

    @PutMapping("order/delivered")
    public ApiResponse<Order> delivered(@RequestParam String orderId){
        AdminService adminService = (AdminService) this.map.get("admin");
        return ApiResponse.<Order>builder()
                .result(adminService.updateOrderStatus(orderId, OrderStatus.Delivered.name()))
                .build();
    }

    @PutMapping("order/canceled")
    public ApiResponse<Order> canceled(@RequestParam String orderId){
        AdminService adminService = (AdminService) this.map.get("admin");
        return ApiResponse.<Order>builder()
                .result(adminService.updateOrderStatus(orderId, OrderStatus.Canceled.name()))
                .build();
    }

    @PutMapping("order/returned")
    public ApiResponse<Order> returned(@RequestParam String orderId){
        AdminService adminService = (AdminService) this.map.get("admin");
        return ApiResponse.<Order>builder()
                .result(adminService.updateOrderStatus(orderId, OrderStatus.Returned.name()))
                .build();
    }

    @PutMapping("order/refunded")
    public ApiResponse<Order> refunded(@RequestParam String orderId){
        AdminService adminService = (AdminService) this.map.get("admin");
        return ApiResponse.<Order>builder()
                .result(adminService.updateOrderStatus(orderId, OrderStatus.Refunded.name()))
                .build();
    }

    @DeleteMapping("/product/delete/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable("productId") String productId){
        AdminService adminService = (AdminService)this.map.get("admin");
        return ApiResponse.<String>builder()
                .result(adminService.deleteProductById(productId))
                .build();
    }
}
