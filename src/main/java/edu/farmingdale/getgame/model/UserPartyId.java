package edu.farmingdale.getgame.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserPartyId implements Serializable {

    @Column(name = "party_id")
    private Long partyId;

    @Column(name = "user_id")
    private Long userId;

    public UserPartyId() {}

    public UserPartyId(Long partyId, Long userId) {
        this.partyId = partyId;
        this.userId = userId;
    }

    // equals and hashCode required for composite key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPartyId)) return false;
        UserPartyId that = (UserPartyId) o;
        return Objects.equals(partyId, that.partyId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, userId);
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // getters and setters
}
