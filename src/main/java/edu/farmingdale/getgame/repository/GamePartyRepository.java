package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.model.GameParty;
import edu.farmingdale.getgame.model.GamePartyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GamePartyRepository extends JpaRepository<GameParty, GamePartyId> {
    @Query("SELECT gp.game.gameId, COUNT(gp.userId) FROM GameParty gp WHERE gp.party.partyId = :partyId GROUP BY gp.game.gameId")
    List<Object[]> findGameCountsByPartyId(@Param("partyId") Long partyId);
    @Query("SELECT g FROM Game g JOIN GameParty gp ON g.gameId = gp.game.gameId " +
            "WHERE gp.party.partyId = :partyId AND gp.userId = :userId")
    List<Game> findGamesSelectedByUserForParty(@Param("partyId") Long partyId, @Param("userId") Long userId);
}
