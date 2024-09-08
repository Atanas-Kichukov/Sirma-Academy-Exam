package com.example.Football.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "records")
public class RecordsDto extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "player_id",referencedColumnName = "id",nullable = false)
    private PlayerDTO playerId;

    @ManyToOne
    @JoinColumn(name = "match_id",referencedColumnName = "id",nullable = false)
    private MatchesDTO matchId;
    @Column(name = "from_minutes",nullable = false)
    private Integer fromMinutes;
    @Column(name = "to_minutes",nullable = true)
    private Integer toMinutes;

    public RecordsDto(PlayerDTO playerId, MatchesDTO matchId, Integer fromMinutes, Integer toMinutes) {
        this.playerId = playerId;
        this.matchId = matchId;
        this.fromMinutes = fromMinutes;
        this.toMinutes = toMinutes;
    }

    public RecordsDto() {
    }

    public PlayerDTO getPlayerId() {
        return playerId;
    }

    public RecordsDto setPlayerId(PlayerDTO playerId) {
        this.playerId = playerId;
        return this;
    }

    public MatchesDTO getMatchId() {
        return matchId;
    }

    public RecordsDto setMatchId(MatchesDTO matchId) {
        this.matchId = matchId;
        return this;
    }

    public Integer getFromMinutes() {
        return fromMinutes;
    }

    public RecordsDto setFromMinutes(Integer fromMinutes) {
        this.fromMinutes = fromMinutes;
        return this;
    }

    public Integer getToMinutes() {
        return toMinutes;
    }

    public RecordsDto setToMinutes(Integer toMinutes) {
        this.toMinutes = toMinutes;
        return this;
    }
}
