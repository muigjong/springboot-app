package me.shinsunyoung.springbootdeveloper.repository;


import me.shinsunyoung.springbootdeveloper.domain.Score;
import me.shinsunyoung.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query("SELECT SUM(s.scoreCorrect) FROM Score s WHERE s.user.userId = :userId")
    Long findTotalScoreByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM User u JOIN Score s ON u.userId = s.user.userId WHERE u.church = :church GROUP BY u.userId ORDER BY SUM(s.scoreCorrect) DESC")
    List<User> findUsersByChurchOrderedByTotalScore(@Param("church") String church);

    @Query("SELECT s FROM Score s WHERE s.user.church = ?1 ORDER BY s.scoreCorrect DESC")
    List<Score> findScoresByChurchOrdered(String church);

    @Query("SELECT SUM(s.scoreCorrect) FROM Score s JOIN s.user u WHERE u.church = :churchName")
    Long sumScoresByChurch(@Param("churchName") String churchName);

    @Query("SELECT u.church, SUM(s.scoreCorrect) FROM Score s JOIN s.user u GROUP BY u.church")
    List<Object[]> sumScoresByAllChurches();

//  이벤트
List<Score> findByUserAndScoreDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s.user, SUM(s.scoreCorrect) AS totalScore " +
            "FROM Score s " +
            "WHERE s.scoreDate BETWEEN :startDate AND :endDate " +
            "GROUP BY s.user " +
            "ORDER BY totalScore DESC")
    List<Object[]> findRankedScores(@Param("startDate") LocalDateTime startDate,
                                    @Param("endDate") LocalDateTime endDate);
}
