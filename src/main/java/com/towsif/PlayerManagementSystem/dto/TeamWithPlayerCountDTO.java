package com.towsif.PlayerManagementSystem.dto;

import com.towsif.PlayerManagementSystem.entity.Team;

public class TeamWithPlayerCountDTO
{
    private Team team;
    private long playerCount;

    public TeamWithPlayerCountDTO()
    {
    }

    public TeamWithPlayerCountDTO(Team team, Long playerCount)
    {
        this.team = team;
        this.playerCount = playerCount;
    }

    public Team getTeam()
    {
        return team;
    }

    public void setTeam(Team team)
    {
        this.team = team;
    }

    public long getPlayerCount()
    {
        return playerCount;
    }

    public void setPlayerCount(long playerCount)
    {
        this.playerCount = playerCount;
    }
}
