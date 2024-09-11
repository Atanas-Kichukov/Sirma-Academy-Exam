package com.example.Football.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity()
@Table(name = "matches")
public class MatchesDTO extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "team_A_id", referencedColumnName = "id")
    private TeamDTO ATeamId;
    @ManyToOne
    @JoinColumn(name = "team_B_id", referencedColumnName = "id")
    private TeamDTO BTeamId;

    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)

    private String score;

    public MatchesDTO(TeamDTO ATeamId, TeamDTO BTeamId, LocalDate date, String score) {
        this.ATeamId = ATeamId;
        this.BTeamId = BTeamId;
        this.date = date;
        this.score = score;
    }

    public MatchesDTO() {
    }

    public TeamDTO getATeamId() {
        return ATeamId;
    }

    public MatchesDTO setATeamId(TeamDTO ATeamId) {
        this.ATeamId = ATeamId;
        return this;
    }

    public TeamDTO getBTeamId() {
        return BTeamId;
    }

    public MatchesDTO setBTeamId(TeamDTO BTeamId) {
        this.BTeamId = BTeamId;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public MatchesDTO setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public String getScore() {
        return score;
    }

    public MatchesDTO setScore(String score) {
        this.score = score;
        return this;
    }
}