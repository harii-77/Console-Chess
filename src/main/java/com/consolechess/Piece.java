package com.consolechess;

import java.util.Objects;

/**
 * Represents a chess piece with comprehensive display and utility functionality.
 * 
 * <p>This class encapsulates a chess piece with its type and color, providing
 * multiple display formats including ANSI colored terminal output, Unicode symbols,
 * and traditional algebraic notation.</p>
 * 
 * <p>The class supports various display modes:</p>
 * <ul>
 *   <li>Colored terminal output with ANSI escape codes</li>
 *   <li>Unicode chess symbols (♔♕♖♗♘♙ for white, ♚♛♜♝♞♟ for black)</li>
 *   <li>Traditional bracketed notation ([K] for white, (k) for black)</li>
 *   <li>Plain text notation (K, k)</li>
 * </ul>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public class Piece {
    
    // ANSI color codes for enhanced terminal rendering
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    private static final String ANSI_YELLOW = "\u001B[93m";
    private static final String ANSI_BRIGHT_WHITE = "\u001B[97m";
    private static final String ANSI_BRIGHT_BLACK = "\u001B[90m";
    
    // Configuration options
    private static final boolean USE_COLORS = true;
    private static final boolean USE_UNICODE_DEFAULT = false;
    
    /** Display mode enumeration for different piece representations */
    public enum DisplayMode {
        /** Colored bracketed notation: yellow [K] for white, cyan (k) for black */
        COLORED_BRACKETED,
        /** Unicode chess symbols: ♔ for white king, ♚ for black king */
        UNICODE,
        /** Plain bracketed notation: [K] for white, (k) for black */
        PLAIN_BRACKETED,
        /** Simple notation: K for white king, k for black king */
        SIMPLE
    }
    
    private final PieceType type;
    private final PieceColor color;
    
    /**
     * Constructs a Piece with the specified type and color.
     * 
     * @param type the type of chess piece (not null)
     * @param color the color of the piece (not null)
     * @throws IllegalArgumentException if type or color is null
     */
    public Piece(PieceType type, PieceColor color) {
        if (type == null) {
            throw new IllegalArgumentException("Piece type cannot be null");
        }
        if (color == null) {
            throw new IllegalArgumentException("Piece color cannot be null");
        }
        
        this.type = type;
        this.color = color;
    }
    
    /**
     * Returns the type of this chess piece.
     * 
     * @return the piece type (never null)
     */
    public PieceType getType() {
        return type;
    }
    
    /**
     * Returns the color of this chess piece.
     * 
     * @return the piece color (never null)
     */
    public PieceColor getColor() {
        return color;
    }
    
    /**
     * Returns the point value of this piece according to standard chess evaluation.
     * 
     * @return the point value (1 for pawn, 3 for knight/bishop, 5 for rook, 9 for queen, ∞ for king)
     */
    public int getPointValue() {
        return type.getPointValue();
    }
    
    /**
     * Checks if this piece is white.
     * 
     * @return true if this is a white piece, false otherwise
     */
    public boolean isWhite() {
        return color.isWhite();
    }
    
    /**
     * Checks if this piece is black.
     * 
     * @return true if this is a black piece, false otherwise
     */
    public boolean isBlack() {
        return color.isBlack();
    }
    
    /**
     * Checks if this piece is of the same color as another piece.
     * 
     * @param other the other piece to compare with (not null)
     * @return true if both pieces have the same color
     * @throws IllegalArgumentException if other piece is null
     */
    public boolean isSameColor(Piece other) {
        if (other == null) {
            throw new IllegalArgumentException("Other piece cannot be null");
        }
        return this.color == other.color;
    }
    
    /**
     * Checks if this piece is an opponent of another piece (different colors).
     * 
     * @param other the other piece to compare with (not null)
     * @return true if pieces have different colors
     * @throws IllegalArgumentException if other piece is null
     */
    public boolean isOpponentOf(Piece other) {
        if (other == null) {
            throw new IllegalArgumentException("Other piece cannot be null");
        }
        return this.color != other.color;
    }
    
    /**
     * Checks if this piece is a major piece (Queen or Rook).
     * 
     * @return true if this is a Queen or Rook
     */
    public boolean isMajorPiece() {
        return type.isMajorPiece();
    }
    
    /**
     * Checks if this piece is a minor piece (Knight or Bishop).
     * 
     * @return true if this is a Knight or Bishop
     */
    public boolean isMinorPiece() {
        return type.isMinorPiece();
    }
    
    /**
     * Checks if this piece is a sliding piece (Queen, Rook, or Bishop).
     * 
     * @return true if this piece can slide along ranks, files, or diagonals
     */
    public boolean isSlidingPiece() {
        return type.isSlidingPiece();
    }
    
    /**
     * Returns the Unicode chess symbol for this piece.
     * 
     * @return the Unicode symbol (e.g., ♔ for white king, ♚ for black king)
     */
    public String getUnicodeSymbol() {
        return type.getUnicodeSymbol(color);
    }
    
    /**
     * Returns chess symbols for pieces in bracketed notation.
     * White pieces: [P] [R] [N] [B] [Q] [K] (uppercase in brackets)
     * Black pieces: (p) (r) (n) (b) (q) (k) (lowercase in parentheses)
     * 
     * @return the bracketed chess symbol
     */
    public String getBracketedSymbol() {
        if (color.isWhite()) {
            // White pieces with brackets and uppercase letters
            return "[" + type.getNotation() + "]";
        } else {
            // Black pieces with parentheses and lowercase letters
            return "(" + type.getNotation().toLowerCase() + ")";
        }
    }
    
    /**
     * Returns a simple notation symbol for this piece.
     * 
     * @return uppercase for white pieces, lowercase for black pieces
     */
    public String getSimpleSymbol() {
        String notation = type.getNotation();
        return color.isWhite() ? notation : notation.toLowerCase();
    }
    
    /**
     * Returns a string representation of this piece in the specified display mode.
     * 
     * @param mode the display mode to use (not null)
     * @return formatted string representation
     * @throws IllegalArgumentException if mode is null
     */
    public String toString(DisplayMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Display mode cannot be null");
        }
        
        switch (mode) {
            case UNICODE:
                return getUnicodeSymbol();
            case PLAIN_BRACKETED:
                return getBracketedSymbol();
            case SIMPLE:
                return getSimpleSymbol();
            case COLORED_BRACKETED:
            default:
                return getColoredBracketedSymbol();
        }
    }
    
    /**
     * Returns the colored bracketed symbol with ANSI color codes.
     * 
     * @return colored symbol if colors are enabled, plain symbol otherwise
     */
    private String getColoredBracketedSymbol() {
        String symbol = getBracketedSymbol();
        
        if (!USE_COLORS) {
            return symbol;
        }
        
        // Enhanced color scheme: bright yellow for white, bright cyan for black
        String colorCode = color.isWhite() ? ANSI_YELLOW : ANSI_BRIGHT_CYAN;
        return colorCode + symbol + ANSI_RESET;
    }
    
    /**
     * Creates a piece from string notation and color.
     * Supports both full names ("Queen") and notation ("Q").
     * 
     * @param pieceString the piece type as string (not null)
     * @param color the piece color (not null)
     * @return a new Piece with the specified type and color
     * @throws IllegalArgumentException if arguments are invalid
     */
    public static Piece fromString(String pieceString, PieceColor color) {
        PieceType type = PieceType.fromString(pieceString);
        return new Piece(type, color);
    }
    
    /**
     * Creates a copy of this piece with a different color.
     * 
     * @param newColor the new color for the piece (not null)
     * @return a new Piece with the same type but different color
     * @throws IllegalArgumentException if newColor is null
     */
    public Piece withColor(PieceColor newColor) {
        if (newColor == null) {
            throw new IllegalArgumentException("New color cannot be null");
        }
        return new Piece(this.type, newColor);
    }
    
    /**
     * Creates a copy of this piece with a different type.
     * 
     * @param newType the new type for the piece (not null)
     * @return a new Piece with the same color but different type
     * @throws IllegalArgumentException if newType is null
     */
    public Piece withType(PieceType newType) {
        if (newType == null) {
            throw new IllegalArgumentException("New type cannot be null");
        }
        return new Piece(newType, this.color);
    }
    
    /**
     * Checks if two pieces are equal based on type and color.
     * 
     * @param obj the object to compare with
     * @return true if both pieces have the same type and color
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        return type == piece.type && color == piece.color;
    }
    
    /**
     * Generates a hash code based on type and color.
     * 
     * @return hash code for this piece
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
    
    /**
     * Returns the default string representation using colored bracketed notation.
     * White pieces appear in bright yellow, black pieces in bright cyan.
     * 
     * @return colored bracketed symbol (e.g., yellow "[K]" for white king)
     */
    @Override
    public String toString() {
        return toString(DisplayMode.COLORED_BRACKETED);
    }
}
