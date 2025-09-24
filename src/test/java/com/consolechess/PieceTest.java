package com.consolechess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Piece class with coin-style representation.
 */
public class PieceTest {
    
    @Test
    public void testPieceCreation() {
        Piece whitePawn = new Piece(PieceType.PAWN, PieceColor.WHITE);
        assertEquals(PieceType.PAWN, whitePawn.getType());
        assertEquals(PieceColor.WHITE, whitePawn.getColor());
    }
    
    @Test
    public void testWhitePieceCoinStyle() {
        Piece whiteKing = new Piece(PieceType.KING, PieceColor.WHITE);
        assertEquals("[K]", whiteKing.toString().replaceAll("\u001B\\[[0-9;]*m", "")); // Remove ANSI colors
        
        Piece whiteQueen = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        assertEquals("[Q]", whiteQueen.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece whiteRook = new Piece(PieceType.ROOK, PieceColor.WHITE);
        assertEquals("[R]", whiteRook.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece whiteBishop = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        assertEquals("[B]", whiteBishop.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece whiteKnight = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        assertEquals("[N]", whiteKnight.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece whitePawn = new Piece(PieceType.PAWN, PieceColor.WHITE);
        assertEquals("[P]", whitePawn.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
    }
    
    @Test
    public void testBlackPieceCoinStyle() {
        Piece blackKing = new Piece(PieceType.KING, PieceColor.BLACK);
        assertEquals("(k)", blackKing.toString().replaceAll("\u001B\\[[0-9;]*m", "")); // Remove ANSI colors
        
        Piece blackQueen = new Piece(PieceType.QUEEN, PieceColor.BLACK);
        assertEquals("(q)", blackQueen.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece blackRook = new Piece(PieceType.ROOK, PieceColor.BLACK);
        assertEquals("(r)", blackRook.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece blackBishop = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        assertEquals("(b)", blackBishop.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece blackKnight = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        assertEquals("(n)", blackKnight.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
        
        Piece blackPawn = new Piece(PieceType.PAWN, PieceColor.BLACK);
        assertEquals("(p)", blackPawn.toString().replaceAll("\u001B\\[[0-9;]*m", ""));
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
    
    @Test
    public void testPieceHashCode() {
        Piece piece1 = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        Piece piece2 = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        
        assertEquals(piece1.hashCode(), piece2.hashCode());
    }
}
