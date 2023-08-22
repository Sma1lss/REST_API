package org.example.mapper;

import org.example.dto.comment.CommentRequestDTO;
import org.example.dto.comment.CommentResponseDTO;
import org.example.dto.comment.CommentUpdateRequestDTO;
import org.example.meetexApi.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "post.id", target = "postId")
    CommentResponseDTO commentToCommentResponseDTO(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sendDate", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "parentComment", ignore = true)
    @Mapping(target = "childComments", ignore = true)
    Comment commentRequestDTOtoComment(CommentRequestDTO commentRequestDTO);

    @Mapping(target = "text", source = "text")
    void updateCommentsFromDTO(CommentUpdateRequestDTO dto, @MappingTarget Comment comment);
}
