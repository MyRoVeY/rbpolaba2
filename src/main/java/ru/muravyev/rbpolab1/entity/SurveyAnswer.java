package ru.muravyev.rbpolab1.entity;

public class SurveyAnswer {

    private Long id;
    private Long participationId;
    private Long questionId;
    private String value;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParticipationId() { return participationId; }
    public void setParticipationId(Long participationId) { this.participationId = participationId; }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}