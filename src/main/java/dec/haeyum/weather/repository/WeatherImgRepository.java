package dec.haeyum.weather.repository;

import dec.haeyum.weather.entity.WeatherImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherImgRepository extends JpaRepository<WeatherImgEntity, Long> {

    Optional<WeatherImgEntity> findByWeatherImgName(String weatherImgName);
}
