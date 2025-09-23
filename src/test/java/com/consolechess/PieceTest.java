package com.consolechess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Piece class.
 */
public class PieceTest {
    
    @Test
    public void testPieceCreation() {
        Piece whitePawn = new Piece(PieceType.PAWN, PieceColor.WHITE);
        assertEquals(PieceType.PAWN, whitePawn.getType());
        assertEquals(PieceColor.WHITE, whitePawn.getColor());
    }
    
    @Test
    public void testPieceToString() {
        Piece whitePawn = new Piece(PieceType.PAWN, PieceColor.WHITE);
        assertEquals("P", whitePawn.toString());
        
        Piece blackPawn = new Piece(PieceType.PAWN, PieceColor.BLACK);
        assertEquals("p", blackPawn.toString());
        
        Piece whiteQueen = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        assertEquals("Q", whiteQueen.toString());
        
        Piece blackKing = new Piece(PieceType.KING, PieceColor.BLACK);
        assertEquals("k", blackKing.toString());
    }
    
    @Test
    public void testPieceEquality() {
        Piece piece1 = new Piece(PieceType.ROOK, PieceColor.WHITE);
        Piece piece2 = new Piece(PieceType.ROOK, PieceColor.WHITE);
        Piece piece3 = new Piece(PieceType.ROOK, PieceColor.BLACK);
        Piece piece4 = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        
        assertEquals(piece1, piece2);
        assertNotEquals(piece1, piece3);
        assertNotEquals(piece1, piece4);
    }
}
