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

    @Id
    private Long userId;

    public GameParty() {
    }

    public GameParty(Long userId, Party party, Game game) {
        this.userId = userId;
        this.party = party;
        this.game = game;
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


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
