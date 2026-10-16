package ru.muravyev.rbpolab1.repository;

import org.springframework.stereotype.Repository;
import ru.muravyev.rbpolab1.entity.Survey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SurveyRepository {

    private final Map<Long, Survey> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<Survey> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Survey> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Survey> findByAuthorId(Long authorId) {
        return storage.values().stream()
                .filter(s -> s.getAuthorId().equals(authorId))
                .toList();
    }

    public Survey save(Survey survey) {
        if (survey.getId() == null) {
            survey.setId(idGenerator.incrementAndGet());
        }
        storage.put(survey.getId(), survey);
        return survey;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}