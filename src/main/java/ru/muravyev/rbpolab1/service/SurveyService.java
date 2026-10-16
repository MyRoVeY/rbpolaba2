package ru.muravyev.rbpolab1.service;

import org.springframework.stereotype.Service;
import ru.muravyev.rbpolab1.entity.Survey;
import ru.muravyev.rbpolab1.entity.SurveyStatus;
import ru.muravyev.rbpolab1.exception.NotFoundException;
import ru.muravyev.rbpolab1.repository.SurveyRepository;
import ru.muravyev.rbpolab1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final SurveyQuestionService questionService;
    private final SurveyParticipationService participationService;

    public SurveyService(SurveyRepository surveyRepository,
                         UserRepository userRepository,
                         SurveyQuestionService questionService,
                         SurveyParticipationService participationService) {
        this.surveyRepository = surveyRepository;
        this.userRepository = userRepository;
        this.questionService = questionService;
        this.participationService = participationService;
    }

    public List<Survey> getAll() {
        return surveyRepository.findAll();
    }

    public Survey getById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Survey " + id + " not found"));
    }

    public Survey create(String title, Long authorId, LocalDateTime startsAt,
                         LocalDateTime endsAt, SurveyStatus status) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        checkAuthorExists(authorId);
        checkDates(startsAt, endsAt);

        Survey survey = new Survey();
        survey.setTitle(title);
        survey.setAuthorId(authorId);
        survey.setStartsAt(startsAt);
        survey.setEndsAt(endsAt);
        survey.setStatus(status != null ? status : SurveyStatus.DRAFT);
        survey.setCreatedAt(LocalDateTime.now());
        return surveyRepository.save(survey);
    }

    public Survey update(Long id, String title, Long authorId, LocalDateTime startsAt,
                         LocalDateTime endsAt, SurveyStatus status) {
        Survey survey = getById(id);
        if (title != null) {
            survey.setTitle(title);
        }
        if (authorId != null) {
            checkAuthorExists(authorId);
            survey.setAuthorId(authorId);
        }
        if (startsAt != null) {
            survey.setStartsAt(startsAt);
        }
        if (endsAt != null) {
            survey.setEndsAt(endsAt);
        }
        if (status != null) {
            survey.setStatus(status);
        }
        checkDates(survey.getStartsAt(), survey.getEndsAt());
        return surveyRepository.save(survey);
    }

    public void delete(Long id) {
        getById(id);
        questionService.deleteBySurveyId(id);
        participationService.deleteBySurveyId(id);
        surveyRepository.deleteById(id);
    }
    public void deleteByAuthorId(Long authorId) {
        for (Survey s : surveyRepository.findByAuthorId(authorId)) {
            delete(s.getId());
        }
    }

    private void checkAuthorExists(Long authorId) {
        if (authorId == null) {
            throw new IllegalArgumentException("authorId is required");
        }
        if (!userRepository.existsById(authorId)) {
            throw new NotFoundException("User " + authorId + " not found");
        }
    }

    private void checkDates(LocalDateTime startsAt, LocalDateTime endsAt) {
        if (startsAt != null && endsAt != null && endsAt.isBefore(startsAt)) {
            throw new IllegalArgumentException("endsAt must be after startsAt");
        }
    }
}