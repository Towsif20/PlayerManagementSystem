package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>
{
    Page<Performance> findPerformanceByDeletedFalse(Pageable pageable);

    Optional<Performance> findPerformanceByIdAndDeletedFalse(Long id);

    Optional<Performance> findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(Long matchId, Long playerId);

    Page<Performance> findPerformanceByMatchIdAndDeletedFalseAndMatchDeletedFalse(Long id, Pageable pageable);

    Page<Performance> findPerformanceByPlayerIdAndDeletedFalseAndPlayerDeletedFalse(Long id, Pageable pageable);

    @Query(
            "SELECT SUM(p.runs) " +
                    "FROM Performance p " +
                    "WHERE p.player.id=:id " +
                    "AND p.deleted = false " +
                    "And p.player.deleted = false"
    )
    Long findRunsByPlayerId(Long id);

    @Query(
            "SELECT SUM(p.runs) " +
                    "FROM Performance p " +
                    "WHERE p.match.id=:id " +
                    "AND p.deleted = false " +
                    "And p.match.deleted = false"
    )
    Long findRunsByMatchId(Long id);

    @Query("SELECT SUM(performance.runs) " +
            "FROM Performance performance " +
            "JOIN performance.player player " +
            "JOIN performance.match match " +
            "WHERE (match.homeTeam.id = :teamId OR match.awayTeam.id = :teamId) " +
            "AND match.id = :matchId " +
            "AND performance.deleted = false " +
            "And performance.player.deleted = false " +
            "And performance.match.deleted = false"
    )
    Long findTotalRunsInAMatchByTeamId(@Param("matchId") Long matchId, @Param("teamId") Long teamId);

    Page<Performance> findPerformanceByPlayerTeamIdAndDeletedFalse(Long teamId, Pageable pageable);
}
