package ru.muravyev.rbpolab1.service;

import org.springframework.stereotype.Service;
import ru.muravyev.rbpolab1.entity.ParticipationStatus;
import ru.muravyev.rbpolab1.entity.SurveyAnswer;
import ru.muravyev.rbpolab1.entity.SurveyParticipation;
import ru.muravyev.rbpolab1.entity.SurveyQuestion;
import ru.muravyev.rbpolab1.exception.NotFoundException;
import ru.muravyev.rbpolab1.repository.SurveyAnswerRepository;
import ru.muravyev.rbpolab1.repository.SurveyParticipationRepository;
import ru.muravyev.rbpolab1.repository.SurveyQuestionRepository;

import java.util.List;

@Service
public class SurveyAnswerService {

    private final SurveyAnswerRepository answerRepository;
    private final SurveyParticipationRepository participationRepository;
    private final SurveyQuestionRepository questionRepository;

    public SurveyAnswerService(SurveyAnswerRepository answerRepository,
                               SurveyParticipationRepository participationRepository,
                               SurveyQuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.participationRepository = participationRepository;
        this.questionRepository = questionRepository;
    }

    public List<SurveyAnswer> getAll() {
        return answerRepository.findAll();
    }

    public SurveyAnswer getById(Long id) {
        return answerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Answer " + id + " not found"));
    }

    public SurveyAnswer create(Long participationId, Long questionId, String value) {
        SurveyParticipation participation = findParticipation(participationId);
        SurveyQuestion question = findQuestion(questionId);

        checkNotSubmitted(participation);
        if (!question.getSurveyId().equals(participation.getSurveyId())) {
            throw new IllegalArgumentException("Question " + questionId
                    + " does not belong to the survey of participation " + participationId);
        }
        if (answerRepository.existsByParticipationIdAndQuestionId(participationId, questionId)) {
            throw new IllegalArgumentException("Question " + questionId
                    + " is already answered in participation " + participationId);
        }
        checkValue(question, value);

        SurveyAnswer answer = new SurveyAnswer();
        answer.setParticipationId(participationId);
        answer.setQuestionId(questionId);
        answer.setValue(value);
        return answerRepository.save(answer);
    }

    public SurveyAnswer update(Long id, String value) {
        SurveyAnswer answer = getById(id);
        if (value == null) {
            return answer;
        }
        checkNotSubmitted(findParticipation(answer.getParticipationId()));
        checkValue(findQuestion(answer.getQuestionId()), value);
        answer.setValue(value);
        return answerRepository.save(answer);
    }

    public void delete(Long id) {
        getById(id);
        answerRepository.deleteById(id);
    }

    public void deleteByParticipationId(Long participationId) {
        for (SurveyAnswer a : answerRepository.findByParticipationId(participationId)) {
            answerRepository.deleteById(a.getId());
        }
    }

    public void deleteByQuestionId(Long questionId) {
        for (SurveyAnswer a : answerRepository.findByQuestionId(questionId)) {
            answerRepository.deleteById(a.getId());
        }
    }

    private SurveyParticipation findParticipation(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("participationId is required");
        }
        return participationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Participation " + id + " not found"));
    }

    private SurveyQuestion findQuestion(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("questionId is required");
        }
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question " + id + " not found"));
    }

    private void checkNotSubmitted(SurveyParticipation participation) {
        if (participation.getStatus() == ParticipationStatus.SUBMITTED) {
            throw new IllegalArgumentException("Participation " + participation.getId()
                    + " is already submitted");
        }
    }

    private void checkValue(SurveyQuestion question, String value) {
        if (value == null || !question.getOptions().contains(value)) {
            throw new IllegalArgumentException("Value must be one of " + question.getOptions());
        }
    }
}