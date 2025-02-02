package dec.haeyum.chat.dto;

import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    private Long chatroomId;
    private String content;
}
