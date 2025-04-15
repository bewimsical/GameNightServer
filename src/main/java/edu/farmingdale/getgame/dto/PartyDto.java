package edu.farmingdale.getgame.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;


public class PartyDto {
    private Long id;
    @JsonProperty("party_name")
    private String partyName;
    @JsonProperty("party_date")
    private LocalDateTime partyDate;
    private String location;

    public PartyDto() {
    }

    public PartyDto(String partyName, LocalDateTime partyDate, String location) {
        this.partyName = partyName;
        this.partyDate = partyDate;
        this.location = location;
    }

    public PartyDto(Long id, String partyName, LocalDateTime partyDate, String location) {
        this.id = id;
        this.partyName = partyName;
        this.partyDate = partyDate;
        this.location = location;
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
}
