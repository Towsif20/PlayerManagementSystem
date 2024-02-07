package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.dto.TeamWithPlayerCountDTO;
import com.towsif.PlayerManagementSystem.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long>
{
    List<Team> findTeamByDeletedFalse();

    Optional<Team> findTeamByIdAndDeletedFalse(Long id);

    @Query("SELECT new com.towsif.PlayerManagementSystem.dto.TeamWithPlayerCountDTO(t, COUNT(p) as playerCount) " +
            "FROM Player p RIGHT JOIN p.team t " +
            "WHERE (p.deleted = false or p is null) AND t.deleted = false " +
            "GROUP BY t")
    Page<TeamWithPlayerCountDTO> findAllTeamsWithPlayerCount(Pageable pageable);
}
