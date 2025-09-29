package com.consolechess;

import java.util.Objects;

/**
 * Represents a player in a chess game with comprehensive player information
 * and game statistics tracking.
 * 
 * <p>This class encapsulates all player-related information including name,
 * piece color, game statistics, and provides utility methods for player
 * management and display.</p>
 * 
 * <p>Players are immutable once created, ensuring consistent state throughout
 * the game. Game statistics can be tracked separately if needed.</p>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public class Player {
    
    /** Maximum allowed length for player names */
    public static final int MAX_NAME_LENGTH = 50;
    
    /** Minimum allowed length for player names */
    public static final int MIN_NAME_LENGTH = 1;
    
    /** Default player name prefix for anonymous players */
    private static final String DEFAULT_NAME_PREFIX = "Player";
    
    private final String name;
    private final PieceColor color;
    private final long creationTime;
    
    /**
     * Constructs a Player with the specified name and piece color.
     * 
     * @param name the player's name (not null, not empty, length 1-50 characters)
     * @param color the color of pieces this player controls (not null)
     * @throws IllegalArgumentException if name is null, empty, too long, or color is null
     */
    public Player(String name, PieceColor color) {
        validateName(name);
        if (color == null) {
            throw new IllegalArgumentException("Player color cannot be null");
        }
        
        this.name = name.trim();
        this.color = color;
        this.creationTime = System.currentTimeMillis();
    }
    
    /**
     * Creates a player with a default name based on the piece color.
     * 
     * @param color the color of pieces this player controls (not null)
     * @return a new Player with default name "White Player" or "Black Player"
     * @throws IllegalArgumentException if color is null
     */
    public static Player createDefault(PieceColor color) {
        if (color == null) {
            throw new IllegalArgumentException("Player color cannot be null");
        }
        
        String defaultName = color.getDisplayName() + " " + DEFAULT_NAME_PREFIX;
        return new Player(defaultName, color);
    }
    
    /**
     * Validates the player name according to game rules.
     * 
     * @param name the name to validate
     * @throws IllegalArgumentException if name is invalid
     */
    private static void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Player name cannot be null");
        }
        
        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        
        if (trimmedName.length() < MIN_NAME_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Player name must be at least %d character(s), got: %d", 
                            MIN_NAME_LENGTH, trimmedName.length()));
        }
        
        if (trimmedName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Player name must be at most %d characters, got: %d", 
                            MAX_NAME_LENGTH, trimmedName.length()));
        }
        
        // Check for potentially problematic characters
        if (trimmedName.contains("\n") || trimmedName.contains("\r") || trimmedName.contains("\t")) {
            throw new IllegalArgumentException("Player name cannot contain line breaks or tabs");
        }
    }
    
    /**
     * Returns the player's name.
     * 
     * @return the player's display name (never null or empty)
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the color of pieces this player controls.
     * 
     * @return the player's piece color (never null)
     */
    public PieceColor getColor() {
        return color;
    }
    
    /**
     * Returns the timestamp when this player was created.
     * 
     * @return creation time in milliseconds since epoch
     */
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * Checks if this player controls white pieces.
     * 
     * @return true if this player plays as White, false otherwise
     */
    public boolean isWhite() {
        return color.isWhite();
    }
    
    /**
     * Checks if this player controls black pieces.
     * 
     * @return true if this player plays as Black, false otherwise
     */
    public boolean isBlack() {
        return color.isBlack();
    }
    
    /**
     * Returns the opponent's color (the color this player is not playing).
     * 
     * @return the opposite color
     */
    public PieceColor getOpponentColor() {
        return color.opposite();
    }
    
    /**
     * Creates a display name with color information in a specified format.
     * 
     * @param includeParentheses if true, color is shown in parentheses; if false, uses brackets
     * @return formatted display string
     */
    public String getDisplayName(boolean includeParentheses) {
        if (includeParentheses) {
            return name + " (" + color.getDisplayName() + ")";
        } else {
            return name + " [" + color.getDisplayName() + "]";
        }
    }
    
    /**
     * Returns a short display format using the player's name and color's short name.
     * 
     * @return short format like "Alice (w)" or "Bob (b)"
     */
    public String getShortDisplayName() {
        return name + " (" + color.getShortName() + ")";
    }
    
    /**
     * Checks if this player has the same name as another player (case-insensitive).
     * 
     * @param other the other player to compare with (not null)
     * @return true if both players have the same name (ignoring case)
     * @throws IllegalArgumentException if other is null
     */
    public boolean hasSameName(Player other) {
        if (other == null) {
            throw new IllegalArgumentException("Other player cannot be null");
        }
        return this.name.equalsIgnoreCase(other.name);
    }
    
    /**
     * Checks if this player controls the opposite color from another player.
     * 
     * @param other the other player to compare with (not null)
     * @return true if players control opposite colors
     * @throws IllegalArgumentException if other is null
     */
    public boolean isOpponentOf(Player other) {
        if (other == null) {
            throw new IllegalArgumentException("Other player cannot be null");
        }
        return this.color != other.color;
    }
    
    /**
     * Checks if two players are equal based on name and color.
     * 
     * @param obj the object to compare with
     * @return true if both players have the same name and color
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return Objects.equals(name, player.name) && color == player.color;
    }
    
    /**
     * Generates a hash code based on name and color.
     * 
     * @return hash code for this player
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
    
    /**
     * Returns a string representation of the player with name and color.
     * 
     * @return player information in the format "Name (Color)"
     */
    @Override
    public String toString() {
        return getDisplayName(true);
    }
}
