package dec.haeyum.chat.service;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.dto.ChatMessageDto;
import dec.haeyum.chat.dto.ChatMessageRequestDto;

import java.util.List;

public interface ChatService {
    List<ChatMessageDto> getMessages(Long chatRoomId);

    ChatMessageDto saveMessage(ChatMessageRequestDto chatMessageDto, String bearerToken);

    void createChatRoom();

    Long getChatRoomIdByCalendar(CalendarEntity calendar);
}
