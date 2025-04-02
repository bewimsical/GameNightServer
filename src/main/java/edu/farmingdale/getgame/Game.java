package edu.farmingdale.getgame;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "games")
public class Game {

    @Id
    private int objectId;

    @Column(nullable = false)
    private String name;

    private String minPlayers;
    private String maxPlayers;
    private String playTime;
    private String imgUrl;
    private String category;

    // Default Constructor
    public Game() {
    }

    // Parameterized Constructor
    public Game(int objectId, String name, String minPlayers, String maxPlayers, String playTime, String imgUrl, String category) {
        this.objectId = objectId;
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.playTime = playTime;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    // Getters and Setters
    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(String minPlayers) {
        this.minPlayers = minPlayers;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(String maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // toString Method
    @Override
    public String toString() {
        return "Game{" +
                "objectId=" + objectId +
                ", name='" + name + '\'' +
                ", minPlayers='" + minPlayers + '\'' +
                ", maxPlayers='" + maxPlayers + '\'' +
                ", playTime='" + playTime + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
