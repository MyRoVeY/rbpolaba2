package ru.muravyev.rbpolab1.dto;

import ru.muravyev.rbpolab1.entity.SurveyStatus;

import java.time.LocalDateTime;

public record SurveyResponse(
        Long id,
        String title,
        Long authorId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        SurveyStatus status,
        LocalDateTime createdAt
) {
}