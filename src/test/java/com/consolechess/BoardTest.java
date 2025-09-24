package com.consolechess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Board class with coin-style pieces.
 */
public class BoardTest {
    private Board board;
    
    @BeforeEach
    public void setUp() {
        board = new Board();
    }
    
    @Test
    public void testInitialBoardSetup() {
        // Check that white pieces are in correct positions (bottom rows)
        Position whiteRookPos = new Position(7, 0); // a1
        Piece whiteRook = board.getPiece(whiteRookPos);
        assertNotNull(whiteRook);
        assertEquals(PieceType.ROOK, whiteRook.getType());
        assertEquals(PieceColor.WHITE, whiteRook.getColor());
        
        Position whiteKingPos = new Position(7, 4); // e1
        Piece whiteKing = board.getPiece(whiteKingPos);
        assertNotNull(whiteKing);
        assertEquals(PieceType.KING, whiteKing.getType());
        assertEquals(PieceColor.WHITE, whiteKing.getColor());
        
        // Check that black pieces are in correct positions (top rows)
        Position blackRookPos = new Position(0, 0); // a8
        Piece blackRook = board.getPiece(blackRookPos);
        assertNotNull(blackRook);
        assertEquals(PieceType.ROOK, blackRook.getType());
        assertEquals(PieceColor.BLACK, blackRook.getColor());
        
        Position blackKingPos = new Position(0, 4); // e8
        Piece blackKing = board.getPiece(blackKingPos);
        assertNotNull(blackKing);
        assertEquals(PieceType.KING, blackKing.getType());
        assertEquals(PieceColor.BLACK, blackKing.getColor());
        
        // Check empty squares in the middle
        Position emptySquare = new Position(3, 3); // d5
        assertNull(board.getPiece(emptySquare));
    }
    
    @Test
    public void testValidPosition() {
        assertTrue(board.isValidPosition(new Position(0, 0))); // a8
        assertTrue(board.isValidPosition(new Position(7, 7))); // h1
        assertFalse(board.isValidPosition(new Position(-1, 0))); // Invalid
        assertFalse(board.isValidPosition(new Position(0, -1))); // Invalid
        assertFalse(board.isValidPosition(new Position(8, 0))); // Invalid
        assertFalse(board.isValidPosition(new Position(0, 8))); // Invalid
    }
    
    @Test
    public void testPawnMove() {
        // Test white pawn move from starting position
        Position from = new Position(6, 4); // e2
        Position to = new Position(5, 4);   // e3
        assertTrue(board.isValidMove(from, to, PieceColor.WHITE));
        
        // Test white pawn double move from starting position
        Position doubleTo = new Position(4, 4); // e4
        assertTrue(board.isValidMove(from, doubleTo, PieceColor.WHITE));
        
        // Test invalid pawn move (backwards)
        Position invalidTo = new Position(7, 4); // e1
        assertFalse(board.isValidMove(from, invalidTo, PieceColor.WHITE));
    }
    
    @Test
    public void testKnightMove() {
        // Test white knight move from starting position
        Position from = new Position(7, 1); // b1
        Position to = new Position(5, 2);   // c3
        assertTrue(board.isValidMove(from, to, PieceColor.WHITE));
        
        // Test another valid knight move
        Position to2 = new Position(5, 0);   // a3
        assertTrue(board.isValidMove(from, to2, PieceColor.WHITE));
        
        // Test invalid knight move
        Position invalidTo = new Position(6, 1); // b2
        assertFalse(board.isValidMove(from, invalidTo, PieceColor.WHITE));
    }
    
    @Test
    public void testMakeMove() {
        Position from = new Position(6, 4); // e2
        Position to = new Position(5, 4);   // e3
        
        Piece originalPiece = board.getPiece(from);
        assertNotNull(originalPiece);
        assertEquals(PieceType.PAWN, originalPiece.getType());
        
        board.makeMove(from, to);
        
        assertNull(board.getPiece(from));
        assertEquals(originalPiece, board.getPiece(to));
    }
    
    @Test
    public void testCannotCaptureOwnPiece() {
        Position from = new Position(7, 0); // a1 (white rook)
        Position to = new Position(7, 1);   // b1 (white knight)
        
        assertFalse(board.isValidMove(from, to, PieceColor.WHITE));
    }
    
    @Test
    public void testBoardToStringFormat() {
        String boardString = board.toString();
        
        // Check that board string contains expected elements
        assertTrue(boardString.contains("a   b   c   d   e   f   g   h"));
        assertTrue(boardString.contains("8"));
        assertTrue(boardString.contains("1"));
        
        // Should contain some pieces (we don't test exact format due to ANSI colors)
        assertTrue(boardString.length() > 100); // Board should be substantial
    }
    
    @Test
    public void testPieceDisplayFormat() {
        // Test that pieces display in coin format
        Position whitePawnPos = new Position(6, 0); // a2
        Piece whitePawn = board.getPiece(whitePawnPos);
        assertNotNull(whitePawn);
        String pawnStr = whitePawn.toString().replaceAll("\u001B\\[[0-9;]*m", ""); // Remove ANSI
        assertEquals("[P]", pawnStr);
        
        Position blackPawnPos = new Position(1, 0); // a7  
        Piece blackPawn = board.getPiece(blackPawnPos);
        assertNotNull(blackPawn);
        String blackPawnStr = blackPawn.toString().replaceAll("\u001B\\[[0-9;]*m", ""); // Remove ANSI
        assertEquals("(p)", blackPawnStr);
    }
}
