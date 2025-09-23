package com.consolechess;

/**
 * Represents a position on the chess board.
 * Row and column are 0-indexed (0-7).
 */
public class Position {
    private final int row;
    private final int column;
    
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getColumn() {
        return column;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }
    
    @Override
    public int hashCode() {
        return row * 8 + column;
    }
    
    @Override
    public String toString() {
        return (char)('a' + column) + String.valueOf(row + 1);
    }
}
