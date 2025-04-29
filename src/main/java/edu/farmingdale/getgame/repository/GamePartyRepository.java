package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.GameParty;
import edu.farmingdale.getgame.model.GamePartyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GamePartyRepository extends JpaRepository<GameParty, GamePartyId> {
    @Query("SELECT gp.game.gameId, gp.count FROM GameParty gp WHERE gp.party.partyId = :partyId")
    List<Object[]> findGameCountsByPartyId(@Param("partyId") Long partyId);
}
