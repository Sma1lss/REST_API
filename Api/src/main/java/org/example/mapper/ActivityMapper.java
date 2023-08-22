package org.example.mapper;

import org.example.dto.activity.ActivityRequestDTO;
import org.example.dto.activity.ActivityResponseDTO;
import org.example.model.Activity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    ActivityMapper INSTANCE = Mappers.getMapper(ActivityMapper.class);

    ActivityResponseDTO activityToActivityResponseDTO(Activity activity);

    @Mapping(target = "activities_id", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "users", ignore = true)
    Activity activityRequestDTOtoActivity(ActivityRequestDTO activityRequestDTO);
}
