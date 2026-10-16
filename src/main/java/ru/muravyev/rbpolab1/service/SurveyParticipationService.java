package ru.muravyev.rbpolab1.service;

import org.springframework.stereotype.Service;
import ru.muravyev.rbpolab1.entity.ParticipationStatus;
import ru.muravyev.rbpolab1.entity.Survey;
import ru.muravyev.rbpolab1.entity.SurveyParticipation;
import ru.muravyev.rbpolab1.entity.SurveyStatus;
import ru.muravyev.rbpolab1.exception.NotFoundException;
import ru.muravyev.rbpolab1.repository.SurveyParticipationRepository;
import ru.muravyev.rbpolab1.repository.SurveyRepository;
import ru.muravyev.rbpolab1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyParticipationService {

    private final SurveyParticipationRepository participationRepository;
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SurveyAnswerService answerService;

    public SurveyParticipationService(SurveyParticipationRepository participationRepository,
                                      SurveyRepository surveyRepository,
                                      UserRepository userRepository,
                                      SurveyAnswerService answerService) {
        this.participationRepository = participationRepository;
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.answerService = answerService;
    }

    public List<SurveyParticipation> getAll() {
        return participationRepository.findAll();
    }

    public SurveyParticipation getById(Long id) {
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participation " + id + " not found"));
    }

    public SurveyParticipation create(Long surveyId, Long respondentId) {
        Survey survey = findSurvey(surveyId);
        if (survey.getStatus() != SurveyStatus.ACTIVE) {
            throw new IllegalArgumentException("Survey " + surveyId + " is not active");
        }
        checkUserExists(respondentId);

        SurveyParticipation participation = new SurveyParticipation();
        participation.setSurveyId(surveyId);
        participation.setRespondentId(respondentId);
        participation.setStatus(ParticipationStatus.IN_PROGRESS);
        participation.setStartedAt(LocalDateTime.now());
        return participationRepository.save(participation);
    }

    public SurveyParticipation update(Long id, ParticipationStatus newStatus) {
        SurveyParticipation participation = getById(id);
        if (newStatus == null || newStatus == participation.getStatus()) {
            return participation;
        }
        if (participation.getStatus() == ParticipationStatus.SUBMITTED) {
            throw new IllegalArgumentException("Submitted participation cannot be changed");
        }
        participation.setStatus(newStatus);
        if (newStatus == ParticipationStatus.SUBMITTED) {
            participation.setSubmittedAt(LocalDateTime.now());
        }
        return participationRepository.save(participation);
    }

    public void delete(Long id) {
        getById(id);
        answerService.deleteByParticipationId(id);
        participationRepository.deleteById(id);
    }

    public void deleteBySurveyId(Long surveyId) {
        for (SurveyParticipation p : participationRepository.findBySurveyId(surveyId)) {
            delete(p.getId());
        }
    }

    public void deleteByRespondentId(Long respondentId) {
        for (SurveyParticipation p : participationRepository.findByRespondentId(respondentId)) {
            delete(p.getId());
        }
    }

    private Survey findSurvey(Long surveyId) {
        if (surveyId == null) {
            throw new IllegalArgumentException("surveyId is required");
        }
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NotFoundException("Survey " + surveyId + " not found"));
    }

    private void checkUserExists(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("respondentId is required");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User " + userId + " not found");
        }
    }
}