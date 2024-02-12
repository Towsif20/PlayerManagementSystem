package com.towsif.PlayerManagementSystem.controller;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.repository.PerformanceRepository;
import com.towsif.PlayerManagementSystem.service.MatchService;
import com.towsif.PlayerManagementSystem.service.PerformanceService;
import com.towsif.PlayerManagementSystem.service.PlayerService;
import com.towsif.PlayerManagementSystem.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/performances")
public class PerformanceController
{
    private final PerformanceService performanceService;

    private final PerformanceRepository performanceRepository;

    private final PlayerService playerService;

    private final MatchService matchService;

    private final TeamService teamService;

    public PerformanceController(PerformanceService performanceService,
                                 PerformanceRepository performanceRepository,
                                 PlayerService playerService,
                                 MatchService matchService,
                                 TeamService teamService)
    {
        this.performanceService = performanceService;
        this.performanceRepository = performanceRepository;
        this.playerService = playerService;
        this.matchService = matchService;
        this.teamService = teamService;
    }

    @ModelAttribute
    public Player addPlayerToModel(@PathVariable(required = false) Long playerId)
    {
        if(playerId == null)
            return null;

        return playerService.findPlayerById(playerId);
    }

    @ModelAttribute
    public Match addMatchToModel(@PathVariable(required = false) Long matchId)
    {
        if(matchId == null)
            return null;

        return matchService.findMatchById(matchId);
    }

    @ModelAttribute
    public Team addTeamToModel(@PathVariable(required = false) Long teamId)
    {
        if(teamId == null)
            return null;

        return teamService.findTeamById(teamId);
    }

    @ModelAttribute
    public Performance addPerformanceToModel(@PathVariable(required = false) Long matchId,
                                             @PathVariable(required = false) Long playerId)
    {
        if(matchId == null || playerId == null)
            return null;

        return performanceRepository.findPerformanceByMatchIdAndPlayerIdAndDeletedFalseAndPlayerDeletedFalseAndMatchDeletedFalse(matchId, playerId)
                .orElse(new Performance(playerService.findPlayerById(playerId), matchService.findMatchById(matchId)));
    }

    @ModelAttribute("performancePage")
    public Page<Performance> addPerformancePageToModel(@PathVariable(required = false) Long matchId,
                                                       @PathVariable(required = false) Long playerId,
                                                       @PathVariable(required = false) Long teamId,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "id") String sortBy,
                                                       @RequestParam(defaultValue = "asc") String sortOrder)
    {
        if(matchId != null && (playerId == null && teamId == null))
        {
            return performanceService.findPerformanceByMatch(matchId, page, size, sortBy, sortOrder);
        }

        if(playerId != null && (matchId == null && teamId == null))
        {
            return performanceService.findPerformanceByPlayer(playerId, page, size, sortBy, sortOrder);
        }

        if(teamId != null && (playerId == null && matchId == null))
        {
            return performanceService.findPerformanceByTeam(teamId, page, size, sortBy, sortOrder);
        }

        return performanceService.findAllPerformances(page, size, sortBy, sortOrder);
    }

    @ModelAttribute("sortBy")
    public String addSortParameterToModel(@RequestParam(defaultValue = "id") String sortBy)
    {
        return sortBy;
    }

    @ModelAttribute("sortOrder")
    public String addSortOrderToModel(@RequestParam(defaultValue = "asc") String sortOrder)
    {
        return sortOrder;
    }

    @GetMapping
    public String showAllPerformances(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "asc") String sortOrder,
                                      @ModelAttribute("performancePage") Page<Performance> performancePage)
    {
        return "performances";
    }

    @GetMapping("/players/{playerId}")
    public String showAllPerformancesByPlayer(@PathVariable Long playerId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortOrder,
                                              @ModelAttribute Player player,
                                              @ModelAttribute("performancePage") Page<Performance> performancePage)
    {
        return "performances";
    }

    @GetMapping("/teams/{teamId}")
    public String showAllPerformancesByTeam(@PathVariable Long teamId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortOrder,
                                            @ModelAttribute Team team,
                                            @ModelAttribute("performancePage") Page<Performance> performancePage)
    {
        return "performances";
    }

    @GetMapping("/matches/{matchId}")
    public String showAllPerformancesByMatch(@PathVariable Long matchId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortOrder,
                                             @ModelAttribute Match match,
                                             @ModelAttribute("performancePage") Page<Performance> performancePage)
    {
        return "performances";
    }

    @GetMapping("/matches/{matchId}/players/{playerId}")
    public String showPerformanceByPlayerAndMatch(@PathVariable Long matchId,
                                                  @PathVariable Long playerId,
                                                  @ModelAttribute Performance performance)
    {
        return "performance";
    }

    @GetMapping("/matches/{matchId}/players/{playerId}/update")
    public String showPerformanceUpdatePage(@PathVariable Long matchId,
                                            @PathVariable Long playerId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortOrder,
                                            @ModelAttribute Match match,
                                            @ModelAttribute Player player)
    {
        return "save_performance";
    }

    @PostMapping("/matches/{matchId}/players/{playerId}/save")
    public String savePerformance(@PathVariable Long matchId,
                                  @PathVariable Long playerId,
                                  @Valid @ModelAttribute Performance performance, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "save_performance";
        }

        performanceService.savePerformance(performance);

        return "redirect:/performances/matches/" + matchId + "/players/" + playerId;
    }
}
