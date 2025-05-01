package edu.farmingdale.getgame.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="game_party")
@IdClass(GamePartyId.class)
public class GameParty {

    @Id
    @ManyToOne
    @JoinColumn(name="game_id")
    private Game game;

    @Id
    @ManyToOne
    @JoinColumn(name="party_id")
    private Party party;

    @Column(nullable = false)
    private int count = 1;

    public GameParty() {
    }

    public GameParty(Game game, Party party) {
        this.game = game;
        this.party = party;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameParty gameParty = (GameParty) o;
        return Objects.equals(game, gameParty.game) && Objects.equals(party, gameParty.party);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, party);
    }
}
