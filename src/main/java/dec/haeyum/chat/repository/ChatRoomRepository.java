package dec.haeyum.chat.repository;

import dec.haeyum.chat.Entity.ChatRoom;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByOrderByLastMessageDateDesc();

    @Query("SELECT c FROM ChatRoom c WHERE c.id NOT IN :excludeIds ORDER BY c.lastMessageDate DESC")
    List<ChatRoom> findByIdNotIn(@Param("excludeIds") List<Long> excludeIds);

    Optional<ChatRoom> findByNameStartingWith(String prefix);
}
