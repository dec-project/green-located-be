package dec.haeyum.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    private Long chatRoomId;
    private String content;
}
