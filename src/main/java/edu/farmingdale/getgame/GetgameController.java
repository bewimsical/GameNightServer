package edu.farmingdale.getgame;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "https://boardgamegeek.com/xmlapi", consumes = MediaType.APPLICATION_XML_VALUE)
public class GetgameController {
}
