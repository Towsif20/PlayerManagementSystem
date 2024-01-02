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

    Optional<Performance> findPerformanceByIdAndDeletedFalse(Long id);

    Optional<Performance> findPerformanceByMatchIdAndPlayerIdAndDeletedFalse(Long matchId, Long playerId);

    Page<Performance> findPerformanceByMatchIdAndDeletedFalse(Long id, Pageable pageable);

    Page<Performance> findPerformanceByPlayerIdAndDeletedFalse(Long id, Pageable pageable);
}
