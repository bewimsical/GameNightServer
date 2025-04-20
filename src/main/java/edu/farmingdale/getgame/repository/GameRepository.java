package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findGameByBggId(int bggId);
}
