package com.example.chessfrontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

/**
 * Controller class for a player record in a leaderboard.
 */
public class PlayerRecordLeaderBoardController extends Region {

    @FXML
    private Text NameText; // Text field for displaying the player's name

    @FXML
    private Text PlaceText; // Text field for displaying the player's place in the leaderboard

    @FXML
    private Text RatingText; // Text field for displaying the player's rating

    /**
     * Gets the text field displaying the player's name.
     *
     * @return The text field for the player's name.
     */
    public Text getNameText() {
        return NameText;
    }

    /**
     * Gets the text field displaying the player's place in the leaderboard.
     *
     * @return The text field for the player's place.
     */
    public Text getPlaceText() {
        return PlaceText;
    }

    /**
     * Gets the text field displaying the player's rating.
     *
     * @return The text field for the player's rating.
     */
    public Text getRatingText() {
        return RatingText;
    }
}
