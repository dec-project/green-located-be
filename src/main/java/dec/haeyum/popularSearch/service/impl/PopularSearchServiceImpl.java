package dec.haeyum.popularSearch.service.impl;

import dec.haeyum.calendar.entity.CalendarEntity;
import dec.haeyum.calendar.repository.CalendarRepository;
import dec.haeyum.chat.Entity.ChatRoom;
import dec.haeyum.chat.dto.ChatRoomDto;
import dec.haeyum.chat.repository.ChatMessageRepository;
import dec.haeyum.chat.repository.ChatRoomRepository;
import dec.haeyum.chat.service.ChatService;
import dec.haeyum.chat.service.ChatroomService;
import dec.haeyum.popularSearch.dto.PopularChatroomDto;
import dec.haeyum.popularSearch.dto.PopularSearchDto;
import dec.haeyum.popularSearch.service.PopularSearchService;
import dec.haeyum.song.service.SongService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopularSearchServiceImpl implements PopularSearchService {
    private final CalendarRepository calendarRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SongService songService;
    private final RedisTemplate redisTemplate;
    private final ChatroomService chatroomService;

    @Override
    @Transactional
    public List<PopularSearchDto> getPopularSearch() {
        List<String> topFiveSearch = getTopFiveSearch();
        int remainingCount = 5 - topFiveSearch.size(); // 부족한 개수 계산

        log.info("remaining count: {}", remainingCount);

        // 부족한 경우 채우기
        if (remainingCount > 0) {
            List<Long> excludeIds = topFiveSearch.stream()
                    .map(Long::valueOf) // String -> Long 변환
                    .toList();

            Pageable pageable = PageRequest.of(0, remainingCount); // 부족한 개수만큼 페이징 설정
            List<CalendarEntity> additionalCalendars = calendarRepository.findTopByViewCountExcluding(excludeIds, pageable);

            // 결과 추가
            topFiveSearch.addAll(additionalCalendars.stream()
                    .map(calendar -> String.valueOf(calendar.getCalendarId()))
                    .toList());
        }

        log.info("topFiveSearch: {}", topFiveSearch);

        return topFiveSearch.stream()
                .map(calendarIdStr -> {
                    Long calendarId = Long.valueOf(calendarIdStr);
                    CalendarEntity calendarEntity = calendarRepository.findById(calendarId).orElse(null);
                    if (calendarEntity != null) {
                        String imgUrl = songService.getCalendarSongImageUrl(calendarEntity.getCalendarId());
                        Long chatroomId = chatroomService.getChatRoomIdByCalendar(calendarEntity);
                        return PopularSearchDto.toDto(calendarEntity, imgUrl, chatroomId);
                    } else {
                        return null;
                    }
                })
                .toList();

    }

    @Override
    @Transactional
    public List<PopularChatroomDto> getPopularChatRoom() {
        List<String> topChatrooms = getTopChatrooms();
        long remainingCount = chatRoomRepository.count() - topChatrooms.size();

        log.info("remaining count: {}", remainingCount);

        // 부족한 경우 채우기
        if (remainingCount > 0) {
            List<Long> excludeIds = topChatrooms.stream()
                    .map(Long::valueOf) // String -> Long 변환
                    .toList();

            List<ChatRoom> additionalChatrooms = chatRoomRepository.findByIdNotIn(excludeIds);

            // 추가된 데이터를 topChatrooms에 합치기
            topChatrooms.addAll(
                    additionalChatrooms.stream()
                            .map(chatRoom -> String.valueOf(chatRoom.getId())) // ChatRoom ID를 String으로 변환
                            .toList()
            );
        }

        log.info("topChatrooms: {}", topChatrooms);

        return topChatrooms.stream()
                .map(chatroomIdStr -> {
                    Long chatroomId = Long.valueOf(chatroomIdStr);
                    ChatRoom chatRoom = chatRoomRepository.findById(chatroomId).orElse(null);
                    if (chatRoom != null) {
                        long chatCnt = chatMessageRepository.countByChatRoomId(chatroomId);
                        return PopularChatroomDto.toDto(chatRoom, chatCnt);
                    } else {
                        return null;
                    }
                })
                .toList();
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailySearch() {
        String searchRedisKey = "dailySearch";
        String chatroomRedisKey = "dailyChatroom";

        // Redis에서 검색어와 채팅방 키 초기화
        redisTemplate.delete(searchRedisKey);
        redisTemplate.delete(chatroomRedisKey);

        log.info("Daily search terms and chatroom rankings have been reset.");
    }

    @Override
    @Transactional
    public void incrementDailySearch(Long calendarId) {
        String redisKey = "dailySearch";
        String calendarIdStr = String.valueOf(calendarId);
        redisTemplate.opsForZSet().incrementScore(redisKey, calendarIdStr, 1);
        List<String> topFiveSearchTerms = getTopFiveSearch();
        log.info("popularsearch : {}", topFiveSearchTerms);
    }

    @Override
    @Transactional
    public void incrementDailyChatroom(Long chatroomId) {
        String redisKey = "dailyChatroom";
        String calendarIdStr = String.valueOf(chatroomId);
        redisTemplate.opsForZSet().incrementScore(redisKey, calendarIdStr, 1);
        List<String> topChatrooms = getTopChatrooms();
        log.info("popularchatroom : {}", topChatrooms);

    }

    private List<String> getTopFiveSearch() {
        String redisKey = "dailySearch";

        // 상위 5개 항목만 가져오기
        Set<String> topKeywords = redisTemplate.opsForZSet().reverseRange(redisKey, 0, 4); // 0부터 4까지 = 상위 5개

        // null 또는 빈 결과 처리
        if (topKeywords == null || topKeywords.isEmpty()) {
            return new ArrayList<>();
        }

        // 결과를 List로 변환
        return new ArrayList<>(topKeywords);
    }

    private List<String> getTopChatrooms() {
        String redisKey = "dailyChatroom";

        // 상위 5개 항목만 가져오기
        Set<String> topKeywords = redisTemplate.opsForZSet().reverseRange(redisKey, 0, -1);

        // null 또는 빈 결과 처리
        if (topKeywords == null || topKeywords.isEmpty()) {
            return new ArrayList<>();
        }

        // 결과를 List로 변환
        return new ArrayList<>(topKeywords);
    }
}
