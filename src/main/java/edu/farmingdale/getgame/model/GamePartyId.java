package edu.farmingdale.getgame.model;

import java.util.Objects;

public class GamePartyId {
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

    private int game;
    private Long party;


    public GamePartyId() {
    }

    public GamePartyId(int game, Long party) {
        this.game = game;
        this.party = party;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GamePartyId that = (GamePartyId) o;
        return game == that.game && Objects.equals(party, that.party);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, party);
    }
}
