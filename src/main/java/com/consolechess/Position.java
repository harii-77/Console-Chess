package com.consolechess;

/**
 * Represents a position on a chess board using zero-indexed coordinates.
 * 
 * <p>This class provides comprehensive position handling for chess games,
 * including coordinate validation, algebraic notation conversion, and
 * various utility methods for chess-specific operations.</p>
 * 
 * <p>Chess board coordinates:</p>
 * <ul>
 *   <li>Rows: 0-7 (0=rank 8, 7=rank 1 in standard notation)</li>
 *   <li>Columns: 0-7 (0=file a, 7=file h in standard notation)</li>
 * </ul>
 * 
 * <p>Examples:</p>
 * <ul>
 *   <li>Position(0, 0) = "a8" (top-left corner)</li>
 *   <li>Position(7, 7) = "h1" (bottom-right corner)</li>
 *   <li>Position(7, 4) = "e1" (white king starting position)</li>
 * </ul>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public class Position {
    
    /** Minimum valid coordinate value (inclusive) */
    public static final int MIN_COORDINATE = 0;
    
    /** Maximum valid coordinate value (inclusive) */
    public static final int MAX_COORDINATE = 7;
    
    /** Total number of files (columns) on a chess board */
    public static final int BOARD_SIZE = 8;
    
    private final int row;
    private final int column;
    
    /**
     * Constructs a Position with the specified coordinates.
     * 
     * @param row the row coordinate (0-7, where 0 is rank 8, 7 is rank 1)
     * @param column the column coordinate (0-7, where 0 is file a, 7 is file h)
     * @throws IllegalArgumentException if coordinates are outside valid range [0-7]
     */
    public Position(int row, int column) {
        validateCoordinates(row, column);
        this.row = row;
        this.column = column;
    }
    
    /**
     * Creates a Position from algebraic notation (e.g., "e4", "a1", "h8").
     * 
     * @param algebraic the algebraic notation string (not null, length 2)
     * @return a new Position corresponding to the algebraic notation
     * @throws IllegalArgumentException if algebraic notation is invalid
     */
    public static Position fromAlgebraic(String algebraic) {
        if (algebraic == null || algebraic.length() != 2) {
            throw new IllegalArgumentException("Algebraic notation must be exactly 2 characters (e.g., 'e4')");
        }
        
        char file = Character.toLowerCase(algebraic.charAt(0));
        char rank = algebraic.charAt(1);
        
        if (file < 'a' || file > 'h') {
            throw new IllegalArgumentException("File must be between 'a' and 'h', got: " + file);
        }
        
        if (rank < '1' || rank > '8') {
            throw new IllegalArgumentException("Rank must be between '1' and '8', got: " + rank);
        }
        
        int column = file - 'a';  // a=0, b=1, ..., h=7
        int row = '8' - rank;     // 8=0, 7=1, ..., 1=7
        
        return new Position(row, column);
    }
    
    /**
     * Validates that coordinates are within the valid chess board range.
     * 
     * @param row the row coordinate to validate
     * @param column the column coordinate to validate
     * @throws IllegalArgumentException if either coordinate is outside [0-7]
     */
    private static void validateCoordinates(int row, int column) {
        if (row < MIN_COORDINATE || row > MAX_COORDINATE) {
            throw new IllegalArgumentException(
                String.format("Row must be between %d and %d, got: %d", 
                            MIN_COORDINATE, MAX_COORDINATE, row));
        }
        if (column < MIN_COORDINATE || column > MAX_COORDINATE) {
            throw new IllegalArgumentException(
                String.format("Column must be between %d and %d, got: %d", 
                            MIN_COORDINATE, MAX_COORDINATE, column));
        }
    }
    
    /**
     * Checks if the given coordinates represent a valid position on the chess board.
     * 
     * @param row the row coordinate to check
     * @param column the column coordinate to check
     * @return true if coordinates are valid (within [0-7]), false otherwise
     */
    public static boolean isValidPosition(int row, int column) {
        return row >= MIN_COORDINATE && row <= MAX_COORDINATE &&
               column >= MIN_COORDINATE && column <= MAX_COORDINATE;
    }
    
    /**
     * Returns the row coordinate (0-7).
     * 
     * @return the row coordinate, where 0 is rank 8 and 7 is rank 1
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Returns the column coordinate (0-7).
     * 
     * @return the column coordinate, where 0 is file a and 7 is file h
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * Returns the rank in chess notation (1-8).
     * 
     * @return the rank number, where 1 is the bottom rank and 8 is the top rank
     */
    public int getRank() {
        return 8 - row;
    }
    
    /**
     * Returns the file in chess notation ('a'-'h').
     * 
     * @return the file character, where 'a' is the leftmost file and 'h' is the rightmost
     */
    public char getFile() {
        return (char)('a' + column);
    }
    
    /**
     * Calculates the Manhattan distance to another position.
     * 
     * @param other the other position (not null)
     * @return the Manhattan distance (sum of horizontal and vertical distances)
     * @throws IllegalArgumentException if other position is null
     */
    public int manhattanDistanceTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Other position cannot be null");
        }
        return Math.abs(this.row - other.row) + Math.abs(this.column - other.column);
    }
    
    /**
     * Calculates the Chebyshev distance (king distance) to another position.
     * This is the maximum of horizontal and vertical distances.
     * 
     * @param other the other position (not null)
     * @return the Chebyshev distance (king moves required to reach the position)
     * @throws IllegalArgumentException if other position is null
     */
    public int chebyshevDistanceTo(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Other position cannot be null");
        }
        return Math.max(Math.abs(this.row - other.row), Math.abs(this.column - other.column));
    }
    
    /**
     * Checks if this position is on the same rank (row) as another position.
     * 
     * @param other the other position (not null)
     * @return true if both positions are on the same rank
     * @throws IllegalArgumentException if other position is null
     */
    public boolean isOnSameRank(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Other position cannot be null");
        }
        return this.row == other.row;
    }
    
    /**
     * Checks if this position is on the same file (column) as another position.
     * 
     * @param other the other position (not null)
     * @return true if both positions are on the same file
     * @throws IllegalArgumentException if other position is null
     */
    public boolean isOnSameFile(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Other position cannot be null");
        }
        return this.column == other.column;
    }
    
    /**
     * Checks if this position is on the same diagonal as another position.
     * 
     * @param other the other position (not null)
     * @return true if both positions are on the same diagonal
     * @throws IllegalArgumentException if other position is null
     */
    public boolean isOnSameDiagonal(Position other) {
        if (other == null) {
            throw new IllegalArgumentException("Other position cannot be null");
        }
        return Math.abs(this.row - other.row) == Math.abs(this.column - other.column);
    }
    
    /**
     * Checks if this position is on a light square (white square).
     * Light squares have an even sum of coordinates.
     * 
     * @return true if this is a light square, false if it's a dark square
     */
    public boolean isLightSquare() {
        return (row + column) % 2 == 0;
    }
    
    /**
     * Checks if this position is on a dark square (black square).
     * Dark squares have an odd sum of coordinates.
     * 
     * @return true if this is a dark square, false if it's a light square
     */
    public boolean isDarkSquare() {
        return !isLightSquare();
    }
    
    /**
     * Creates a new Position by adding the given offsets to this position.
     * Returns null if the resulting position would be outside the board.
     * 
     * @param rowOffset the row offset to add (can be negative)
     * @param columnOffset the column offset to add (can be negative)
     * @return a new Position with the offsets applied, or null if invalid
     */
    public Position add(int rowOffset, int columnOffset) {
        int newRow = this.row + rowOffset;
        int newColumn = this.column + columnOffset;
        
        if (isValidPosition(newRow, newColumn)) {
            return new Position(newRow, newColumn);
        }
        return null;
    }
    
    /**
     * Checks if two positions are equal.
     * 
     * @param obj the object to compare with
     * @return true if the positions have the same coordinates, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }
    
    /**
     * Generates a hash code for this position.
     * 
     * @return a hash code based on the row and column coordinates
     */
    @Override
    public int hashCode() {
        return row * BOARD_SIZE + column;
    }
    
    /**
     * Returns the algebraic notation representation of this position.
     * 
     * @return the algebraic notation (e.g., "e4", "a1", "h8")
     */
    @Override
    public String toString() {
        return String.valueOf(getFile()) + getRank();
    }
}
