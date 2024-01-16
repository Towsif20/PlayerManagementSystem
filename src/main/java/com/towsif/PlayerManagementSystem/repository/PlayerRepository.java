package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>
{
    Page<Player> findPlayerByDeletedFalse(Pageable pageable);

    Optional<Player> findPlayerByIdAndDeletedFalse(Long id);


    @Transactional
    @Modifying
    @Query(value = "update Player set deleted = true, deleted_at = :deletedAt where id = :id", nativeQuery = true)
    void deletePlayerById(@Param("id") Long id, @Param("deletedAt") LocalDateTime deletedAt);

    List<Player> findPlayerByTeamIdAndDeletedFalse(Long id);

    Page<Player> findPlayerByTeamIdAndDeletedFalse(Pageable pageable, Long id);
}
