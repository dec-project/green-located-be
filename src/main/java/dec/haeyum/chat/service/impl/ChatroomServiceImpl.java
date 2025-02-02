package dec.haeyum.chat.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.chat.repository.ChatRoomRepository;
import dec.haeyum.chat.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatroomServiceImpl implements ChatroomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    @Transactional
    public Long getChatRoomIdByCalendar(CalendarEntity calendar) {
        String prefix = calendar.getCalendarName().substring(0, 3);
        return chatRoomRepository.findByNameStartingWith(prefix)
                .map(ChatRoom::getId)
                .orElse(null);
    }
}
