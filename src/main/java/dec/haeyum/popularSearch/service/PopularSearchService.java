package dec.haeyum.popularSearch.service;

import dec.haeyum.chat.dto.ChatRoomDto;
import dec.haeyum.popularSearch.dto.PopularChatroomDto;
import dec.haeyum.popularSearch.dto.PopularSearchDto;

import java.util.List;

public interface PopularSearchService {
    List<PopularSearchDto> getPopularSearch();
    List<PopularChatroomDto> getPopularChatRoom();
    void resetDailySearch();
    void incrementDailySearch(Long calendarId);
    void incrementDailyChatroom(Long chatroomId);
}
