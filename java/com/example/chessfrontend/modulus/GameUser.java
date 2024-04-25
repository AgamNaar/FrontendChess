package com.example.chessfrontend.modulus;

import java.util.HashMap;
import java.util.Map;

/**
 * Entity class representing a game user.
 */
public class GameUser {

    private String userName;
    private String email;
    private String password;
    private int rating;
    private String token;

    /**
     * Default constructor.
     */
    public GameUser() {
    }

    /**
     * Constructor with username and rating.
     *
     * @param userName the username of the user
     * @param rating   the rating of the user
     */
    public GameUser(String userName, int rating) {
        this.userName = userName;
        this.rating = rating;
    }

    /**
     * Constructor with username and password.
     *
     * @param userName the username of the user
     * @param password the password of the user
     */
    public GameUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Constructor with all attributes.
     *
     * @param userName the username of the user
     * @param email    the email of the user
     * @param password the password of the user
     * @param rating   the rating of the user
     */
    public GameUser(String userName, String email, String password, int rating) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.rating = rating;
    }

    /**
     * Getter for the username.
     *
     * @return the username of the user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter for the email.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for the password.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for the rating.
     *
     * @return the rating of the user
     */
    public int getRating() {
        return rating;
    }

    /**
     * Getter for the token.
     *
     * @return the token of the user
     */
    public String getToken() {
        return token;
    }

    /**
     * Setter for the username.
     *
     * @param userName the username to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Setter for the email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter for the password.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter for the rating.
     *
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Setter for the token.
     *
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Overrides the toString() method to provide a string representation of the object.
     *
     * @return a string representation of the GameUser object
     */
    @Override
    public String toString() {
        return "GameUser{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rating=" + rating +
                ", token='" + token + '\'' +
                '}';
    }

    /**
     * Converts the GameUser object into JSON format.
     *
     * @return the JSON representation of the GameUser object
     */
    public String convertIntoJson() {
        Map<String, String> attributes = new HashMap<>();
        if (userName != null) {
            attributes.put("userName", userName);
        }
        if (email != null) {
            attributes.put("email", email);
        }
        if (password != null) {
            attributes.put("password", password);
        }
        if (rating != 0) {
            attributes.put("rating", String.valueOf(rating));
        }
        if (token != null) {
            attributes.put("token", token);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");

        int count = 0;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            stringBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
            if (++count < attributes.size()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
