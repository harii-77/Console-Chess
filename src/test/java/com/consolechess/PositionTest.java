package com.consolechess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Position class.
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
        Position pos = new Position(0, 0); // a1
        assertEquals("a1", pos.toString());
        
        pos = new Position(7, 7); // h8
        assertEquals("h8", pos.toString());
        
        pos = new Position(3, 4); // e4
        assertEquals("e4", pos.toString());
    }
    
    @Test
    public void testPositionHashCode() {
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(2, 3);
        
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }
}
