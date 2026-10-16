package ru.muravyev.rbpolab1.dto;

import ru.muravyev.rbpolab1.entity.ParticipationStatus;

public record SurveyParticipationRequest(
        Long surveyId,
        Long respondentId,
        ParticipationStatus status
) {
}