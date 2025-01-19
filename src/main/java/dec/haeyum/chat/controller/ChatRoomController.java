package dec.haeyum.chat.controller;

import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.chat.dto.ChatRoomDto;
import dec.haeyum.chat.repository.ChatRoomRepository;
import dec.haeyum.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

    @PostMapping("/chatroom/create")
    public ResponseEntity<String> createChatRoom() {
        chatService.createChatRoom();
        return ResponseEntity.ok("채팅방 생성이 완료되었습니다");
    }

    @GetMapping("/chatroom")
    public ResponseEntity<List<ChatRoomDto>> getChatRoom() {
        List<ChatRoomDto> chatRoomDtos = chatRoomRepository.findAll()
                .stream()
                .map(ChatRoomDto::toDto).toList();
        return ResponseEntity.ok(chatRoomDtos);
    }



}
