package ru.muravyev.rbpolab1.service;

import org.springframework.stereotype.Service;
import ru.muravyev.rbpolab1.entity.User;
import ru.muravyev.rbpolab1.exception.NotFoundException;
import ru.muravyev.rbpolab1.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SurveyService surveyService;
    private final SurveyParticipationService participationService;

    public UserService(UserRepository userRepository,
                       SurveyService surveyService,
                       SurveyParticipationService participationService) {
        this.userRepository = userRepository;
        this.surveyService = surveyService;
        this.participationService = participationService;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User " + id + " not found"));
    }

    public User create(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now()); // время ставит сервер
        return userRepository.save(user);
    }

    public User update(Long id, String name, String email) {
        User user = getById(id);
        if (name != null) {
            user.setName(name);
        }
        if (email != null) {
            user.setEmail(email);
        }
        return userRepository.save(user);
    }

    public void delete(Long id) {
        getById(id);
        surveyService.deleteByAuthorId(id);
        participationService.deleteByRespondentId(id);
        userRepository.deleteById(id);
    }
}