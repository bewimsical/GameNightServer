package edu.farmingdale.getgame.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Entity
@Table(name = "user_party")
public class UserParty {

    @EmbeddedId
    private UserPartyId id = new UserPartyId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partyId")
    @JoinColumn(name = "party_id")
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "party_host")
    private Boolean partyHost;

    public UserParty(){}

    public UserParty(Party party, User user, Boolean partyHost) {
        this.party = party;
        this.user = user;
        this.partyHost = partyHost;
        this.id = new UserPartyId(party.getPartyId(), user.getUserId());
    }

    public UserPartyId getId() {
        return id;
    }

    public void setId(UserPartyId id) {
        this.id = id;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getPartyHost() {
        return partyHost;
    }

    public void setPartyHost(Boolean partyHost) {
        this.partyHost = partyHost;
    }

    @Override
    public String toString() {
        return "UserParty{" +
                "id=" + id +
                ", party=" + party +
                ", user=" + user +
                ", partyHost=" + partyHost +
                '}';
    }
}
