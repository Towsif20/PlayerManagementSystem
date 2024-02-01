package com.towsif.PlayerManagementSystem.repository;

import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Long>
{
    Page<Sponsor> findSponsorByDeletedFalse(Pageable pageable);

    Optional<Sponsor> findSponsorByIdAndDeletedFalse(Long id);

    @Query(
            "SELECT p " +
                    "FROM Player p JOIN p.sponsors s " +
                    "WHERE s.id = :sponsorId " +
                    "AND p.deleted = false " +
                    "AND s.deleted = false"
    )
    List<Player> findPlayersBySponsorId(Long sponsorId);

    @Query(
            "SELECT t " +
                    "FROM Team t JOIN t.sponsors s " +
                    "WHERE s.id = :sponsorId " +
                    "AND t.deleted = false " +
                    "AND s.deleted = false"
    )
    List<Team> findTeamsBySponsorId(Long sponsorId);
}
