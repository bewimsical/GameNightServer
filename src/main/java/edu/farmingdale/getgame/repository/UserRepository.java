package edu.farmingdale.getgame.repository;

import edu.farmingdale.getgame.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
