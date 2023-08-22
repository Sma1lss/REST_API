package org.example.mapper;

import org.example.dto.message.MessageRequestDTO;
import org.example.dto.message.MessageResponseDTO;
import org.example.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    @Mapping(source = "sender.id", target = "senderId")
    @Mapping(source = "recipient.id", target = "recipientId")
    MessageResponseDTO messageToMessageResponseDTO(Message message);

    @Mapping(source = "recipientId", target = "recipient.id")
    @Mapping(target = "sendDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sender", ignore = true)
    Message MessageRequestDTOtoMessage(MessageRequestDTO messageRequestDTO);
}
