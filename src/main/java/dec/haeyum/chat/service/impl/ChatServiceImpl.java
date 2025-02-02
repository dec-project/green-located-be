package dec.haeyum.chat.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.chat.Entity.ChatMessage;
import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.chat.dto.ChatMessageDto;
import dec.haeyum.chat.dto.ChatMessageRequestDto;
import dec.haeyum.chat.repository.ChatMessageRepository;
import dec.haeyum.chat.repository.ChatRoomRepository;
import dec.haeyum.chat.service.ChatService;
import dec.haeyum.config.error.ErrorCode;
import dec.haeyum.config.error.exception.BusinessException;
import dec.haeyum.member.entity.Member;
import dec.haeyum.member.jwt.JwtTokenProvider;
import dec.haeyum.popularSearch.service.PopularSearchService;
import dec.haeyum.social.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialService socialService;
    private final PopularSearchService popularSearchService;
    private final CalendarRepository calendarRepository;

    //특정 채팅방 메시지 조회
    @Override
    @Transactional
    public List<ChatMessageDto> getMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId).stream()
                .map(ChatMessageDto::toDto)
                .toList();
    }

    //메시지 저장
    @Override
    @Transactional
    public ChatMessageDto saveMessage(ChatMessageRequestDto chatMessageRequestDto, String bearerToken) {

        String accessToken = jwtTokenProvider.resolveBearerToken(bearerToken);
        String subject = jwtTokenProvider.parseClaims(accessToken).getSubject();

        Member member = socialService.findMember(subject);

        //해당하는 채팅방 찾기
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.getChatRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND));

        ChatMessage message = new ChatMessage(chatRoom, member, chatMessageRequestDto.getContent());
        ChatMessage savedMessage = chatMessageRepository.save(message);

        chatRoom.setLastMessage(savedMessage.getContent());
        chatRoom.setLastMessageDate(savedMessage.getDate());

        popularSearchService.incrementDailyChatroom(chatRoom.getId());

        return ChatMessageDto.toDto(savedMessage);
    }

    @Override
    @Transactional
    public void createChatRoom() {
        ChatRoom chatRoomAll = ChatRoom.builder().name("전체").imgName("all.webp").build();
        ChatRoom chatRoom70s = ChatRoom.builder().name("1970년대").imgName("1970.webp").build();
        ChatRoom chatRoom80s = ChatRoom.builder().name("1980년대").imgName("1980.webp").build();
        ChatRoom chatRoom90s = ChatRoom.builder().name("1990년대").imgName("1990.webp").build();
        ChatRoom chatRoom00s = ChatRoom.builder().name("2000년대").imgName("2000.webp").build();
        ChatRoom chatRoom10s = ChatRoom.builder().name("2010년대").imgName("2010.webp").build();
        ChatRoom chatRoom20s = ChatRoom.builder().name("2020년대").imgName("2020.webp").build();

        chatRoomRepository.save(chatRoomAll);
        chatRoomRepository.save(chatRoom20s);
        chatRoomRepository.save(chatRoom10s);
        chatRoomRepository.save(chatRoom00s);
        chatRoomRepository.save(chatRoom90s);
        chatRoomRepository.save(chatRoom80s);
        chatRoomRepository.save(chatRoom70s);

    }

}
