package com.towsif.PlayerManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Team implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String managerName;

    private Integer foundedYear;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonIgnore
    private LocalDateTime updatedAt;

    @JsonIgnore
    private LocalDateTime deletedAt;

    @JsonIgnore
    private boolean deleted = false;

    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private List<Sponsor> sponsors;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public Integer getFoundedYear()
    {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear)
    {
        this.foundedYear = foundedYear;
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

    public List<Sponsor> getSponsors()
    {
        return sponsors;
    }

    public void setSponsors(List<Sponsor> sponsors)
    {
        this.sponsors = sponsors;
    }

    @Override
    public String toString()
    {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", managerName='" + managerName + '\'' +
                ", foundedYear=" + foundedYear +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", deleted=" + deleted +
                '}';
    }

    @PrePersist
    private void setCreationTime()
    {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void setUpdateTime()
    {
        LocalDateTime time = LocalDateTime.now();

        if(this.deleted)
            deletedAt = time;

        updatedAt = time;
    }
}
