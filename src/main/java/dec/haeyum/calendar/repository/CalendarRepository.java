package dec.haeyum.calendar.repository;

import dec.haeyum.calendar.entity.CalendarEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

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



}
