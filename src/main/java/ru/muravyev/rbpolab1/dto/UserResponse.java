package ru.muravyev.rbpolab1.dto;

import java.time.LocalDateTime;

public record UserResponse(Long id, String name, String email, LocalDateTime createdAt) {
}