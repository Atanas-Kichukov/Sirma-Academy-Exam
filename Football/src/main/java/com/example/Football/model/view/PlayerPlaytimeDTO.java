package com.example.Football.model.view;

public class PlayerPlaytimeDTO {
    private Long playerId;
    private String fullName;
    private String teamName;
    private int totalPlaytime;


    public PlayerPlaytimeDTO(Long playerId, String fullName, String teamName, int totalPlaytime) {
        this.playerId = playerId;
        this.fullName = fullName;
        this.teamName = teamName;
        this.totalPlaytime = totalPlaytime;
    }


    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTotalPlaytime() {
        return totalPlaytime;
    }

    public void setTotalPlaytime(int totalPlaytime) {
        this.totalPlaytime = totalPlaytime;
    }
}
