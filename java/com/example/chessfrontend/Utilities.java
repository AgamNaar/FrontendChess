package com.example.chessfrontend;

import com.example.chessfrontend.modulus.GameUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utility class for common functions used across the frontend of the chess application.
 */
public class Utilities {

    // FXML file paths for different pages
    public static final String CREATE_NEW_ACCOUNT_PAGE_PATH = "/com/example/chessfrontend/CreateNewAccountPage.fxml";
    public static final String GAME_LOBBY_PATH = "/com/example/chessfrontend/GameLobbyPage.fxml";
    public static final String CHESS_GAME_PATH_LOBBY = "/com/example/chessfrontend/ChessGamePage.fxml";

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
    public void goToPage(String pagePath, ActionEvent event) {
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
     * Displays a popup alert with the specified message.
     *
     * @param msg the message to be displayed in the alert
     */
    public void popupAlert(String msg) {
        // Create an Alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        // Show the alert
        alert.showAndWait();
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
}
