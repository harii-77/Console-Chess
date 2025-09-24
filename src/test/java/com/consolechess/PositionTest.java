package com.consolechess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Position class.
 * Current coordinate system: (0,0) = a8 (top-left), (7,7) = h1 (bottom-right)
 */
public class PositionTest {
    
    @Test
    public void testPositionCreation() {
        Position pos = new Position(3, 4);
        assertEquals(3, pos.getRow());
        assertEquals(4, pos.getColumn());
    }
    
    @Test
    public void testPositionEquality() {
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(2, 3);
        Position pos3 = new Position(3, 2);
        
        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
    }
    
    @Test
    public void testPositionToString() {
        // Test corner positions based on current coordinate system
        Position topLeft = new Position(0, 0); // a8 (top-left corner)
        assertEquals("a8", topLeft.toString());
        
        Position topRight = new Position(0, 7); // h8 (top-right corner)  
        assertEquals("h8", topRight.toString());
        
        Position bottomLeft = new Position(7, 0); // a1 (bottom-left corner)
        assertEquals("a1", bottomLeft.toString());
        
        Position bottomRight = new Position(7, 7); // h1 (bottom-right corner)
        assertEquals("h1", bottomRight.toString());
        
        // Test middle positions
        Position center = new Position(3, 4); // e5 (center-ish)
        assertEquals("e5", center.toString());
        
        Position d4 = new Position(4, 3); // d4
        assertEquals("d4", d4.toString());
    }
    
    @Test
    public void testPositionHashCode() {
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(2, 3);
        
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }
    
    @Test
    public void testAllBoardPositions() {
        // Test that all positions generate valid chess notation
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                String notation = pos.toString();
                
                // Verify format: letter (a-h) + number (1-8)
                assertEquals(2, notation.length());
                char file = notation.charAt(0);
                char rank = notation.charAt(1);
                
                assertTrue(file >= 'a' && file <= 'h', "Invalid file: " + file);
                assertTrue(rank >= '1' && rank <= '8', "Invalid rank: " + rank);
            }
        }
    }
    
    private void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
