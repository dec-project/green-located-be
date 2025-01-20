package dec.haeyum.popularSearch.dto;

import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.chat.dto.ChatRoomDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class PopularChatroomDto {
    private Long roomId;
    private String name;
    private String imgUrl;
    private long chatCnt;

    public static PopularChatroomDto toDto(ChatRoom chatRoom, long chatCnt) {
        return PopularChatroomDto.builder()
                .roomId(chatRoom.getId())
                .name(chatRoom.getName())
                .imgUrl("/img/chatroom/" + chatRoom.getImgName())
                .chatCnt(chatCnt)
                .build();
    }
}
