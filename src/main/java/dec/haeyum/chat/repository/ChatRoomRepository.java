package dec.haeyum.chat.repository;

import dec.haeyum.chat.Entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByOrderByLastMessageDateDesc();
}
