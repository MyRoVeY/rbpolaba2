package ru.muravyev.rbpolab1.service;

import org.springframework.stereotype.Service;
import ru.muravyev.rbpolab1.entity.SurveyQuestion;
import ru.muravyev.rbpolab1.exception.NotFoundException;
import ru.muravyev.rbpolab1.repository.SurveyQuestionRepository;
import ru.muravyev.rbpolab1.repository.SurveyRepository;

import java.util.List;

@Service
public class SurveyQuestionService {

    private final SurveyQuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyAnswerService answerService;

    public SurveyQuestionService(SurveyQuestionRepository questionRepository,
                                 SurveyRepository surveyRepository,
                                 SurveyAnswerService answerService) {
        this.questionRepository = questionRepository;
        this.surveyRepository = surveyRepository;
        this.answerService = answerService;
    }

    public List<SurveyQuestion> getAll() {
        return questionRepository.findAll();
    }

    public SurveyQuestion getById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question " + id + " not found"));
    }

    public SurveyQuestion create(Long surveyId, String text, Boolean required, List<String> options) {
        checkSurveyExists(surveyId);
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("text is required");
        }
        checkOptions(options);

        SurveyQuestion question = new SurveyQuestion();
        question.setSurveyId(surveyId);
        question.setText(text);
        question.setRequired(required != null && required);
        question.setOptions(List.copyOf(options));
        return questionRepository.save(question);
    }

    public SurveyQuestion update(Long id, Long surveyId, String text, Boolean required, List<String> options) {
        SurveyQuestion question = getById(id);
        if (surveyId != null) {
            checkSurveyExists(surveyId);
            question.setSurveyId(surveyId);
        }
        if (text != null) {
            question.setText(text);
        }
        if (required != null) {
            question.setRequired(required);
        }
        if (options != null) {
            checkOptions(options);
            question.setOptions(List.copyOf(options));
        }
        return questionRepository.save(question);
    }

    public void delete(Long id) {
        getById(id);
        answerService.deleteByQuestionId(id);
        questionRepository.deleteById(id);
    }

    public void deleteBySurveyId(Long surveyId) {
        for (SurveyQuestion q : questionRepository.findBySurveyId(surveyId)) {
            delete(q.getId());
        }
    }

    private void checkSurveyExists(Long surveyId) {
        if (surveyId == null) {
            throw new IllegalArgumentException("surveyId is required");
        }
        if (!surveyRepository.existsById(surveyId)) {
            throw new NotFoundException("Survey " + surveyId + " not found");
        }
    }

    private void checkOptions(List<String> options) {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("options must contain at least one value");
        }
    }
}