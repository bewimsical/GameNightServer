package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
