package edu.farmingdale.getgame;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/games", produces = "application/json")
public class GetgameController {

    @GetMapping("/{id}")
    public Game getGame(@PathVariable String id){
        APIClient client = new APIClient("boardgame",id);
        //int objectId, String name, int minPlayers, int maxPlayers, int playTime, String imgUrl
        String gameId = id;
        String name = client.getField("//name[@primary='true']");
        String minPlayers = client.getField("//minplayers");
        String maxPlayers = client.getField("//maxplayers");
        String playTime = client.getField("//playingtime");
        String imgUrl = client.getField("//thumbnail");
        String category = client.getField("//boardgamesubdomain");
        return new Game(gameId, name, minPlayers, maxPlayers, playTime, imgUrl, category);
    }
}
