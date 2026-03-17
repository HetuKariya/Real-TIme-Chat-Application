package com.chat.app.repository;

import com.chat.app.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<ConversationEntity, Long> {

    @Query("SELECT c FROM ConversationEntity c WHERE (c.user1 = :a AND c.user2 = :b) OR (c.user1 = :b AND c.user2 = :a)")
    Optional<ConversationEntity> findBetween(@Param("a") String a, @Param("b") String b);

    @Query("SELECT c FROM ConversationEntity c WHERE c.user1 = :username OR c.user2 = :username")
    List<ConversationEntity> findAllForUser(@Param("username") String username);
}