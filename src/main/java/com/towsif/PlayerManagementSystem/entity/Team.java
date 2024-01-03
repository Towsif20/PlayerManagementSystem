package com.towsif.PlayerManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Team Name cannot be empty")
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
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "team_sponsor",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "sponsor_id")
    )
    @JsonIgnore
    private List<Sponsor> sponsors;

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
}
