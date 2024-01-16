package com.towsif.PlayerManagementSystem.dto;

import com.towsif.PlayerManagementSystem.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
