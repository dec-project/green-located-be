package dec.haeyum.chat.dto;

import dec.haeyum.chat.Entity.ChatRoom;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class ChatRoomDto {
    private Long roomId;
    private String name;
    private LocalDate lastMessageDate;
    private String lastMessage;

    public static ChatRoomDto toDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .lastMessageDate(chatRoom.getLastMessageDate())
                .lastMessage(chatRoom.getLastMessage())
                .build();
    }
}
