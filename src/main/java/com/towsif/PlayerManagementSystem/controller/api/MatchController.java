package com.towsif.PlayerManagementSystem.controller.api;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.MatchService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController("MatchRestController")
@RequestMapping("/api/matches")
public class MatchController
{
    @Autowired
    private MatchService matchService;

    @PostMapping
    public Match saveMatch(@RequestBody @Valid Match match, BindingResult bindingResult,
                           @RequestParam(defaultValue = "1") Long homeTeamId,
                           @RequestParam(defaultValue = "2") Long awayTeamId)
    {
        if(bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return matchService.saveMatch(match, homeTeamId, awayTeamId);
    }

    @PostMapping("/{matchId}/players/add")
    public String addPlayersToMatch(@PathVariable("matchId") Long matchId,
                                    @RequestBody Map<String, List<Integer>> playerIdsMap)
    {
        matchService.addPlayersToMatch(matchId, playerIdsMap);

        return "Added players to match";
    }

    @GetMapping
    public List<Match> findAllMatches(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "asc") String sortOrder)
    {
        return matchService.findAllMatches(page, size, sortBy, sortOrder).getContent();
    }

    @GetMapping("/{id}")
    public Match findMatchById(@PathVariable("id") Long id)
    {
        return matchService.findMatchById(id);
    }

    @GetMapping("/{id}/teams")
    public List<Team> findTeams(@PathVariable("id") Long id)
    {
        return matchService.findTeams(id);
    }

    @DeleteMapping("/{id}")
    public String deleteMatchById(@PathVariable("id") Long id)
    {
        matchService.deleteMatchById(id);

        return "Deleted";
    }

    @PutMapping("/{id}")
    public Match updateMatchById(@PathVariable("id") Long id, @RequestBody @Valid Match match, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
            throw new ValidationException(message);
        }

        return matchService.updateMatchById(id, match);
    }

    @GetMapping("/{matchId}/players")
    public List<Player> findPlayersByMatchId(@PathVariable("matchId") Long matchId)
    {
        return matchService.findPlayersByMatchId(matchId);
    }

    @GetMapping("/{id}/runs")
    public long findRunsByMatchId(@PathVariable("id") Long id)
    {
        return matchService.findRunsByMatchId(id);
    }

    @GetMapping("/{matchId}/teams/{teamId}/runs")
    public long findRunsByTeamIdAndMatchId(@PathVariable("matchId") Long matchId, @PathVariable("teamId") Long teamId)
    {
        return matchService.findRunsByMatchIdAndPlayerId(matchId, teamId);
    }
}
