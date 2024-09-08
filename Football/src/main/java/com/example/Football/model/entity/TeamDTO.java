package com.example.Football.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teams")
public class TeamDTO extends BaseEntity{
    @Column(nullable = false,columnDefinition = "TEXT")
    private String name;
    @Column(nullable = false,columnDefinition = "TEXT")

    private String managerFullName;
    @Column(nullable = false,columnDefinition = "TEXT")
    private String teamGroup;

    public TeamDTO(String name, String managerFullName, String teamGroup) {
        this.name = name;
        this.managerFullName = managerFullName;
        this.teamGroup = teamGroup;
    }

    public TeamDTO() {
    }

    public String getName() {
        return name;
    }

    public TeamDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getManagerFullName() {
        return managerFullName;
    }

    public TeamDTO setManagerFullName(String managerFullName) {
        this.managerFullName = managerFullName;
        return this;
    }

    public String getTeamGroup() {
        return teamGroup;
    }

    public TeamDTO setTeamGroup(String teamGroup) {
        this.teamGroup = teamGroup;
        return this;
    }
}
