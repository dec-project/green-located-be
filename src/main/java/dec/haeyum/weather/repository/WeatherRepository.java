package dec.haeyum.weather.repository;

import dec.haeyum.weather.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity,Long> {


    Optional<WeatherEntity> findByCalendarEntity_CalendarId(Long calendarId);

}
