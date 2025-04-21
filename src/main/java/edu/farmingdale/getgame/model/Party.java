package edu.farmingdale.getgame.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "partyId"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "party")
public class Party {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;
    private String partyName;
    private LocalDateTime partyDate;
    private String location;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserParty> userParties = new HashSet<>();

    public Party() {
    }
    public Party(Long partyId, String partyName, LocalDateTime partyDate, String location, Set<UserParty> userParties) {
        this.partyId = partyId;
        this.partyName = partyName;
        this.partyDate = partyDate;
        this.location = location;
        this.userParties = userParties;
    }


    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public LocalDateTime getPartyDate() {
        return partyDate;
    }

    public void setPartyDate(LocalDateTime partyDate) {
        this.partyDate = partyDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<UserParty> getUserParties() {
        return userParties;
    }

    public void setUserParties(Set<UserParty> userParties) {
        this.userParties = userParties;
    }

    @Override
    public String toString() {
        return "Party{" +
                "partyId=" + partyId +
                ", partyName='" + partyName + '\'' +
                ", partyDate=" + partyDate +
                ", location='" + location + '\'' +
                ", userParties=" + userParties +
                '}';
    }
}
