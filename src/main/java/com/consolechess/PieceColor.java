package com.consolechess;

/**
 * Enumeration representing the color of chess pieces.
 * 
 * <p>This enum provides utility methods for chess piece colors including
 * color manipulation, display formatting, and game logic support.</p>
 * 
 * <p>In chess, there are exactly two colors: WHITE and BLACK. White pieces
 * always move first in a standard game.</p>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public enum PieceColor {
    
    /** White pieces - moves first in chess */
    WHITE("White", "w", "Light"),
    
    /** Black pieces - moves second in chess */
    BLACK("Black", "b", "Dark");
    
    // Instance variables for enhanced functionality
    private final String displayName;
    private final String shortName;
    private final String alternateName;
    
    /**
     * Constructs a PieceColor with display properties.
     * 
     * @param displayName the full display name (e.g., "White")
     * @param shortName the abbreviated name (e.g., "w")
     * @param alternateName an alternative display name (e.g., "Light")
     */
    PieceColor(String displayName, String shortName, String alternateName) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.alternateName = alternateName;
    }
    
    /**
     * Returns the opposite color.
     * White returns Black, Black returns White.
     * 
     * @return the opposite piece color
     */
    public PieceColor opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
    
    /**
     * Returns the full display name of the color.
     * 
     * @return the display name (e.g., "White" or "Black")
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Returns the short name of the color.
     * 
     * @return the short name (e.g., "w" or "b")
     */
    public String getShortName() {
        return shortName;
    }
    
    /**
     * Returns an alternative name for the color.
     * 
     * @return the alternative name (e.g., "Light" or "Dark")
     */
    public String getAlternateName() {
        return alternateName;
    }
    
    /**
     * Checks if this color represents white pieces.
     * 
     * @return true if this is WHITE, false otherwise
     */
    public boolean isWhite() {
        return this == WHITE;
    }
    
    /**
     * Checks if this color represents black pieces.
     * 
     * @return true if this is BLACK, false otherwise
     */
    public boolean isBlack() {
        return this == BLACK;
    }
    
    /**
     * Parses a string to get the corresponding PieceColor.
     * Supports various input formats: full names, short names, alternate names.
     * Case-insensitive parsing.
     * 
     * @param colorString the string to parse (not null)
     * @return the corresponding PieceColor
     * @throws IllegalArgumentException if the string doesn't match any color
     */
    public static PieceColor fromString(String colorString) {
        if (colorString == null) {
            throw new IllegalArgumentException("Color string cannot be null");
        }
        
        String normalized = colorString.trim().toLowerCase();
        
        // Check full names
        if ("white".equals(normalized) || "light".equals(normalized)) {
            return WHITE;
        }
        if ("black".equals(normalized) || "dark".equals(normalized)) {
            return BLACK;
        }
        
        // Check short names
        if ("w".equals(normalized)) {
            return WHITE;
        }
        if ("b".equals(normalized)) {
            return BLACK;
        }
        
        throw new IllegalArgumentException("Unknown color: " + colorString);
    }
    
    /**
     * Returns the display name of the color.
     * 
     * @return the display name (e.g., "White" or "Black")
     */
    @Override
    public String toString() {
        return displayName;
    }
}
