package com.consolechess;

/**
 * Represents a chess piece with its type and color.
 */
public class Piece {
    private final PieceType type;
    private final PieceColor color;
    // ANSI color codes for nicer terminal rendering
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BRIGHT_WHITE = "\u001B[97m";
    private static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    // Toggle to disable colors if your terminal doesn't support ANSI
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
    
    @Override
    public String toString() {
        String symbol = type.toString();
        String rendered = color == PieceColor.WHITE ? symbol : symbol.toLowerCase();
        if (!USE_COLORS) {
            return rendered;
        }
        // White pieces in bright white, black pieces in bright cyan
        String colorCode = (color == PieceColor.WHITE) ? ANSI_BRIGHT_WHITE : ANSI_BRIGHT_CYAN;
        return colorCode + rendered + ANSI_RESET;
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
