package ru.muravyev.rbpolab1.entity;

import java.util.List;

public class SurveyQuestion {

    private Long id;
    private Long surveyId;
    private String text;
    private boolean required;
    private List<String> options;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }
}