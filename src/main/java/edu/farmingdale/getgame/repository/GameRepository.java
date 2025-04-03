package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    //can add additional queries here - see JPA documentation
}
