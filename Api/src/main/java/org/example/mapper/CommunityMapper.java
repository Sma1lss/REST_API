package org.example.mapper;

import org.example.dto.community.CommunityRequestDTO;
import org.example.dto.community.CommunityResponseDTO;
import org.example.model.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommunityMapper {
    CommunityMapper INSTANCE = Mappers.getMapper(CommunityMapper.class);

    @Mapping(source = "creator.id", target = "creatorId")
    CommunityResponseDTO communityToCommunityResponseDTO(Community community);

    @Mapping(source = "imageUrl", target = "imageUrl")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "members", ignore = true)
    Community communityRequestDTOtoCommunity(CommunityRequestDTO communityRequestDTO);
}
