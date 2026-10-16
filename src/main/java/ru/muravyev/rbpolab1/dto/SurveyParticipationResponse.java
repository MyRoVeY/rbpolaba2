package ru.muravyev.rbpolab1.dto;

import ru.muravyev.rbpolab1.entity.ParticipationStatus;

import java.time.LocalDateTime;

public record SurveyParticipationResponse(
        Long id,
        Long surveyId,
        Long respondentId,
        ParticipationStatus status,
        LocalDateTime startedAt,
        LocalDateTime submittedAt
) {
}