package dec.haeyum.chat.dto;

import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageDto {

    private Long chatRoomId;
    private String senderId;
    private String senderName;
    private String profileImg;
    private String content;
    private LocalDate date;

    public static ChatMessageDto toDto(ChatMessage chatMessage, String fileUrl) {
        Member senderMember = chatMessage.getSenderMember();
        return ChatMessageDto.builder()
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(senderMember.getSocial().getSocialSub())
                .senderName(senderMember.getUsername())
                .profileImg(fileUrl + senderMember.getProfileImg())
                .content(chatMessage.getContent())
                .date(chatMessage.getDate())
                .build();
    }
}
