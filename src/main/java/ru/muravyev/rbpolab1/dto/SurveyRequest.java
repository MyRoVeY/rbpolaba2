package ru.muravyev.rbpolab1.dto;

import ru.muravyev.rbpolab1.entity.SurveyStatus;

import java.time.LocalDateTime;

public record SurveyRequest(
        String title,
        Long authorId,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        SurveyStatus status
) {
}