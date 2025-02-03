package dec.haeyum.chat.service;

import dec.haeyum.calendar.entity.CalendarEntity;

public interface ChatroomService {
    Long getChatRoomIdByCalendar(CalendarEntity calendar);
}
