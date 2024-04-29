package com.example.chessfrontend.controllers;

import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.Utilities;
import com.example.chessfrontend.modulus.GameRecord;
import com.example.chessfrontend.modulus.GameUser;
import com.example.chessfrontend.servercommunication.GamePlayServant;
import com.example.chessfrontend.servercommunication.GamePlayService;
import com.example.chessfrontend.servercommunication.ServerRequestHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import static com.example.chessfrontend.Utilities.*;
import static com.example.chessfrontend.servercommunication.ServerRequestHandler.HTTP_OK;

public class GameLobbyController {

    @FXML
    private Button VsRandomButton;

    @FXML
    private Button NewInviteButton;


    @FXML
    private Button JoinInviteButton;

    @FXML
    private VBox ViewVbox;

    @FXML
    private Text CurrentRatingText;

    @FXML
    private TextField SearchPlayerNameText;

    @FXML
    private Text WelcomeText;

    @FXML
    private TextField GameCodeID;

    @FXML
    private Button ViewButton;

    @FXML
    private Text ViewText;


    private static final String CURRENT_RATING_TEXT = "rating: ";
    private static final String WELCOME_TEXT = "Welcome ";
    private static final String FAILED_TO_FIND_A_MATCH_TEXT = "Failed to find a match";
    private static final String COULD_NOT_FIND_PLAYER_TEXT = "Couldn't find that player in the server";
    private static final String FAILED_TO_LOAD_USER_INFO_TEXT = "Failed to load user info";
    private static final String SEARCH_FOR_GAME_TEXT = "Searching for game please wait...";
    private static final String COULD_NOT_JOIN_TEXT = "Could not join this game code ID";
    private static final String CHALLENGE_LINK_INVITATION_TEXT = "The challenge game ID invitation is: ";
    private static final String NEW_INVITE_CHALLENGE_TEXT = "New invite";
    private static final String CANCEL_INVITE_CHALLENGE_TEXT = "Cancel challenge";
    private static final String GAME_ID_TEXT = "GameID: ";
    private static final String MATCH_HISTORY_TEXT = "Match history";
    private static final String LEADERBOARD_TEXT = "Leaderboard";
    private static final String FONT_FOR_VIEW_TEXT = "Agency FB";
    private static final double BIG_FONT_FOR_VIEW = 40;
    private static final double SMALL_FONT_FOR_VIEW = 24;

    Utilities utilities = new Utilities();
    ServerRequestHandler serverRequestHandler = new ServerRequestHandler();
    private boolean waitingToAcceptChallenge = false;
    private boolean isRegistryLive = false;
    private boolean isLeaderBoardView = true;


    /**
     * Initializes the application.
     * This method sets up necessary components such as registry, user information, and leaderboard.
     * If any exception occurs during initialization, it displays an alert popup.
     */
    public void initialize() {
        try {
            // Check if the registry is live; if not, set it up
            if (!isRegistryLive)
                setUpRegistry();

            // Set up user information
            setUpUserInfo();

            // Set up the leaderboard
            setUpLeaderBoard();
        } catch (Exception e) {
            // Display an alert popup with the exception message
            utilities.waitPopupAlert(e.toString());
        }
    }

    /**
     * Sets up and displays the match history for a given user.
     *
     * @param user The user for whom the match history is to be displayed.
     * @throws IOException If an I/O exception occurs while retrieving the match history.
     */
    private void setUpMatchHistory(GameUser user) throws IOException {
        // Get the match history for the specified user from the server
        ServerResponse response = serverRequestHandler.getMatchHistory(user);

        // Check if the response is null or if the HTTP status is not OK
        if (response == null || response.getHttpStatus() != HTTP_OK)
            return;

        // Parse the list of game records from the response
        LinkedList<GameRecord> gameHistoryList = utilities.parseGameRecordList(response.getResponse());

        // Create a VBox to hold the player records
        VBox playerRecordsVBox = new VBox();
        playerRecordsVBox.setSpacing(3); // Adjust the spacing between elements if needed

        // Iterate over each game record in the list
        for (GameRecord gameRecord : gameHistoryList) {
            // Load the FXML file for the game record
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GAME_RECORD_PATH));
            Parent fxmlRoot = fxmlLoader.load();

            // Get the controller for the loaded FXML
            GameRecordController controller = fxmlLoader.getController();

            // Set the player names, ratings, and game ID in the controller
            controller.getNameText1().setText(gameRecord.getWhitePlayerName());
            controller.getNameText2().setText(gameRecord.getBlackPlayerName());
            controller.getRatingText1().setText(String.valueOf(gameRecord.getWhitePlayerRating()));
            controller.getRatingText2().setText(String.valueOf(gameRecord.getWhitePlayerRating()));
            controller.getGameIDText().setText(GAME_ID_TEXT + gameRecord.getGameID());
            controller.setBackRoundColor(user.getUserName(), gameRecord.getWinnerUserName());

            // Add the player record to the VBox
            playerRecordsVBox.getChildren().add(fxmlRoot);
        }

        // Create a ScrollPane and set its content to the VBox
        ScrollPane scrollPane = new ScrollPane(playerRecordsVBox);

        // Set the preferred height of the scroll pane
        scrollPane.setPrefHeight(ViewVbox.getPrefHeight()); // Adjust the height as needed

        // Add the scroll pane to the layout
        ViewVbox.getChildren().add(scrollPane);
    }


    /**
     * Sets up and displays the leaderboard by retrieving the top 100 players from the server.
     *
     * @throws IOException If an I/O exception occurs while retrieving the leaderboard data.
     */
    private void setUpLeaderBoard() throws IOException {
        // Retrieve the top 100 players from the server
        ServerResponse response = serverRequestHandler.getTop100Players();

        // Check if the response is null or if the HTTP status is not OK
        if (response == null || response.getHttpStatus() != HTTP_OK)
            return;

        // Parse the list of top players from the response
        LinkedList<GameUser> leaderBoardList = utilities.parseGameUserList(response.getResponse());

        // Create a VBox to hold the player records
        VBox playerRecordsVBox = new VBox();
        playerRecordsVBox.setSpacing(3); // Adjust the spacing between elements if needed

        // Iterate over each player in the leaderboard list
        for (int i = 0; i < leaderBoardList.size(); i++) {
            GameUser currentUser = leaderBoardList.get(i);

            // Load the FXML file for the player record
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(PLAYER_RECORD_PATH));
            Parent fxmlRoot = fxmlLoader.load();

            // Get the controller for the loaded FXML
            PlayerRecordController controller = fxmlLoader.getController();

            // Set the position, name, and rating in the controller
            controller.getPlaceText().setText(String.valueOf(i + 1));
            controller.getNameText().setText(currentUser.getUserName());
            controller.getRatingText().setText(String.valueOf(currentUser.getRating()));

            // Add the player record to the VBox
            playerRecordsVBox.getChildren().add(fxmlRoot);
        }

        // Create a ScrollPane and set its content to the VBox
        ScrollPane scrollPane = new ScrollPane(playerRecordsVBox);
        scrollPane.setFitToWidth(true); // Allow the scroll pane to resize horizontally

        // Set the preferred height of the scroll pane
        scrollPane.setPrefHeight(ViewVbox.getPrefHeight()); // Adjust the height as needed

        // Add the scroll pane to the layout
        ViewVbox.getChildren().add(scrollPane);
    }


    /**
     * Sets up and displays the user information by retrieving it from the server.
     * If the retrieval fails, it displays a popup alert.
     */
    private void setUpUserInfo() {
        // Find the player information for the current user from the server
        ServerResponse response = serverRequestHandler.findPlayer(nameToJson(utilities.getGameUser().getUserName()));

        // Check if the response is null or if the HTTP status is not OK (200)
        if (response == null || response.getHttpStatus() != 200)
            utilities.waitPopupAlert(FAILED_TO_LOAD_USER_INFO_TEXT);
        else {
            // If user information is successfully retrieved, update the UI with the user's details
            GameUser userFromDB = new GameUser(response.getResponse());
            utilities.getGameUser().setRating(userFromDB.getRating());
            WelcomeText.setText(WELCOME_TEXT + utilities.getGameUser().getUserName());
            CurrentRatingText.setText(CURRENT_RATING_TEXT + utilities.getGameUser().getRating());
        }
    }

    /**
     * Sets up the registry for the game play service.
     *
     * @throws RemoteException If a remote exception occurs while creating or rebinding the registry.
     */
    public void setUpRegistry() throws RemoteException {
        // Create a new registry on the specified port
        Registry registry = LocateRegistry.createRegistry(GamePlayService.PORT);

        // Create a new instance of the GamePlayServant
        GamePlayServant gamePlayServant = new GamePlayServant();

        // Rebind the servant to the registry using the user's token as the key
        registry.rebind(utilities.getGameUser().getToken(), gamePlayServant);

        // Update the utilities with the new GamePlayServant and set the registry status to live
        utilities.setGamePlayServant(gamePlayServant);
        isRegistryLive = true;
    }

    /**
     * Handles the click event of the "View" button.
     * Depending on the current view (leaderboard or match history), switches between them.
     * Clears the current view and sets up the new view accordingly.
     */
    @FXML
    void ViewButtonClicked() {
        try {
            // Clear the current view
            ViewVbox.getChildren().clear();

            // Switch between leaderboard and match history views
            if (isLeaderBoardView) {
                // If currently viewing leaderboard, switch to match history
                setUpMatchHistory(utilities.getGameUser());
                ViewButton.setText(LEADERBOARD_TEXT);
                ViewText.setText(MATCH_HISTORY_TEXT);
            } else {
                // If currently viewing match history, switch to leaderboard
                setUpLeaderBoard();
                ViewButton.setText(MATCH_HISTORY_TEXT);
                ViewText.setText(LEADERBOARD_TEXT);
            }

            // Toggle the view flag
            isLeaderBoardView = !isLeaderBoardView;

            // Set the font for the view text
            ViewText.setFont(Font.font(FONT_FOR_VIEW_TEXT, BIG_FONT_FOR_VIEW));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the click event of the "Find Match vs Random" button.
     * Initiates a search for a game against a random opponent.
     * If successful, proceeds to the chess game page.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void FindMatchVsRandomClicked(ActionEvent event) {
        // Display a non-blocking alert indicating that the game search is in progress
        utilities.noWaitPopupAlert(SEARCH_FOR_GAME_TEXT);

        // Send a request to the server to find a match against a random opponent
        ServerResponse response = serverRequestHandler.findMatchVsRandom(utilities.getGameUser());

        // Check if the response is valid and if the match search was successful
        if (response == null || response.getHttpStatus() != HTTP_OK)
            utilities.waitPopupAlert(FAILED_TO_FIND_A_MATCH_TEXT); // Display an alert for failure to find a match
        else {
            // If a match is found, set the current game ID and navigate to the chess game page
            utilities.setCurrentGameID(Integer.parseInt(response.getResponse()));
            utilities.goToPage(CHESS_GAME_PATH, event);
        }
    }

    /**
     * Handles the click event of the "Search Player" button.
     * Retrieves information about a player based on the entered username.
     * If successful, displays the player's match history.
     *
     * @throws IOException If an I/O exception occurs while retrieving player information.
     */
    @FXML
    void SearchPlayerClicked() throws IOException {
        // Send a request to the server to find the player based on the entered username
        ServerResponse response = serverRequestHandler.findPlayer(nameToJson(SearchPlayerNameText.getText()));

        // Check if the response is valid and if the player is found
        if (response == null || response.getHttpStatus() != HTTP_OK)
            utilities.waitPopupAlert(COULD_NOT_FIND_PLAYER_TEXT); // Display an alert if player not found
        else {
            // If player is found, clear the view, display player information, and show match history
            ViewVbox.getChildren().clear();
            GameUser playerFound = new GameUser(response.getResponse());
            ViewText.setText(playerFound.getUserName() + " " + CURRENT_RATING_TEXT + playerFound.getRating());
            ViewText.setFont(Font.font(FONT_FOR_VIEW_TEXT, SMALL_FONT_FOR_VIEW));

            setUpMatchHistory(playerFound); // Display the match history of the found player
        }
    }

    /**
     * Handles the click event of the "Join Invite" button.
     * Joins a game invitation using the provided game code ID.
     * If successful, proceeds to the chess game page.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void JoinInviteClicked(ActionEvent event) {
        String gameCodeID = GameCodeID.getText();
        // Send a request to the server to join the game invitation using the provided game code ID
        ServerResponse response = serverRequestHandler.joinGameInvention(gameCodeID, utilities.getGameUser());

        // Check if the response is valid and if the join operation is successful
        if (response != null && response.getHttpStatus() == HTTP_OK) {
            // If join is successful, set the current game ID and navigate to the chess game page
            utilities.setCurrentGameID(Integer.parseInt(response.getResponse()));
            System.out.println(utilities.getCurrentGameID());
            utilities.goToPage(CHESS_GAME_PATH, event);
        } else {
            // If join fails, display a non-blocking alert indicating failure to join
            utilities.noWaitPopupAlert(COULD_NOT_JOIN_TEXT);
        }
    }

    /**
     * Handles the click event of the "New Invite" button.
     * Initiates or cancels a game invitation.
     * Displays an invitation link upon successful initiation.
     *
     * @param event The action event triggered by clicking the button.
     */
    @FXML
    void NewInviteClicked(ActionEvent event) {
        ServerResponse response;
        if (waitingToAcceptChallenge) {
            // If currently waiting to accept challenge, cancel the game invitation
            response = serverRequestHandler.cancelGameInvention(utilities.getGameUser());
            NewInviteButton.setText(NEW_INVITE_CHALLENGE_TEXT); // Change button text to "New Invite"
        } else {
            // If not waiting to accept challenge, create a new game invitation
            response = serverRequestHandler.createGameInvention(utilities.getGameUser());
            NewInviteButton.setText(CANCEL_INVITE_CHALLENGE_TEXT); // Change button text to "Cancel Invite"
            utilities.getGamePlayServant().setEvent(event);
        }

        // Check if the response is valid and if the operation is successful
        if (response != null && response.getHttpStatus() == HTTP_OK && !waitingToAcceptChallenge)
            utilities.waitPopupAlert(CHALLENGE_LINK_INVITATION_TEXT + response.getResponse()); // Display invitation link
        disableButtons(!waitingToAcceptChallenge); // Disable join and vs random buttons based on the current state
        waitingToAcceptChallenge = !waitingToAcceptChallenge; // Toggle waiting state
    }

    /**
     * Disables or enables the "Join Invite" and "Vs Random" buttons based on the given action.
     *
     * @param action If true, buttons will be disabled; otherwise, enabled.
     */
    private void disableButtons(boolean action) {
        JoinInviteButton.setDisable(action);
        VsRandomButton.setDisable(action);
    }

    /**
     * Converts the entered username to a JSON string format.
     *
     * @param userName The username to be converted.
     * @return The username in JSON string format.
     */
    private String nameToJson(String userName) {
        return "{ \"userName\":\"" + userName + "\" }";
    }



}

