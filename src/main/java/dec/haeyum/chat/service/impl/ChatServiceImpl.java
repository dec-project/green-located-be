package dec.haeyum.chat.service.impl;

import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.chat.dto.ChatMessageDto;
import dec.haeyum.chat.repository.ChatMessageRepository;
import dec.haeyum.chat.repository.ChatRoomRepository;
import dec.haeyum.chat.service.ChatService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    //특정 채팅방 메시지 조회
    @Override
    @Transactional
    public List<ChatMessage> getMessagesByChatRoomId(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    //메시지 저장
    @Override
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {

        //해당하는 채팅방 찾기
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        ChatMessage message = new ChatMessage(chatRoom, chatMessageDto.getSender(), chatMessageDto.getContent());
        ChatMessage savedMessage = chatMessageRepository.save(message);

        chatRoom.setLastMessage(savedMessage.getContent());
        chatRoom.setLastMessageDate(savedMessage.getDate());

        return ChatMessageDto.toDto(savedMessage);
    }

    @Override
    @Transactional
    public void createChatRoom() {
        ChatRoom chatRoom70s = ChatRoom.builder().name("1970년대").build();
        ChatRoom chatRoom80s = ChatRoom.builder().name("1980년대").build();
        ChatRoom chatRoom90s = ChatRoom.builder().name("1990년대").build();
        ChatRoom chatRoom00s = ChatRoom.builder().name("2000년대").build();
        ChatRoom chatRoom10s = ChatRoom.builder().name("2010년대").build();
        ChatRoom chatRoom20s = ChatRoom.builder().name("2020년대").build();

        chatRoomRepository.save(chatRoom70s);
        chatRoomRepository.save(chatRoom80s);
        chatRoomRepository.save(chatRoom90s);
        chatRoomRepository.save(chatRoom00s);
        chatRoomRepository.save(chatRoom10s);
        chatRoomRepository.save(chatRoom20s);
    }
}
