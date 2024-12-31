package dec.haeyum.movie.repository;

import dec.haeyum.movie.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity,Long> {


    @Query(
            value = "select movie_uuid from movie where movie_uuid in (:list)",
            nativeQuery = true
    )
    Set<Integer> findByMovieUuid(Set<Integer> list);


}
