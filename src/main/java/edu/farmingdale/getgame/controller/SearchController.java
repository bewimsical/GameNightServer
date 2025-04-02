package edu.farmingdale.getgame.controller;

import edu.farmingdale.getgame.service.APIClient;
import edu.farmingdale.getgame.model.SearchGame;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/search", produces = "application/json")
public class SearchController {

    @GetMapping("/{query}")
    public List<SearchGame> searchResults(@PathVariable String query){
        query = query.replaceAll(" ", "+");
        APIClient client = new APIClient("search", "?search="+query);
        return client.getSearchResults();
    }






}
