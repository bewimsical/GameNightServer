package edu.farmingdale.getgame.dto;

import lombok.*;


public class PartyUserDto {
    private UserDto user;
    private Boolean partyHost;

    public PartyUserDto() {
    }

    public PartyUserDto(UserDto user, Boolean partyHost) {
        this.user = user;
        this.partyHost = partyHost;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Boolean getPartyHost() {
        return partyHost;
    }

    public void setPartyHost(Boolean partyHost) {
        this.partyHost = partyHost;
    }

    // Constructors, getters, setters
}

