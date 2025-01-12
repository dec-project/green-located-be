package dec.haeyum.chat.controller;

import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.dto.ChatMessageDto;
import dec.haeyum.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatService chatService;

    // 채팅방 메시지조회
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@PathVariable Long id){
        List<ChatMessage> messages = chatService.getMessagesByChatRoomId(id);

        return ResponseEntity.ok(messages);
    }

    //메시지 송신 및 수신, /pub가 생략된 모습. 클라이언트 단에선 /pub/message로 요청
    @MessageMapping("/message")
    public void receiveMessage(ChatMessageDto chatMessageDto) {
        log.info("메시지 전송: {}", chatMessageDto.getContent());
        log.info("채팅방: {}", chatMessageDto.getChatRoomId());
        log.info("sender: {}", chatMessageDto.getSender());
        //메시지 저장
        ChatMessageDto savedMessage = chatService.saveMessage(chatMessageDto);

        // 메시지를 해당 채팅방 구독자들에게 전송
        template.convertAndSend("/sub/chatroom/" + savedMessage.getChatRoomId(), savedMessage);
    }
}
