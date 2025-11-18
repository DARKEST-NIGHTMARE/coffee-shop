package com.coffeeshop.coffee_shop_backend.dto;

import com.coffeeshop.coffee_shop_backend.model.StaffRole;
public record RegisterRequest(
        String username,
        String password,
        StaffRole role
) {
}