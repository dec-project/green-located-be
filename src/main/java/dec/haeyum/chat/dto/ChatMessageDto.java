package dec.haeyum.chat.dto;

import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.Entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageDto {
    private Long chatRoomId;
    private String sender;
    private String content;

    public static ChatMessageDto toDto(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .chatRoomId(chatMessage.getChatRoom().getId())
                .sender(chatMessage.getSender())
                .content(chatMessage.getContent())
                .build();
    }
}
