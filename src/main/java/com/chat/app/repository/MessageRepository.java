package com.chat.app.repository;

import com.chat.app.entity.MessageEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findTop50BySenderOrderBySentAtDesc(String sender);
    List<MessageEntity> findTop50ByOrderBySentAtAsc(PageRequest pageRequest);
}