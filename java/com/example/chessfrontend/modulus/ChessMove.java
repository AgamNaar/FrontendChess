package com.example.chessfrontend.modulus;

import java.io.*;

/**
 * Represents a move of a piece on the chessboard.
 */
public class ChessMove implements Serializable {
    private byte currentPieceSquare;
    private byte targetSquare;
    private char typeOfPieceToPromoteTo;

    /**
     * Constructs a PieceMove object.
     *
     * @param piecePosition          Current position of the piece.
     * @param targetSquare           Target square the piece will move to.
     * @param typeOfPieceToPromoteTo Type of piece to promote to.
     */
    public ChessMove(byte piecePosition, byte targetSquare, char typeOfPieceToPromoteTo) {
        this.currentPieceSquare = piecePosition;
        this.targetSquare = targetSquare;
        this.typeOfPieceToPromoteTo = typeOfPieceToPromoteTo;
    }

    /**
     * Generates the string representation of the move.
     *
     * @return String representation of the move.
     */
    @Override
    public String toString() {
        return positionToNotation(currentPieceSquare) + positionToNotation(targetSquare) + typeOfPieceToPromoteTo;
    }

    /**
     * Converts a byte position on the board to algebraic notation.
     *
     * @param position Byte position on the board.
     * @return Algebraic notation for the position.
     */
    private String positionToNotation(byte position) {
        String row = String.valueOf((position / 8) + 1);
        char column = (char) ((7 - (position % 8)) + 'a');
        return column + row;
    }

    /**
     * Gets the current piece square.
     *
     * @return Current piece square.
     */
    public byte getCurrentPieceSquare() {
        return currentPieceSquare;
    }

    /**
     * Gets the target square.
     *
     * @return Target square.
     */
    public byte getTargetSquare() {
        return targetSquare;
    }

    /**
     * Retrieves the type of piece to promote to.
     *
     * @return the type of piece to promote to (e.g., 'q' for queen, 'r' for rook, 'n' for knight, 'b' for bishop)
     */
    public char getTypeOfPieceToPromoteTo() {
        return typeOfPieceToPromoteTo;
    }

    /**
     * Custom serialization using ObjectOutputStream.
     *
     * @param out ObjectOutputStream for serialization.
     * @throws IOException If an I/O error occurs during serialization.
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeByte(currentPieceSquare);
        out.writeByte(targetSquare);
        out.writeChar(typeOfPieceToPromoteTo);
    }

    /**
     * Custom deserialization using ObjectInputStream.
     *
     * @param in ObjectInputStream for deserialization.
     * @throws IOException            If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        currentPieceSquare = in.readByte();
        targetSquare = in.readByte();
        typeOfPieceToPromoteTo = in.readChar();
    }

}
