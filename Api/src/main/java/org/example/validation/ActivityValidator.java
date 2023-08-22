package org.example.validation;

import org.example.exception.activity.ActivityNotFoundException;
import org.example.model.Activity;
import com.example.repository.ActivityRepository;
import org.springframework.stereotype.Component;

@Component
public class ActivityValidator {
    private final ActivityRepository activityRepository;

    public ActivityValidator(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public Activity deleteValidation(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ActivityNotFoundException("Can not delete activity with id = " + id));
    }

    public Activity findActivityValidation(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(()->new ActivityNotFoundException("Can not found activity with id = "+id));
    }
}
