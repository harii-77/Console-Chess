package com.consolechess;

/**
 * Enum representing the types of chess pieces.
 */
public enum PieceType {
    PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;
    
    @Override
    public String toString() {
        switch (this) {
            case PAWN: return "P";
            case ROOK: return "R";
            case KNIGHT: return "N";
            case BISHOP: return "B";
            case QUEEN: return "Q";
            case KING: return "K";
            default: return "?";
        }
    }
}
