package com.example.identityService.service;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.UserRequest;
import com.example.identityService.dto.response.UserResponse;

import java.util.List;

public interface AdminService{
    PageResponse<UserResponse> getAll(int page, int size);
    UserResponse getUserById(String id);
    UserResponse updateUser(String id, UserRequest request);
    String deleteUser(String id);
}
