package edu.farmingdale.getgame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class GameNightServerApplication {

    @Autowired
    //private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(GameNightServerApplication.class, args);
    }


}
