package com.example.Football.model.view;

import java.util.List;

public class PlayerPairDTO {
    private PlayerPlaytimeDTO player1;
    private PlayerPlaytimeDTO player2;

    public PlayerPairDTO(PlayerPlaytimeDTO player1, PlayerPlaytimeDTO player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public PlayerPlaytimeDTO getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerPlaytimeDTO player1) {
        this.player1 = player1;
    }

    public PlayerPlaytimeDTO getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerPlaytimeDTO player2) {
        this.player2 = player2;
    }
}

