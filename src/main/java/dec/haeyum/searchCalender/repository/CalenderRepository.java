package dec.haeyum.searchCalender.repository;

import dec.haeyum.searchCalender.entity.CalenderEntity;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalenderRepository extends JpaRepository<CalenderEntity,Long> {
    @Query(
            value = "select * from calender order by calender_id DESC limit 1", nativeQuery = true
    )
    CalenderEntity findByLastData();

    @Query(
            value = "select * from calender where calender_date BETWEEN :startDate and :endDate"
            ,countQuery = "select  count(*) from calender where calender_date between :startDate and :endDate"
            ,nativeQuery = true
    )
    Page<CalenderEntity> findByCalenderNameBetween(@Param(value = "startDate") LocalDate startDate, @Param(value = "endDate") LocalDate endDate, Pageable page);
}
