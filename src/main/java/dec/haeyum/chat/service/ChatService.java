package dec.haeyum.chat.service;

import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.dto.ChatMessageDto;

import java.util.List;

public interface ChatService {
    List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId);

    ChatMessageDto saveMessage(ChatMessageDto chatMessageDto, String bearerToken);

    void createChatRoom();
}
