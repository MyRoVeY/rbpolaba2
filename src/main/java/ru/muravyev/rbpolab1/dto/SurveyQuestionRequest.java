package ru.muravyev.rbpolab1.dto;

import java.util.List;

public record SurveyQuestionRequest(
        Long surveyId,
        String text,
        Boolean required,
        List<String> options
) {
}