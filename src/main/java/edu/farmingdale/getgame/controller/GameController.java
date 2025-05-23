package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.service.APIClient;
import edu.farmingdale.getgame.model.Game;
import edu.farmingdale.getgame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/games", produces = "application/json")
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable int id){
        int gameId = id; //TODO validate the id to make sure it is a number
        Optional<Game> game = gameService.getGameByBggId(gameId);
        if(game.isEmpty()){
            APIClient client = new APIClient("boardgame", String.valueOf(id));
            String name = client.getField("//name[@primary='true']");//TODO add backup that doesnt use primary
            int minPlayers = Integer.parseInt(client.getField("//minplayers"));
            int maxPlayers = Integer.parseInt(client.getField("//maxplayers"));
            int playTime = Integer.parseInt(client.getField("//playingtime"));
            String imgUrl = client.getField("//thumbnail");
            String category = client.getField("//boardgamesubdomain");
            game = Optional.of(new Game(gameId, name, minPlayers, maxPlayers, playTime, imgUrl, category));

            gameService.saveGame(game.get());//TODO make sure the api request didnt fail before saving
        }
        return game.get();
    }

}
