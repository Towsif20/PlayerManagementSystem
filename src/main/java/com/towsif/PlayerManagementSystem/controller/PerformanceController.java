package com.towsif.PlayerManagementSystem.controller;

import com.towsif.PlayerManagementSystem.entity.Match;
import com.towsif.PlayerManagementSystem.entity.Performance;
import com.towsif.PlayerManagementSystem.entity.Player;
import com.towsif.PlayerManagementSystem.entity.Team;
import com.towsif.PlayerManagementSystem.service.MatchService;
import com.towsif.PlayerManagementSystem.service.PerformanceService;
import com.towsif.PlayerManagementSystem.service.PlayerService;
import com.towsif.PlayerManagementSystem.service.TeamService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/performances")
public class PerformanceController
{
    private final PerformanceService performanceService;

    private final PlayerService playerService;

    private final MatchService matchService;

    private final TeamService teamService;

    public PerformanceController(PerformanceService performanceService, PlayerService playerService, MatchService matchService, TeamService teamService)
    {
        this.performanceService = performanceService;
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

    @GetMapping
    public String showAllPerformances(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size,
                                      @RequestParam(defaultValue = "id") String sortBy,
                                      @RequestParam(defaultValue = "asc") String sortOrder,
                                      Model model)
    {
        Page<Performance> performancePage = performanceService.findAllPerformances(page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/players/{playerId}")
    public String showAllPerformancesByPlayer(@PathVariable Long playerId,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy,
                                              @RequestParam(defaultValue = "asc") String sortOrder,
                                              @ModelAttribute Player player,
                                              Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByPlayer(player, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/teams/{teamId}")
    public String showAllPerformancesByTeam(@PathVariable Long teamId,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy,
                                            @RequestParam(defaultValue = "asc") String sortOrder,
                                            @ModelAttribute Team team,
                                            Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByTeamId(team, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/matches/{matchId}")
    public String showAllPerformancesByMatch(@PathVariable Long matchId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "asc") String sortOrder,
                                             @ModelAttribute Match match,
                                             Model model)
    {
        Page<Performance> performancePage = performanceService.findPerformanceByMatch(match, page, size, sortBy, sortOrder);

        model.addAttribute("performancePage", performancePage);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performances";
    }

    @GetMapping("/matches/{matchId}/players/{playerId}")
    public String showPerformanceByPlayerAndMatch(@PathVariable Long matchId,
                                                  @PathVariable Long playerId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                  @RequestParam(defaultValue = "asc") String sortOrder,
                                                  @ModelAttribute Match match,
                                                  @ModelAttribute Player player,
                                                  Model model)
    {
        Performance performance = performanceService.findPerformanceByMatchAndPlayer(match, player);

        model.addAttribute("performance", performance);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "performance";
    }
}
