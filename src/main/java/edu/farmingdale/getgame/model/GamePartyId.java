package edu.farmingdale.getgame.model;

import java.util.Objects;

public class GamePartyId {

    private int game;
    private Long party;
    private Long userId;

    public GamePartyId() {
    }


    public GamePartyId(int game, Long party, Long userId) {
        this.game = game;
        this.party = party;
        this.userId = userId;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public Long getParty() {
        return party;
    }

    public void setParty(Long party) {
        this.party = party;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GamePartyId that = (GamePartyId) o;
        return game == that.game && Objects.equals(party, that.party) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, party, userId);
    }
}
