package ru.muravyev.rbpolab1.repository;

import org.springframework.stereotype.Repository;
import ru.muravyev.rbpolab1.entity.SurveyParticipation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SurveyParticipationRepository {

    private final Map<Long, SurveyParticipation> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public List<SurveyParticipation> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<SurveyParticipation> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<SurveyParticipation> findBySurveyId(Long surveyId) {
        return storage.values().stream()
                .filter(p -> p.getSurveyId().equals(surveyId))
                .toList();
    }

    public List<SurveyParticipation> findByRespondentId(Long respondentId) {
        return storage.values().stream()
                .filter(p -> p.getRespondentId().equals(respondentId))
                .toList();
    }

    public SurveyParticipation save(SurveyParticipation participation) {
        if (participation.getId() == null) {
            participation.setId(idGenerator.incrementAndGet());
        }
        storage.put(participation.getId(), participation);
        return participation;
    }

    public void deleteById(Long id) {
        storage.remove(id);
    }

    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}