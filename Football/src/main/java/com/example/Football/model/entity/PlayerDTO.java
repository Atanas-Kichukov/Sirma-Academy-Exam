package com.example.Football.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class PlayerDTO extends BaseEntity{
    @Column(nullable = false,columnDefinition = "TEXT")
    private Integer teamNumber;
    @Column(nullable = false,columnDefinition = "TEXT")

    private String position;
    @Column(nullable = false,columnDefinition = "TEXT")

    private String fullName;
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private TeamDTO teamId;

    public PlayerDTO(Integer teamNumber, String position, String fullName, TeamDTO teamId) {
        this.teamNumber = teamNumber;
        this.position = position;
        this.fullName = fullName;
        this.teamId = teamId;
    }

    public PlayerDTO() {
    }

    public TeamDTO getTeamId() {
        return teamId;
    }

    public PlayerDTO setTeamId(TeamDTO teamId) {
        this.teamId = teamId;
        return this;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public PlayerDTO setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
        return this;
    }

    public String getPosition() {
        return position;
    }

    public PlayerDTO setPosition(String position) {
        this.position = position;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public PlayerDTO setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

}
