package ru.muravyev.rbpolab1.dto;

import java.util.List;

public record SurveyQuestionResponse(
        Long id,
        Long surveyId,
        String text,
        boolean required,
        List<String> options
) {
}