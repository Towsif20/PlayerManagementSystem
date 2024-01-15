package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Player;
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
    Page<Team> findTeamByDeletedFalse(Pageable pageable);

    List<Team> findTeamByDeletedFalse();

    Optional<Team> findTeamByIdAndDeletedFalse(Long id);
}
