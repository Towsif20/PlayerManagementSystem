package com.towsif.PlayerManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Performance implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int runs;

    private int wickets;

    private int catches;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    private LocalDateTime deletedAt;

    @JsonIgnore
    private boolean deleted = false;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Player player;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    Match match;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public int getRuns()
    {
        return runs;
    }

    public void setRuns(int runs)
    {
        this.runs = runs;
    }

    public int getWickets()
    {
        return wickets;
    }

    public void setWickets(int wickets)
    {
        this.wickets = wickets;
    }

    public int getCatches()
    {
        return catches;
    }

    public void setCatches(int catches)
    {
        this.catches = catches;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt()
    {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public Match getMatch()
    {
        return match;
    }

    public void setMatch(Match match)
    {
        this.match = match;
    }

    @Override
    public String toString()
    {
        return "Performance{" +
                "id=" + id +
                ", runs=" + runs +
                ", wickets=" + wickets +
                ", catches=" + catches +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", deleted=" + deleted +
                '}';
    }
}
