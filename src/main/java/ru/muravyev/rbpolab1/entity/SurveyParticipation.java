package ru.muravyev.rbpolab1.entity;

import java.time.LocalDateTime;

public class SurveyParticipation {

    private Long id;
    private Long surveyId;
    private Long respondentId;
    private ParticipationStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }

    public Long getRespondentId() { return respondentId; }
    public void setRespondentId(Long respondentId) { this.respondentId = respondentId; }

    public ParticipationStatus getStatus() { return status; }
    public void setStatus(ParticipationStatus status) { this.status = status; }

    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}