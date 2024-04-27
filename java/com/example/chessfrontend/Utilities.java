package com.example.chessfrontend;

import com.example.chessfrontend.modulus.GameUser;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.chessfrontend.modulus.GameUser.GAME_USER_RATING_USERNAME_JSON_REGEX;

/**
 * Utility class for common functions used across the frontend of the chess application.
 */
public class Utilities {

    // FXML file paths for different pages
    public static final String URL = "/com/example/chessfrontend/";
    public static final String CREATE_NEW_ACCOUNT_PAGE_PATH = URL + "CreateNewAccountPage.fxml";
    public static final String GAME_LOBBY_PATH = URL + "GameLobbyPage.fxml";
    public static final String CHESS_GAME_LOBBY_PATH = URL + "ChessGamePage.fxml";
    public static final String PLAYER_LEADER_BOARD_TEMPLATE_PATH = URL + "PlayerLeaderBoardTemplate.fxml";

    // Constants for rating limits
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 3000;

    // Messages
    public static final String SUCCESSFULLY_CREATED_ACCOUNT = "Your account has been successfully created!";
    public static final String UNEXPECTED_ERROR = "Unexpected error has occurred";
    public static final String SUCCESSFULLY_LODGED_IN = "You have successfully logged in!";
    public static final String MISSING_INFORMATION = "Please fill all fields";
    public static final String INVALID_ELO = "Invalid elo, must be a whole number between "
            + MIN_RATING + " - " + MAX_RATING;

    // Current user's game information
    private static GameUser gameUser;
    private static int currentGameID;

    /**
     * Navigates to the specified FXML page upon an action event.
     *
     * @param pagePath the path to the FXML file of the target page
     * @param event    the action event triggering the navigation
     */
    public void goToPage(String pagePath, Event event) {
        Node sourceNode = (Node) event.getSource();
        Stage stage = (Stage) sourceNode.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pagePath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error loading the new page: " + e);
        }
    }

    /**
     * Displays a popup alert with the specified message, and wait till player closes it.
     *
     * @param msg the message to be displayed in the alert
     */
    public void waitPopupAlert(String msg) {
        // Create an Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Show the alert
        alert.showAndWait();
    }

    /**
     * Displays a popup alert with the specified message.
     *
     * @param msg the message to be displayed in the alert
     */
    public void noWaitPopupAlert(String msg) {
        // Create an Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Show the alert
        alert.show();
    }


    /**
     * Sets the current game ID.
     *
     * @param currentGameID the ID of the current game
     */
    public void setCurrentGameID(int currentGameID) {
        Utilities.currentGameID = currentGameID;
    }

    /**
     * Retrieves the current game ID.
     *
     * @return the ID of the current game
     */
    public int getCurrentGameID() {
        return currentGameID;
    }

    /**
     * Retrieves the game user.
     *
     * @return the current game user
     */
    public GameUser getGameUser() {
        return gameUser;
    }

    /**
     * Sets the game user.
     *
     * @param gameUser the game user to be set
     */
    public void setGameUser(GameUser gameUser) {
        Utilities.gameUser = gameUser;
    }

    /**
     * Parses a string representation of a list of GameUser objects into a list of GameUser objects.
     *
     * @param input The string representation of the list.
     * @return A list of GameUser objects.
     */
    public LinkedList<GameUser> parseList(String input) {
        LinkedList<GameUser> userList = new LinkedList<>();

        // Define a regular expression pattern to match GameUser representations
        Pattern pattern = Pattern.compile(GAME_USER_RATING_USERNAME_JSON_REGEX);

        // Create a matcher for the input string
        Matcher matcher = pattern.matcher(input);

        // Iterate through the matches and extract userName and rating
        while (matcher.find()) {
            String userName = matcher.group(1);
            int rating = Integer.parseInt(matcher.group(2));

            // Creating a new GameUser object and adding it to the list
            userList.add(new GameUser(userName, rating));
        }

        return userList;
    }

}
