package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long>
{
    Page<Performance> findPerformanceByDeletedFalse(Pageable pageable);

    Optional<Performance> findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(Long matchId, Long playerId);

    Page<Performance> findPerformanceByMatchIdAndDeletedFalseAndMatchDeletedFalse(Long id, Pageable pageable);

    Page<Performance> findPerformanceByPlayerIdAndDeletedFalseAndPlayerDeletedFalse(Long id, Pageable pageable);

    Page<Performance> findPerformanceByPlayerTeamIdAndDeletedFalse(Long teamId, Pageable pageable);

}
