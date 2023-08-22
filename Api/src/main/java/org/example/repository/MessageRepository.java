package org.example.repository;

import org.example.model.Message;
import org.jetbrains.annotations.TestOnly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySender_IdAndRecipient_Id(Long senderId, Long recipientId);

}
