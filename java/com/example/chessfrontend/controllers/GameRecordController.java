package com.example.chessfrontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Controller class for handling game records in the frontend.
 */
public class GameRecordController {

    // Constants for defining colors
    private static final String WIN_COLOR = "#89CE94";
    private static final String LOSE_COLOR = "#E54B4B";
    private static final String DRAW_COLOR = "#D1BEB0";

    // FXML elements injected from the corresponding FXML file
    @FXML
    private Text NameText1;

    @FXML
    private Text NameText2;

    @FXML
    private Text RatingText1;

    @FXML
    private Text RatingText2;

    @FXML
    private HBox RecordVBox;

    @FXML
    private Text GameIDText;

    /**
     * Sets the background color of the record box based on the outcome of the game.
     *
     * @param user       The name of the user whose game record is being displayed.
     * @param winnerName The name of the winner of the game.
     */
    public void setBackRoundColor(String user, String winnerName) {
        // Set background color based on the outcome
        if (user.equals(winnerName)) {
            // If the user is the winner, set background color to WIN_COLOR
            RecordVBox.setStyle("-fx-background-color: " + WIN_COLOR);
        } else if (winnerName == null || winnerName.equals("null")) {
            // If the game ended in a draw, set background color to DRAW_COLOR
            RecordVBox.setStyle("-fx-background-color: " + DRAW_COLOR);
        } else {
            // If the user is not the winner and the game didn't end in a draw, set background color to LOSE_COLOR
            RecordVBox.setStyle("-fx-background-color: " + LOSE_COLOR);
        }
    }


    /**
     * Getter method for NameText1.
     *
     * @return The Text element representing the name of player 1.
     */
    public Text getNameText1() {
        return NameText1;
    }

    /**
     * Getter method for NameText2.
     *
     * @return The Text element representing the name of player 2.
     */
    public Text getNameText2() {
        return NameText2;
    }

    /**
     * Getter method for RatingText1.
     *
     * @return The Text element representing the rating of player 1.
     */
    public Text getRatingText1() {
        return RatingText1;
    }

    /**
     * Getter method for RatingText2.
     *
     * @return The Text element representing the rating of player 2.
     */
    public Text getRatingText2() {
        return RatingText2;
    }

    /**
     * Gets the Text object representing the game ID.
     *
     * @return The Text object representing the game ID
     */
    public Text getGameIDText() {
        return GameIDText;
    }
}
