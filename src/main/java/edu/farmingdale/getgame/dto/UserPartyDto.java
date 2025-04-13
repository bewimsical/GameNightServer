package edu.farmingdale.getgame.dto;

import lombok.*;


public class UserPartyDto {
    private PartyDto party;
    private Boolean partyHost;

    public UserPartyDto() {
    }

    public UserPartyDto(PartyDto party, Boolean partyHost) {
        this.party = party;
        this.partyHost = partyHost;
    }

    public PartyDto getParty() {
        return party;
    }

    public void setParty(PartyDto party) {
        this.party = party;
    }

    public Boolean getPartyHost() {
        return partyHost;
    }

    public void setPartyHost(Boolean partyHost) {
        this.partyHost = partyHost;
    }

    // Constructors, getters, setters
}

