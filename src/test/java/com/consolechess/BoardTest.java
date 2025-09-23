package com.consolechess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Board class.
 */
public class BoardTest {
    private Board board;
    
    @BeforeEach
    public void setUp() {
        board = new Board();
    }
    
    @Test
    public void testInitialBoardSetup() {
        // Check that white pieces are in correct positions
        Position whiteRookPos = new Position(7, 0);
        Piece whiteRook = board.getPiece(whiteRookPos);
        assertNotNull(whiteRook);
        assertEquals(PieceType.ROOK, whiteRook.getType());
        assertEquals(PieceColor.WHITE, whiteRook.getColor());
        
        // Check that black pieces are in correct positions
        Position blackRookPos = new Position(0, 0);
        Piece blackRook = board.getPiece(blackRookPos);
        assertNotNull(blackRook);
        assertEquals(PieceType.ROOK, blackRook.getType());
        assertEquals(PieceColor.BLACK, blackRook.getColor());
    }
    
    @Test
    public void testValidPosition() {
        assertTrue(board.isValidPosition(new Position(0, 0)));
        assertTrue(board.isValidPosition(new Position(7, 7)));
        assertFalse(board.isValidPosition(new Position(-1, 0)));
        assertFalse(board.isValidPosition(new Position(0, -1)));
        assertFalse(board.isValidPosition(new Position(8, 0)));
        assertFalse(board.isValidPosition(new Position(0, 8)));
    }
    
    @Test
    public void testPawnMove() {
        // Test white pawn move from starting position
        Position from = new Position(6, 4); // e2
        Position to = new Position(5, 4);   // e3
        assertTrue(board.isValidMove(from, to, PieceColor.WHITE));
        
        // Test invalid pawn move (backwards)
        Position invalidTo = new Position(7, 4); // e1
        assertFalse(board.isValidMove(from, invalidTo, PieceColor.WHITE));
    }
    
    @Test
    public void testKnightMove() {
        // Test knight move from starting position
        Position from = new Position(7, 1); // b1
        Position to = new Position(5, 2);   // c3
        assertTrue(board.isValidMove(from, to, PieceColor.WHITE));
        
        // Test invalid knight move
        Position invalidTo = new Position(6, 1); // b2
        assertFalse(board.isValidMove(from, invalidTo, PieceColor.WHITE));
    }
    
    @Test
    public void testMakeMove() {
        Position from = new Position(6, 4); // e2
        Position to = new Position(5, 4);   // e3
        
        Piece originalPiece = board.getPiece(from);
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
}
