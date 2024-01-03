package com.towsif.PlayerManagementSystem.service;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Sponsor;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.MatchRepository;
import com.towsif.PlayerManagementSystem.repository.PlayerRepository;
import com.towsif.PlayerManagementSystem.repository.SponsorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PlayerService
{
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private PaginationAndSortingService paginationAndSortingService;


    public Player savePlayer(Player player)
    {
        player.setCreatedAt(LocalDateTime.now());

        return playerRepository.save(player);
    }

    public String addMatchToPlayer(Long playerId, Long matchId)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + playerId));

        Match match = matchRepository.findMatchByIdAndDeletedFalse(matchId)
                .orElseThrow(() -> new EntityNotFoundException("No match Found with id " + matchId));

        player.getMatches().add(match);

        playerRepository.save(player);

        return "Added match to player";
    }

    public List<Player> findAllPlayers(int page, int size, String sortBy, String sortOrder)
    {
        Pageable pageable = paginationAndSortingService.configurePaginationAndSorting(page, size, sortBy, sortOrder);

        return playerRepository.findPlayerByDeletedFalse(pageable).getContent();
    }

    public Player findPlayerById(Long id)
    {
        return playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));
    }

    @Transactional
    public String deletePlayerById(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        player.setDeleted(true);
        player.setDeletedAt(LocalDateTime.now());

        playerRepository.save(player);

//        playerRepository.deletePlayerById(id, LocalDateTime.now());

        return "Deleted";
    }

    @Transactional
    public Player updatePlayerById(Long id, Player playerFromRequest)
    {
        Player playerFromDB = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        playerFromRequest.setId(id);
        playerFromRequest.setUpdatedAt(LocalDateTime.now());
        playerFromRequest.setCreatedAt(playerFromDB.getCreatedAt());

        return playerRepository.save(playerFromRequest);
    }


    public List<Match> findMatchesByPlayerId(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        return player.getMatches();
    }

    public Team findTeamById(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        return player.getTeam();
    }

    public String addSponsorToPlayer(Long playerId, Long sponsorId)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(playerId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + playerId));

        Sponsor sponsor = sponsorRepository.findSponsorByIdAndDeletedFalse(sponsorId)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + sponsorId));

        player.getSponsors().add(sponsor);

        playerRepository.save(player);

        return "Added sponsor to player";
    }

    public List<Sponsor> findSponsorByPlayerId(Long id)
    {
        Player player = playerRepository.findPlayerByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("No Player Found with id " + id));

        return player.getSponsors();
    }
}
