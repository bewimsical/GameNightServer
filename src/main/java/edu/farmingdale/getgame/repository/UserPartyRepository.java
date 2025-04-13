package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.Party;
import edu.farmingdale.getgame.model.User;
import edu.farmingdale.getgame.model.UserParty;
import edu.farmingdale.getgame.model.UserPartyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPartyRepository extends JpaRepository<UserParty, UserPartyId> {
    List<UserParty> findByUser_UserId(Long userId);
    List<UserParty> findByUser_UserIdAndPartyHostTrue(Long userId);
    List<UserParty> findByParty_PartyId(Long partyId);
    List<UserParty> findByParty_PartyIdAndPartyHostTrue(Long partyId);
    boolean existsByUserAndParty(User user, Party party);
    Optional<UserParty> findByUser_UserIdAndParty_PartyId(Long userId, Long partyId);
}
