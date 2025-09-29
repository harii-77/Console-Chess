package com.consolechess;

/**
 * Enumeration representing the types of chess pieces.
 * 
 * <p>This enum provides comprehensive information about chess pieces including
 * their standard point values, movement characteristics, and display properties.</p>
 * 
 * <p>Chess pieces have traditional point values used for evaluation:
 * Pawn=1, Knight=3, Bishop=3, Rook=5, Queen=9, King=infinite</p>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public enum PieceType {
    
    /** Pawn piece - moves forward, captures diagonally */
    PAWN("Pawn", "P", 1, "\u2659", "\u265F"),
    
    /** Rook piece - moves horizontally and vertically */
    ROOK("Rook", "R", 5, "\u2656", "\u265C"),
    
    /** Knight piece - moves in L-shape */
    KNIGHT("Knight", "N", 3, "\u2658", "\u265E"),
    
    /** Bishop piece - moves diagonally */
    BISHOP("Bishop", "B", 3, "\u2657", "\u265D"),
    
    /** Queen piece - most powerful, combines rook and bishop moves */
    QUEEN("Queen", "Q", 9, "\u2655", "\u265B"),
    
    /** King piece - most important, game ends when captured */
    KING("King", "K", Integer.MAX_VALUE, "\u2654", "\u265A");
    
    // Instance variables for enhanced functionality
    private final String displayName;
    private final String notation;
    private final int pointValue;
    private final String whiteUnicodeSymbol;
    private final String blackUnicodeSymbol;
    
    /**
     * Constructs a PieceType with comprehensive properties.
     * 
     * @param displayName the full display name (e.g., "Knight")
     * @param notation the algebraic notation symbol (e.g., "N")
     * @param pointValue the traditional chess point value
     * @param whiteUnicodeSymbol Unicode symbol for white pieces
     * @param blackUnicodeSymbol Unicode symbol for black pieces
     */
    PieceType(String displayName, String notation, int pointValue, 
              String whiteUnicodeSymbol, String blackUnicodeSymbol) {
        this.displayName = displayName;
        this.notation = notation;
        this.pointValue = pointValue;
        this.whiteUnicodeSymbol = whiteUnicodeSymbol;
        this.blackUnicodeSymbol = blackUnicodeSymbol;
    }
    
    /**
     * Returns the full display name of the piece type.
     * 
     * @return the display name (e.g., "Knight", "Queen")
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Returns the algebraic notation symbol for the piece.
     * 
     * @return the notation symbol (e.g., "N" for Knight, "Q" for Queen)
     */
    public String getNotation() {
        return notation;
    }
    
    /**
     * Returns the traditional chess point value of the piece.
     * Used for position evaluation and material counting.
     * 
     * @return the point value (Pawn=1, Knight=3, Bishop=3, Rook=5, Queen=9, King=∞)
     */
    public int getPointValue() {
        return pointValue;
    }
    
    /**
     * Returns the Unicode symbol for this piece type in the specified color.
     * 
     * @param color the color of the piece (not null)
     * @return the Unicode chess symbol
     * @throws IllegalArgumentException if color is null
     */
    public String getUnicodeSymbol(PieceColor color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        return color.isWhite() ? whiteUnicodeSymbol : blackUnicodeSymbol;
    }
    
    /**
     * Returns the Unicode symbol for white pieces of this type.
     * 
     * @return the white Unicode chess symbol
     */
    public String getWhiteUnicodeSymbol() {
        return whiteUnicodeSymbol;
    }
    
    /**
     * Returns the Unicode symbol for black pieces of this type.
     * 
     * @return the black Unicode chess symbol
     */
    public String getBlackUnicodeSymbol() {
        return blackUnicodeSymbol;
    }
    
    /**
     * Checks if this piece type is a major piece (Queen or Rook).
     * Major pieces are the most valuable after the King.
     * 
     * @return true if this is a Queen or Rook, false otherwise
     */
    public boolean isMajorPiece() {
        return this == QUEEN || this == ROOK;
    }
    
    /**
     * Checks if this piece type is a minor piece (Knight or Bishop).
     * Minor pieces are developed early in the game.
     * 
     * @return true if this is a Knight or Bishop, false otherwise
     */
    public boolean isMinorPiece() {
        return this == KNIGHT || this == BISHOP;
    }
    
    /**
     * Checks if this piece type is a sliding piece (Queen, Rook, or Bishop).
     * Sliding pieces can move multiple squares in a direction.
     * 
     * @return true if this piece can slide along ranks, files, or diagonals
     */
    public boolean isSlidingPiece() {
        return this == QUEEN || this == ROOK || this == BISHOP;
    }
    
    /**
     * Parses a string to get the corresponding PieceType.
     * Supports both full names and notation symbols.
     * Case-insensitive parsing.
     * 
     * @param typeString the string to parse (not null)
     * @return the corresponding PieceType
     * @throws IllegalArgumentException if the string doesn't match any piece type
     */
    public static PieceType fromString(String typeString) {
        if (typeString == null) {
            throw new IllegalArgumentException("Type string cannot be null");
        }
        
        String normalized = typeString.trim().toLowerCase();
        
        // Check full names
        for (PieceType type : values()) {
            if (type.displayName.toLowerCase().equals(normalized) ||
                type.notation.toLowerCase().equals(normalized)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Unknown piece type: " + typeString);
    }
    
    /**
     * Returns all piece types except the King.
     * Useful for promotion choices and piece creation.
     * 
     * @return array of piece types excluding King
     */
    public static PieceType[] getPromotionPieces() {
        return new PieceType[]{QUEEN, ROOK, BISHOP, KNIGHT};
    }
    
    /**
     * Returns the algebraic notation symbol for the piece.
     * 
     * @return the notation symbol (e.g., "P", "N", "B", "R", "Q", "K")
     */
    @Override
    public String toString() {
        return notation;
    }
}
