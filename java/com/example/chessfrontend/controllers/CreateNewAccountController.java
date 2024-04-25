package com.example.chessfrontend.controllers;

import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.GameUser;
import com.example.chessfrontend.servercommunication.ServerRequestHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.example.chessfrontend.Utilities.*;
import static com.example.chessfrontend.servercommunication.ServerRequestHandler.HTTP_OK;

/**
 * Controller class for the create new account page UI.
 */
public class CreateNewAccountController {

    public Button CreateNewAccountButton;
    // Instance variables for server request handling and utilities
    ServerRequestHandler serverRequestHandler = new ServerRequestHandler();
    Utilities utilities = new Utilities();

    // FXML annotations to inject text fields for user input
    @FXML
    private TextField EmailText;

    @FXML
    private TextField PasswordText;

    @FXML
    private TextField RatingText;

    @FXML
    private TextField UserNameText;

    /**
     * Event handler method for "Create New Account" button click.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    void CreateNewAccountClicked(ActionEvent event) {
        // Check if any input parameters are invalid
        if (areParametersInvalid())
            return;

        // Create a GameUser object with user input data
        GameUser gameUser = new GameUser(UserNameText.getText(),
                EmailText.getText(),
                PasswordText.getText(),
                Integer.parseInt(RatingText.getText()));

        // Send create new account request to the server and get the response
        ServerResponse response = serverRequestHandler.createNewAccount(gameUser);

        // Handle null response (unexpected error)
        if (response == null) {
            utilities.popupAlert(UNEXPECTED_ERROR);
            return;
        }

        // If the server response indicates an error, display an alert with the error message
        if (response.getHttpStatus() != HTTP_OK)
            utilities.popupAlert(response.getResponse());
        else {
            // If account creation is successful, display success message, set user token, and redirect to game lobby
            utilities.popupAlert(SUCCESSFULLY_CREATED_ACCOUNT);
            gameUser.setToken(response.getResponse());
            utilities.setGameUser(gameUser);
            utilities.goToPage(GAME_LOBBY_PATH, event);
        }
    }

    /**
     * Method to validate input parameters.
     *
     * @return true if any of the input parameters are invalid, false otherwise
     */
    private boolean areParametersInvalid() {
        String email = EmailText.getText();
        String password = PasswordText.getText();
        String ratingStr = RatingText.getText();
        String userName = UserNameText.getText();

        // Check if any of the fields are empty
        if (email.isEmpty() || password.isEmpty() || ratingStr.isEmpty() || userName.isEmpty()) {
            utilities.popupAlert(MISSING_INFORMATION);
            return true;
        }

        // Check if rating is not a number or not within the specified range
        try {
            int rating = Integer.parseInt(ratingStr);
            if (rating < MIN_RATING || rating > MAX_RATING) {
                utilities.popupAlert(INVALID_ELO);
                return true;
            }
        } catch (NumberFormatException e) {
            utilities.popupAlert(INVALID_ELO);
            return true; // Rating is not a valid number
        }

        // All parameters are valid
        return false;
    }
}
