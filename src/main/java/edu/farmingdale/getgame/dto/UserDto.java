package edu.farmingdale.getgame.dto;

public class UserDto {
    String username; //should we make this unique?
    String fName;
    String lName;
    String email;
    String profilePicUrl;

    public UserDto() {
    }
    public UserDto(String username, String fName, String lName, String email, String profilePicUrl) {
        this.username = username;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.profilePicUrl = profilePicUrl;
    }

    //efrat stuff
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    //end of


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
