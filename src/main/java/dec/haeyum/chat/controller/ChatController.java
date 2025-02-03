package dec.haeyum.chat.controller;

import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.dto.ChatMessageDto;
import dec.haeyum.chat.dto.ChatMessageRequestDto;
import dec.haeyum.chat.service.ChatService;
import dec.haeyum.member.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Controller
public class ChatController {

    private final SimpMessageSendingOperations template;
    private final ChatService chatService;
    private final JwtTokenProvider jwtTokenProvider;

    // 유저id 반환
    @GetMapping("/chat/get-user-id")
    public ResponseEntity<Map<String, String>> getUserId(HttpServletRequest request) {
        try {
            String accessToken = jwtTokenProvider.resloveAccessToken(request);
            String subject = jwtTokenProvider.parseClaims(accessToken).getSubject();

            Map<String, String> response = new HashMap<>();
            response.put("userId", subject);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.singletonMap("userId", null));
        }
    }

    // 채팅방 메시지조회
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable Long id) {
        List<ChatMessageDto> chatMessageDtos = chatService.getMessages(id);
        return ResponseEntity.ok(chatMessageDtos);
    }

    //메시지 송신 및 수신, /pub가 생략된 모습. 클라이언트 단에선 /pub/message로 요청
    @MessageMapping("/message")
    public void receiveMessage(ChatMessageRequestDto chatMessageRequestDto, @Header("Authorization") String bearerToken) {

        log.info("메시지 전송: {}", chatMessageRequestDto.getContent());
        log.info("채팅방: {}", chatMessageRequestDto.getChatroomId());

        //메시지 저장
        ChatMessageDto savedMessage = chatService.saveMessage(chatMessageRequestDto, bearerToken);

        // 메시지를 해당 채팅방 구독자들에게 전송
        template.convertAndSend("/sub/chatroom/" + savedMessage.getChatroomId(), savedMessage);
    }
}
