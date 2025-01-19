package dec.haeyum.chat.Entity;

import dec.haeyum.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    private String content;

    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    private Member senderMember;

    public ChatMessage(ChatRoom chatRoom, Member member, String content) {
        this.chatRoom = chatRoom;
        this.senderMember = member;
        this.content = content;
    }
}
