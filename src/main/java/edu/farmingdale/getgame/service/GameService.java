package edu.farmingdale.getgame.service;

import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private  final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames(){
        return gameRepository.findAll();
    }

    public Optional<Game> getGame(Integer id){
        return gameRepository.findById(id);
    }
    public Game saveGame(Game game){
        return gameRepository.save(game);
    }
    public void deleteGame(Game game){
        gameRepository.delete(game);
    }
    public boolean gameExists(Integer id){
        return gameRepository.existsById(id);
    }


}
