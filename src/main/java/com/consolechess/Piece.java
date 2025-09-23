package com.consolechess;

/**
 * Represents a chess piece with its type and color.
 */
public class Piece {
    private final PieceType type;
    private final PieceColor color;
    
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
        return color == PieceColor.WHITE ? symbol : symbol.toLowerCase();
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
