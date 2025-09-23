package com.consolechess;

/**
 * Enum representing the color of chess pieces.
 */
public enum PieceColor {
    WHITE, BLACK;
    
    @Override
    public String toString() {
        return this == WHITE ? "White" : "Black";
    }
}
