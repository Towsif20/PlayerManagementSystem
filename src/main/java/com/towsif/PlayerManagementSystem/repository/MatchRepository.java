package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>
{
    Page<Match> findMatchByDeletedFalse(Pageable pageable);

    Optional<Match> findMatchByIdAndDeletedFalse(Long id);

    Page<Match> findMatchByHomeTeamIdOrAwayTeamIdAndDeletedFalseAndHomeTeamDeletedFalseAndAwayTeamDeletedFalse(Long homeTeamId, Long awayTeamId, Pageable pageable);

}
