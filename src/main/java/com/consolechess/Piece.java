package com.consolechess;

/**
 * Represents a chess piece with its type and color.
 */
public class Piece {
    private final PieceType type;
    private final PieceColor color;
    // ANSI color codes for nicer terminal rendering
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    private static final String ANSI_YELLOW = "\u001B[93m";
    
    // Configuration options
    private static final boolean USE_COLORS = true;
    
    public Piece(PieceType type, PieceColor color) {
        this.type = type;
        this.color = color;
    }
    
    public PieceType getType() {
        return type;
    }
    
    public PieceColor getColor() {
        return color;
    }
    
    /**
     * Returns chess symbols for pieces.
     * White pieces: Unicode U+2654-U+2659 (filled symbols)
     * Black pieces: Unicode U+265A-U+265F (filled symbols)
     */
    private String getChessSymbol() {
        if (color == PieceColor.WHITE) {
            // White pieces with Unicode escape sequences
            switch (type) {
                case KING: return "\u2654";     // White King
                case QUEEN: return "\u2655";    // White Queen  
                case ROOK: return "\u2656";     // White Rook
                case BISHOP: return "\u2657";   // White Bishop
                case KNIGHT: return "\u2658";   // White Knight
                case PAWN: return "\u2659";     // White Pawn
                default: return "?";
            }
        } else { // BLACK  
            // Black pieces with Unicode escape sequences
            switch (type) {
                case KING: return "\u265A";     // Black King
                case QUEEN: return "\u265B";    // Black Queen
                case ROOK: return "\u265C";     // Black Rook
                case BISHOP: return "\u265D";   // Black Bishop
                case KNIGHT: return "\u265E";   // Black Knight
                case PAWN: return "\u265F";     // Black Pawn
                default: return "?";
            }
        }
    }
    
    @Override
    public String toString() {
        String symbol = getChessSymbol();
        if (!USE_COLORS) {
            return symbol;
        }
        // White pieces in bright yellow, black pieces in bright cyan for contrast
        String colorCode = (color == PieceColor.WHITE) ? ANSI_YELLOW : ANSI_BRIGHT_CYAN;
        return colorCode + symbol + ANSI_RESET;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        return type == piece.type && color == piece.color;
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() * 31 + color.hashCode();
    }
}
