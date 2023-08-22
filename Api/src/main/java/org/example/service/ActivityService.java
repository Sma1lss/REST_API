package org.example.service;

import org.example.dto.activity.ActivityResponseDTO;
import org.example.mapper.ActivityMapper;
import org.example.model.Activity;
import org.example.repository.ActivityRepository;
import org.example.validation.ActivityValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ActivityService {
    private final ActivityMapper activityMapper;
    private final ActivityRepository activityRepository;
    private final ActivityValidator activityValidator;

    public ActivityService(ActivityMapper activityMapper, ActivityRepository activityRepository, ActivityValidator activityValidator) {
        this.activityMapper = activityMapper;
        this.activityRepository = activityRepository;
        this.activityValidator = activityValidator;
    }

    public void delete(Long id) {
        activityRepository.delete(activityValidator.deleteValidation(id));
    }

    public ActivityResponseDTO findById(Long id) {
        Activity activity = activityValidator.findActivityValidation(id);

        return activityMapper.activityToActivityResponseDTO(activity);
    }
}
