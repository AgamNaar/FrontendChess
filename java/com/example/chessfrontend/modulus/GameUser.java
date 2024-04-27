package com.example.chessfrontend.modulus;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Entity class representing a game user.
 */
public class GameUser {

    public static final String GAME_USER_RATING_USERNAME_JSON_REGEX = "GameUser\\{userName='(.*?)', rating=(\\d+)}";

    private final String userName;
    private String email;
    private String password;
    private int rating;
    private String token;

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

    /**
     * Parses a string representation of a GameUser object into a GameUser object.
     *
     * @param input The string representation of the GameUser.
     */
    public GameUser(String input) {
        // Define a regular expression pattern to match the GameUser representation
        Pattern pattern = Pattern.compile(GAME_USER_RATING_USERNAME_JSON_REGEX);

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);

        // Check if there is a match
        if (matcher.find()) {
            String userName = matcher.group(1);
            int rating = Integer.parseInt(matcher.group(2));

            this.userName = userName;
            this.rating = rating;
        } else {
            // Handle the case where there is no match
            throw new IllegalArgumentException("Invalid input string: " + input);
        }
    }
}
