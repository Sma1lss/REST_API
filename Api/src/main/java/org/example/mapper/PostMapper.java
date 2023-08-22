package org.example.mapper;

import org.example.dto.post.PostRequestDTO;
import org.example.dto.post.PostResponseDTO;
import org.example.dto.post.PostUpdateRequest;
import org.example.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "sender.id", target = "senderId")
    PostResponseDTO postToPostResponseDTO(Post post);

    @Mapping(target = "sendDate", ignore = true)
    @Mapping(target = "sender", ignore = true)
    Post postRequestDTOtoPost(PostRequestDTO postRequestDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sendDate", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "addressee", ignore = true)
    @Mapping(target = "reactions", ignore = true)
    @Mapping(target = "commentCount", ignore = true)
    @Mapping(target = "comments", ignore = true)
    Post postUpdateRequestToPost(PostUpdateRequest postUpdateRequest);
}
