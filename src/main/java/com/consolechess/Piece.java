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
            // White pieces with ASCII symbols
            switch (type) {
                case KING: return "♔";     // King symbol
                case QUEEN: return "♕";    // Queen symbol  
                case ROOK: return "♖";     // Rook symbol
                case BISHOP: return "♗";   // Bishop symbol
                case KNIGHT: return "♘";   // Knight symbol
                case PAWN: return "♙";     // Pawn symbol
                default: return "?";
            }
        } else { // BLACK  
            // Black pieces with different ASCII symbols
            switch (type) {
                case KING: return "♚";     // Black King
                case QUEEN: return "♛";    // Black Queen
                case ROOK: return "♜";     // Black Rook
                case BISHOP: return "♝";   // Black Bishop
                case KNIGHT: return "♞";   // Black Knight
                case PAWN: return "♟";     // Black Pawn
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
