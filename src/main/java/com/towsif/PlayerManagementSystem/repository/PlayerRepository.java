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
public interface PlayerRepository extends JpaRepository<Player, Long>
{
    Page<Player> findPlayerByDeletedFalse(Pageable pageable);

    Optional<Player> findPlayerByIdAndDeletedFalse(Long id);

    List<Player> findPlayerByTeamIdAndDeletedFalseAndTeamDeletedFalse(Long id);

    Page<Player> findPlayerByTeamIdAndDeletedFalseAndTeamDeletedFalse(Pageable pageable, Long id);

    @Query(
            "SELECT m " +
                    "FROM Match m JOIN m.players p " +
                    "WHERE p.id = :id " +
                    "AND m.deleted = false " +
                    "AND p.deleted = false"
    )
    Page<Match> findMatchesByPlayerId(Long id, Pageable pageable);
}
