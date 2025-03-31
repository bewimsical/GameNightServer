package edu.farmingdale.getgame;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @GetMapping("/{query}")
    public List<Game> searchResults(@PathVariable String query){
        query = query.replaceAll(" ", "+");
        APIClient client = new APIClient("search", "?search="+query);
        return client.getSearchResults();
    }






}
