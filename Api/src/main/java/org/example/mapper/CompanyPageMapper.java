package org.example.mapper;

import org.example.dto.companyPage.CompanyPageRequestDTO;
import org.example.dto.companyPage.CompanyPageResponseDTO;
import org.example.dto.user.UserResponseDTO;
import org.example.model.CompanyPage;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.HashSet;
import java.util.Set;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CompanyPageMapper {

    @Mapping(source = "ownerId", target = "admins", qualifiedByName = "idToAdmins")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "website", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    CompanyPage companyPageRequestDTOtoCompanyPage(CompanyPageRequestDTO companyPageRequestDTO);

    @Mapping(source = "logo", target = "logoUrl")
    @Mapping(source = "admins", target = "owner", qualifiedByName = "firstAdminId")
    CompanyPageResponseDTO companyPageToCompanyPageResponseDTO(CompanyPage companyPage);

    @Named("idToAdmins")
    default Set<User> idToAdmins(Long id) {
        if (id == null) {
            return null;
        }

        User user = new User();
        user.setId(id);
        Set<User> users = new HashSet<>();
        users.add(user);

        return users;
    }

    @Named("firstAdminId")
    default UserResponseDTO firstAdminId(Set<User> admins) {
        if (admins == null || admins.isEmpty()) {
            return null;
        }

        User firstAdmin = admins.iterator().next();
        return Mappers.getMapper(UserMapper.class).userToUserResponseDTO(firstAdmin);
    }
}
