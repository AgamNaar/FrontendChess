package com.example.chessfrontend;

/**
 * Represents a response received from the server, including the response message and the HTTP status code.
 */
public class ServerResponse {

    // The response message from the server
    private final String response;
    // The HTTP status code
    private final int httpStatus;

    /**
     * Constructs a new ServerResponse with the given response message and HTTP status code.
     *
     * @param response   the response message from the server
     * @param httpStatus the HTTP status code
     */
    public ServerResponse(String response, int httpStatus) {
        this.response = response;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets the response message from the server.
     *
     * @return the response message
     */
    public String getResponse() {
        return response;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code
     */
    public int getHttpStatus() {
        return httpStatus;
    }
}
