package org.example.service;

import org.example.dto.message.MessageRequestDTO;
import org.example.dto.message.MessageResponseDTO;
import org.example.mapper.MessageMapper;
import org.example.model.Message;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;
import org.example.validation.MessageValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final MessageValidator messageValidator;
    private final MessageMapper messageMapper;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository, MessageValidator messageValidator, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messageValidator = messageValidator;
        this.messageMapper = messageMapper;
    }

    /**
     * @param id
     */
    public void delete(Long id) {
        messageRepository.delete(messageValidator.deleteMessageValidation(id));
    }

    /**
     * @param userId
     * @param messageRequestDTO
     * @return
     */
    public MessageResponseDTO sendMessage(Long userId, MessageRequestDTO messageRequestDTO) {
        messageValidator.sendMessageValidation(userId, messageRequestDTO);

        Message message = new Message();

        message.setSender(userRepository.getById(userId));
        message.setRecipient(userRepository.getById(messageRequestDTO.getRecipientId()));
        message.setText(messageRequestDTO.getText());
        message.setSendDate(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        return messageMapper.messageToMessageResponseDTO(savedMessage);
    }

    /**
     * @param senderId
     * @param recipientId
     * @return Dto of messages between users
     */
    public List<MessageResponseDTO> findAllMessagesBetweenUsers(Long senderId, Long recipientId) {
        messageValidator.findAllMessagesBetweenUsersValidation(senderId, recipientId);
        List<Message> messages = messageRepository.findAllBySender_IdAndRecipient_Id(senderId, recipientId);
        messages.addAll(messageRepository.findAllBySender_IdAndRecipient_Id(recipientId, senderId));
        messages.sort(Comparator.comparing(Message::getSendDate));

        return messages.stream()
                .map(messageMapper::messageToMessageResponseDTO)
                .collect(Collectors.toList());
    }

}
