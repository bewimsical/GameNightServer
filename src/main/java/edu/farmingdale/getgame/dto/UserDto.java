package edu.farmingdale.getgame.dto;

public class UserDto {
    Long userId;
    String username; //should we make this unique?
    String fName;
    String lName;
    String email;
    String profilePicUrl;

    public UserDto() {
    }
    public UserDto(Long userId, String username, String fName, String lName, String email, String profilePicUrl) {
        this.userId = userId;
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }
}
