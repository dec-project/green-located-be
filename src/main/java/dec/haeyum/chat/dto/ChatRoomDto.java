package dec.haeyum.chat.dto;

import dec.haeyum.chat.Entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ChatRoomDto {
    private Long roomId;
    private String name;
    private LocalDate lastMessageDate;
    private String lastMessage;
    private String imgUrl;

    public static ChatRoomDto toDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .lastMessageDate(chatRoom.getLastMessageDate())
                .lastMessage(chatRoom.getLastMessage())
                .imgUrl("/img/chatroom/" + chatRoom.getImgName())
                .build();
    }
}
