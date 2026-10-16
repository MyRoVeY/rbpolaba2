package ru.muravyev.rbpolab1.dto;

public record SurveyAnswerResponse(
        Long id,
        Long participationId,
        Long questionId,
        String value
) {
}