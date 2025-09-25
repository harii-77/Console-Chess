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
     * White pieces: [P] [R] [N] [B] [Q] [K] (uppercase in brackets)
     * Black pieces: (p) (r) (n) (b) (q) (k) (lowercase in parentheses)
     */
    private String getChessSymbol() {
        if (color == PieceColor.WHITE) {
            // White pieces with brackets and uppercase letters
            switch (type) {
                case KING: return "[K]";     // White King
                case QUEEN: return "[Q]";    // White Queen  
                case ROOK: return "[R]";     // White Rook
                case BISHOP: return "[B]";   // White Bishop
                case KNIGHT: return "[N]";   // White Knight
                case PAWN: return "[P]";     // White Pawn
                default: return "[?]";
            }
        } else { // BLACK  
            // Black pieces with parentheses and lowercase letters
            switch (type) {
                case KING: return "(k)";     // Black King
                case QUEEN: return "(q)";    // Black Queen
                case ROOK: return "(r)";     // Black Rook
                case BISHOP: return "(b)";   // Black Bishop
                case KNIGHT: return "(n)";   // Black Knight
                case PAWN: return "(p)";     // Black Pawn
                default: return "(?)";
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
