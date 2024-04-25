package com.example.chessfrontend.controllers;

import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.GameUser;
import com.example.chessfrontend.servercommunication.ServerRequestHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import static com.example.chessfrontend.Utilities.*;
import static com.example.chessfrontend.servercommunication.ServerRequestHandler.HTTP_OK;

/**
 * Controller class for the login page UI.
 */
public class LoginPageController {

    // Instance variables to handle server requests and utilities
    ServerRequestHandler serverRequestHandler = new ServerRequestHandler();
    Utilities utilities = new Utilities();

    // FXML annotations to inject text fields for user input
    @FXML
    private TextField UserNameText;

    @FXML
    private TextField PasswordText;

    /**
     * Event handler method for "Create New Account" button click.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    void CreateNewAccountClicked(ActionEvent event) {
        // Redirects to the create new account page
        utilities.goToPage(CREATE_NEW_ACCOUNT_PAGE_PATH, event);
    }

    /**
     * Event handler method for "Login" button click.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    void LoginClicked(ActionEvent event) {
        // Create a GameUser object with username and password from input fields
        GameUser gameUser = new GameUser(UserNameText.getText(), PasswordText.getText());

        // Send login request to the server and get the response
        ServerResponse response = serverRequestHandler.logIntoAccount(gameUser);

        // Handle null response (unexpected error)
        if (response == null) {
            utilities.popupAlert(UNEXPECTED_ERROR);
            return;
        }

        // If the server response indicates an error, display an alert with the error message
        if (response.getHttpStatus() != HTTP_OK)
            utilities.popupAlert(response.getResponse());
        else {
            // If login is successful, display success message, set user token, and redirect to game lobby
            utilities.popupAlert(SUCCESSFULLY_LODGED_IN);
            gameUser.setToken(response.getResponse());
            utilities.setGameUser(gameUser);
            utilities.goToPage(GAME_LOBBY_PATH, event);
        }
    }

}
