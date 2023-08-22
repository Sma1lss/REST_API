package org.example.mapper;

import org.example.dto.user.UserRegistrationRequest;
import org.example.dto.user.UserRequestDTO;
import org.example.dto.user.UserResponseDTO;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Mapping(target = "lastName", ignore = true)
    public abstract UserResponseDTO userToUserResponseDTO(User user);

    public abstract User userRequestDTOtoUser(UserRequestDTO userRequestDTO);

    @Mapping(target = "lastName", ignore = true)
    public abstract User userRegistrationRequestToUser(UserRegistrationRequest userRegistrationRequest);
}
