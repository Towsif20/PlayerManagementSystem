package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>
{
    Page<Match> findMatchByDeletedFalse(Pageable pageable);

    Optional<Match> findMatchByIdAndDeletedFalse(Long id);

    @Query(
            "SELECT p " +
                    "FROM Player p JOIN p.matches m " +
                    "WHERE m.id = :matchId " +
                    "and p.deleted = false"
    )
    List<Player> findPlayersByMatchId(Long matchId);

    @Query(
            "SELECT p " +
                    "FROM Player p JOIN p.matches m " +
                    "WHERE m.id = :matchId " +
                    "and p.deleted = false " +
                    "and p.team.id = m.homeTeam.id"
    )
    List<Player> findHomePlayersByMatchId(Long matchId);

    @Query(
            "SELECT p " +
                    "FROM Player p JOIN p.matches m " +
                    "WHERE m.id = :matchId " +
                    "and p.deleted = false " +
                    "and p.team.id = m.awayTeam.id"
    )
    List<Player> findAwayPlayersByMatchId(Long matchId);

    List<Match> findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalse(Long homeTeamId, Long awayTeamId);
    
    List<Match> findMatchByHomeTeamIdAndDeletedFalse(Long teamId);

    List<Match> findMatchByAwayTeamIdAndDeletedFalse(Long teamId);
}
