package com.example.chessfrontend.servercommunication;

import com.example.chessfrontend.ServerResponse;
import com.example.chessfrontend.modulus.GameUser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.chessfrontend.modulus.ChessBoard.DEFAULT_PROMOTION;

/**
 * Handles communication with the server, including sending requests and receiving responses.
 */
public class ServerRequestHandler {
    // URL constants for server endpoints
    private static final String SERVER_URL = "http://localhost:8080";

    private static final String USER_URL = SERVER_URL + "/user";
    private static final String LOGIN_URL = USER_URL + "/login";
    private static final String CREATE_ACCOUNT_URL = USER_URL + "/create";
    private static final String GET_TOP_100_URL = USER_URL + "/get-top100";
    private static final String FIND_PLAYER_URL = USER_URL + "/get-user";

    private static final String MATCHMAKING_URL = SERVER_URL + "/matchmaking";
    private static final String FIND_MATCH_URL = MATCHMAKING_URL + "/find-match";
    private static final String JOIN_INVITATION_GAME_URL = MATCHMAKING_URL + "/join-invention/";
    private static final String CREATE_INVITATION_GAME_URL = MATCHMAKING_URL + "/create-invention";
    private static final String CANCEL_INVITATION_GAME_URL = MATCHMAKING_URL + "/cancel-invention";


    private static final String ONLINE_GAME_URL = SERVER_URL + "/game/online";
    private static final String SUBMIT_MOVE_URL = ONLINE_GAME_URL + "/submit-move/";
    private static final String GET_MATCH_PLAYER_URL = ONLINE_GAME_URL + "/get-players/";
    private static final String RESIGN_THE_GAME_URL = ONLINE_GAME_URL + "/resign/";
    private static final String GET_MATCH_HISTORY_URL = ONLINE_GAME_URL + "/match-history100";

    // HTTP request methods
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String EMPTY_PAYLOAD = "";

    // HTTP status codes
    public static final int HTTP_OK = 200;
    private static final int TIME_OUT_MILLISECOND = 20 * 1000;

    /**
     * Retrieves the match history for the specified game user from the server.
     *
     * @param gameUser The game user for whom to retrieve match history
     * @return A ServerResponse object containing the response from the server
     */
    public ServerResponse getMatchHistory(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), GET_MATCH_HISTORY_URL);
    }

    /**
     * Sends a request to join a game invitation with the specified game code ID and user information.
     *
     * @param gameCodedID The ID of the game invitation to join.
     * @param gameUser    The user information.
     * @return A ServerResponse indicating the status of the join request.
     */
    public ServerResponse joinGameInvention(String gameCodedID, GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), JOIN_INVITATION_GAME_URL + gameCodedID);
    }

    /**
     * Sends a request to create a game invitation with the specified user information.
     *
     * @param gameUser The user information.
     * @return A ServerResponse indicating the status of the create invitation request.
     */
    public ServerResponse createGameInvention(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), CREATE_INVITATION_GAME_URL);
    }

    /**
     * Sends a request to cancel a game invitation with the specified user information.
     *
     * @param gameUser The user information.
     * @return A ServerResponse indicating the status of the cancel invitation request.
     */
    public ServerResponse cancelGameInvention(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), CANCEL_INVITATION_GAME_URL);
    }


    /**
     * Sends a request to the server to resign the game with the specified game ID.
     *
     * @param gameID The ID of the game to resign.
     * @return A ServerResponse indicating the status of the resignation request.
     */
    public ServerResponse reignTheGame(GameUser user, int gameID) {
        String payload = user.convertIntoJson();
        // Send a POST request to the server to resign the game
        return sendNewRequestToServer(POST, payload, RESIGN_THE_GAME_URL + gameID);
    }

    /**
     * Retrieves the players involved in the match with the specified ID from the server.
     *
     * @param currentGameID The ID of the match to retrieve player information.
     * @return A ServerResponse containing the player information for the match.
     */
    public ServerResponse getPlayerOfMatch(int currentGameID) {
        // Send a GET request to the server to retrieve player information for the match
        return sendNewRequestToServer(GET, EMPTY_PAYLOAD, GET_MATCH_PLAYER_URL + currentGameID);
    }


    /**
     * Submits a move to the server for the specified game.
     *
     * @param gameUser            the user making the move
     * @param initialSquareNumber the square from which the piece is being moved
     * @param targetSquareNumber  the target square to which the piece is being moved
     * @param currentGameID       the ID of the current game
     * @return the server response to the move submission
     */
    public ServerResponse submitMove(GameUser gameUser, int initialSquareNumber,
                                     int targetSquareNumber, int currentGameID) {
        String jsonPayLoad = parseIntoJson(gameUser, initialSquareNumber, targetSquareNumber);
        return sendNewRequestToServer(POST, jsonPayLoad, SUBMIT_MOVE_URL + currentGameID);
    }

    /**
     * Finds a player with the specified username.
     *
     * @param playerFindUsername the username of the player to find
     * @return the server response containing information about the player
     */
    public ServerResponse findPlayer(String playerFindUsername) {
        return sendNewRequestToServer(POST, playerFindUsername, FIND_PLAYER_URL);
    }

    /**
     * Logs into the user account using the provided GameUser object.
     *
     * @param gameUser the GameUser object representing the user's credentials
     * @return the server response containing the login status and user information
     */
    public ServerResponse logIntoAccount(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), LOGIN_URL);
    }

    /**
     * Creates a new user account using the provided GameUser object.
     *
     * @param gameUser the GameUser object representing the new user's information
     * @return the server response containing the account creation status
     */
    public ServerResponse createNewAccount(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), CREATE_ACCOUNT_URL);
    }

    /**
     * Retrieves the top 100 players from the server.
     *
     * @return the server response containing information about the top 100 players
     */
    public ServerResponse getTop100Players() {
        return sendNewRequestToServer(GET, EMPTY_PAYLOAD, GET_TOP_100_URL);
    }

    /**
     * Initiates a matchmaking request to find a match against a random player.
     *
     * @param gameUser the GameUser object representing the current user
     * @return the server response containing information about the match
     */
    public ServerResponse findMatchVsRandom(GameUser gameUser) {
        return sendNewRequestToServer(POST, gameUser.convertIntoJson(), FIND_MATCH_URL);
    }


    /**
     * Sends a new HTTP request to the server with the specified method, payload, and URL address.
     * Handles both successful and error responses from the server.
     *
     * @param method     the HTTP method (e.g., "POST", "GET") for the request
     * @param payload    the payload or data to be sent with the request
     * @param urlAddress the URL address of the server endpoint to send the request to
     * @return a ServerResponse object containing the server's response to the request
     */
    private ServerResponse sendNewRequestToServer(String method, String payload, String urlAddress) {
        HttpURLConnection connection = null;
        try {
            // Contract a new http request to the server
            URL url = new URL(urlAddress);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIME_OUT_MILLISECOND);
            connection.setReadTimeout(TIME_OUT_MILLISECOND);
            connection.setDoOutput(true);

            // Only write to output stream if the request method is POST
            if ("POST".equals(method)) {
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
            }

            int responseCode = connection.getResponseCode();

            BufferedReader reader;
            if (responseCode >= 400) {
                // For error responses (status code 400 and above), read from error stream
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                // For successful responses (status code 200-399), read from input stream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }

            // Read the response from the server
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return new ServerResponse(response.toString(), responseCode);
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * Converts the specified game move information into a JSON string representation.
     *
     * @param gameUser            the GameUser object representing the user's information
     * @param initialSquareNumber the square number from which the piece is being moved
     * @param targetSquareNumber  the square number to which the piece is being moved
     * @return a JSON string representing the game move information
     */
    public String parseIntoJson(GameUser gameUser, int initialSquareNumber, int targetSquareNumber) {
        // Construct JSON string manually
        return "{" +
                "\"gameUser\": {" +
                "\"userName\": \"" + gameUser.getUserName() + "\"," +
                "\"token\": \"" + gameUser.getToken() + "\"" +
                "}," +
                "\"chessMove\": {" +
                "\"currentPieceSquare\": " + initialSquareNumber + "," +
                "\"targetSquare\": " + targetSquareNumber + "," +
                "\"typeOfPieceToPromoteTo\": \"" + DEFAULT_PROMOTION + "\"" +
                "}" +
                "}";
    }


}
