package ru.muravyev.rbpolab1.repository;

import org.springframework.stereotype.Repository;
import ru.muravyev.rbpolab1.entity.SurveyAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SurveyAnswerRepository {

    private final Map<Long, SurveyAnswer> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<SurveyAnswer> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<SurveyAnswer> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<SurveyAnswer> findByParticipationId(Long participationId) {
        return storage.values().stream()
                .filter(a -> a.getParticipationId().equals(participationId))
                .toList();
    }

    public List<SurveyAnswer> findByQuestionId(Long questionId) {
        return storage.values().stream()
                .filter(a -> a.getQuestionId().equals(questionId))
                .toList();
    }

    public boolean existsByParticipationIdAndQuestionId(Long participationId, Long questionId) {
        return storage.values().stream()
                .anyMatch(a -> a.getParticipationId().equals(participationId)
                        && a.getQuestionId().equals(questionId));
    }

    public SurveyAnswer save(SurveyAnswer answer) {
        if (answer.getId() == null) {
            answer.setId(idGenerator.incrementAndGet());
        }
        storage.put(answer.getId(), answer);
        return answer;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }
}