package ru.muravyev.rbpolab1.repository;

import org.springframework.stereotype.Repository;
import ru.muravyev.rbpolab1.entity.SurveyQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SurveyQuestionRepository {

    private final Map<Long, SurveyQuestion> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<SurveyQuestion> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<SurveyQuestion> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<SurveyQuestion> findBySurveyId(Long surveyId) {
        return storage.values().stream()
                .filter(q -> q.getSurveyId().equals(surveyId))
                .toList();
    }

    public SurveyQuestion save(SurveyQuestion question) {
        if (question.getId() == null) {
            question.setId(idGenerator.incrementAndGet());
        }
        storage.put(question.getId(), question);
        return question;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}