package dec.haeyum.movie.repository;

import dec.haeyum.calendar.dto.response.Top5MoviesDto;
import dec.haeyum.movie.entity.CalendarMovieEntity;
import dec.haeyum.movie.entity.MoviePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarMovieRepository extends JpaRepository<CalendarMovieEntity, MoviePk> {
    @Query(
            value =
                    "select " +
                            "C.movie_id as movie_Id,"+
                            "C.ranking as ranking," +
                            "M.title as title," +
                            "M.img as img " +
                            "from calendar_movie as C " +
                            "inner join movie as M " +
                            "on C.movie_id = M.movie_id " +
                            "where C.calendar_id = :calendarId and ranking " +
                            "between 1 and 5 " +
                            "order by C.ranking asc",
            nativeQuery = true

    )
    Optional<List<Top5MoviesDto>> getTop5Movie(Long calendarId);

}
