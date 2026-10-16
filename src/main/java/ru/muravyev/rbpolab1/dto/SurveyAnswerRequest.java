package ru.muravyev.rbpolab1.dto;

public record SurveyAnswerRequest(
        Long participationId,
        Long questionId,
        String value
) {
}