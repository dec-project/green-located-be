package dec.haeyum.calendar.repository;

import dec.haeyum.calendar.entity.CalendarEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity,Long> {
    @Query(
            value = "select * from calendar order by calendar_id DESC limit 1", nativeQuery = true
    )
    CalendarEntity findByLastData();

    @Query(
            value = "select * from calendar where calendar_date BETWEEN :startDate and :endDate"
            ,countQuery = "select  count(*) from calendar where calendar_date between :startDate and :endDate"
            ,nativeQuery = true
    )
    Page<CalendarEntity> findByCalendarNameBetween(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, Pageable page);

    @Query(value = "SELECT c FROM calendar c ORDER BY c.viewCount DESC")
    List<CalendarEntity> findTop5ByViewCount(Pageable pageable);

    Optional<CalendarEntity> findByCalendarDate(LocalDate calendarDate);

    @Query("SELECT c FROM calendar c WHERE c.calendarId NOT IN :excludeIds ORDER BY c.viewCount DESC")
    List<CalendarEntity> findTopByViewCountExcluding(@Param("excludeIds") List<Long> excludeIds, Pageable pageable);

    @Query("select c from calendar c where c.calendarId = :calendarId")
    Optional<CalendarEntity> findByIdForUpdate(Long calendarId);
}
