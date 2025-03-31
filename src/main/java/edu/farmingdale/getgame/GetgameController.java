package edu.farmingdale.getgame;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GetgameController {

    @GetMapping("/{id}")
    public String getGame(@PathVariable String id){
        APIClient client = new APIClient("boardgame",id);

        return client.getField("//name[@primary='true']");
    }
}
