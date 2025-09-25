package com.consolechess;

import java.util.*;

/**
 * Represents the chess board and handles piece placement and movement validation.
 */
public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece[][] squares;
    
    // Castling rights tracking
    private boolean whiteKingMoved;
    private boolean blackKingMoved;
    private boolean whiteKingsideRookMoved;
    private boolean whiteQueensideRookMoved;
    private boolean blackKingsideRookMoved;
    private boolean blackQueensideRookMoved;
    
    // En passant tracking
    private Position enPassantTarget; // The square that can be captured en passant
    private int lastMoveNumber; // Track move number for en passant timing
    
    // Move history for save/load functionality
    private List<String> moveHistory;
    
    public Board() {
        this.squares = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.whiteKingMoved = false;
        this.blackKingMoved = false;
        this.whiteKingsideRookMoved = false;
        this.whiteQueensideRookMoved = false;
        this.blackKingsideRookMoved = false;
        this.blackQueensideRookMoved = false;
        this.enPassantTarget = null;
        this.lastMoveNumber = 0;
        this.moveHistory = new ArrayList<>();
        initializeBoard();
    }
    
    /**
     * Initialize the board with pieces in their starting positions.
     */
    private void initializeBoard() {
        // Clear the board
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col] = null;
            }
        }
        
        // Place white pieces
        squares[7][0] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        squares[7][1] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        squares[7][2] = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        squares[7][3] = new Piece(PieceType.QUEEN, PieceColor.WHITE);
        squares[7][4] = new Piece(PieceType.KING, PieceColor.WHITE);
        squares[7][5] = new Piece(PieceType.BISHOP, PieceColor.WHITE);
        squares[7][6] = new Piece(PieceType.KNIGHT, PieceColor.WHITE);
        squares[7][7] = new Piece(PieceType.ROOK, PieceColor.WHITE);
        
        for (int col = 0; col < BOARD_SIZE; col++) {
            squares[6][col] = new Piece(PieceType.PAWN, PieceColor.WHITE);
        }
        
        // Place black pieces
        squares[0][0] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        squares[0][1] = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        squares[0][2] = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        squares[0][3] = new Piece(PieceType.QUEEN, PieceColor.BLACK);
        squares[0][4] = new Piece(PieceType.KING, PieceColor.BLACK);
        squares[0][5] = new Piece(PieceType.BISHOP, PieceColor.BLACK);
        squares[0][6] = new Piece(PieceType.KNIGHT, PieceColor.BLACK);
        squares[0][7] = new Piece(PieceType.ROOK, PieceColor.BLACK);
        
        for (int col = 0; col < BOARD_SIZE; col++) {
            squares[1][col] = new Piece(PieceType.PAWN, PieceColor.BLACK);
        }
    }
    
    /**
     * Get the piece at the specified position.
     */
    public Piece getPiece(Position position) {
        if (isValidPosition(position)) {
            return squares[position.getRow()][position.getColumn()];
        }
        return null;
    }
    
    /**
     * Set a piece at the specified position.
     */
    public void setPiece(Position position, Piece piece) {
        if (isValidPosition(position)) {
            squares[position.getRow()][position.getColumn()] = piece;
        }
    }
    
    /**
     * Check if a position is valid on the board.
     */
    public boolean isValidPosition(Position position) {
        return position.getRow() >= 0 && position.getRow() < BOARD_SIZE &&
               position.getColumn() >= 0 && position.getColumn() < BOARD_SIZE;
    }
    
    /**
     * Check if a move is valid for the given player.
     */
    public boolean isValidMove(Position from, Position to, PieceColor playerColor) {
        if (!isValidPosition(from) || !isValidPosition(to)) {
            return false;
        }
        
        Piece piece = getPiece(from);
        if (piece == null || piece.getColor() != playerColor) {
            return false;
        }
        
        Piece targetPiece = getPiece(to);
        if (targetPiece != null && targetPiece.getColor() == playerColor) {
            return false; // Cannot capture own piece
        }
        
        // First ensure the piece's movement pattern is valid and, where relevant,
        // that the path between from and to is clear
        if (!isValidPieceMove(piece, from, to)) {
            return false;
        }
        
        // Simulate the move and ensure it does not leave the player's king in check
        Piece captured = getPiece(to);
        setPiece(to, piece);
        setPiece(from, null);
        boolean leavesKingInCheck = isInCheck(playerColor);
        // revert
        setPiece(from, piece);
        setPiece(to, captured);
        
        return !leavesKingInCheck;
    }
    
    /**
     * Make a move on the board.
     */
    public void makeMove(Position from, Position to) {
        Piece piece = getPiece(from);
        lastMoveNumber++;
        
        // Handle castling
        if (piece.getType() == PieceType.KING && Math.abs(to.getColumn() - from.getColumn()) == 2) {
            performCastling(from, to, piece.getColor());
            return;
        }
        
        // Handle en passant capture
        if (piece.getType() == PieceType.PAWN && enPassantTarget != null && to.equals(enPassantTarget)) {
            performEnPassantCapture(from, to, piece.getColor());
            return;
        }
        
        // Check if pawn moves two squares (potential en passant setup)
        Position newEnPassantTarget = null;
        if (piece.getType() == PieceType.PAWN && Math.abs(to.getRow() - from.getRow()) == 2) {
            int direction = piece.getColor() == PieceColor.WHITE ? -1 : 1;
            newEnPassantTarget = new Position(from.getRow() + direction, from.getColumn());
        }
        
        // Regular move
        setPiece(to, piece);
        setPiece(from, null);
        
        // Update castling rights
        updateCastlingRights(piece, from);
        
        // Update en passant target
        enPassantTarget = newEnPassantTarget;
        
        // Add to move history
        String moveNotation = positionToNotation(from) + positionToNotation(to);
        moveHistory.add(moveNotation);
    }
    
    /**
     * Check if a piece move is valid according to chess rules.
     */
    private boolean isValidPieceMove(Piece piece, Position from, Position to) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getColumn() - from.getColumn());
        
        switch (piece.getType()) {
            case PAWN:
                return isValidPawnMove(piece, from, to, rowDiff, colDiff);
            case ROOK:
                return isValidRookMove(from, to, rowDiff, colDiff);
            case KNIGHT:
                return isValidKnightMove(rowDiff, colDiff);
            case BISHOP:
                return isValidBishopMove(from, to, rowDiff, colDiff);
            case QUEEN:
                return isValidQueenMove(from, to, rowDiff, colDiff);
            case KING:
                return isValidKingMove(piece, from, to, rowDiff, colDiff);
            default:
                return false;
        }
    }
    
    private boolean isValidPawnMove(Piece piece, Position from, Position to, int rowDiff, int colDiff) {
        int direction = piece.getColor() == PieceColor.WHITE ? -1 : 1;
        int startRow = piece.getColor() == PieceColor.WHITE ? 6 : 1;
        
        // Forward move
        if (colDiff == 0 && getPiece(to) == null) {
            if (from.getRow() + direction == to.getRow()) {
                return true;
            }
            // Two squares from starting position
            if (from.getRow() == startRow && from.getRow() + 2 * direction == to.getRow()) {
                return true;
            }
        }
        
        // Diagonal capture
        if (colDiff == 1 && rowDiff == 1 && from.getRow() + direction == to.getRow()) {
            // Regular capture
            if (getPiece(to) != null) {
                return true;
            }
            
            // En passant capture
            if (enPassantTarget != null && to.equals(enPassantTarget)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isValidRookMove(Position from, Position to, int rowDiff, int colDiff) {
        if (!((rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0))) {
            return false;
        }
        return isPathClear(from, to);
    }
    
    private boolean isValidKnightMove(int rowDiff, int colDiff) {
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isValidBishopMove(Position from, Position to, int rowDiff, int colDiff) {
        if (!(rowDiff == colDiff && rowDiff > 0)) {
            return false;
        }
        return isPathClear(from, to);
    }
    
    private boolean isValidQueenMove(Position from, Position to, int rowDiff, int colDiff) {
        // Path clearance is handled in the respective functions
        return isValidRookMove(from, to, rowDiff, colDiff) || 
               isValidBishopMove(from, to, rowDiff, colDiff);
    }
    
    private boolean isValidKingMove(Piece piece, Position from, Position to, int rowDiff, int colDiff) {
        // Regular king move (one square in any direction)
        if ((rowDiff <= 1 && colDiff <= 1) && (rowDiff > 0 || colDiff > 0)) {
            return true;
        }
        
        // Castling move (king moves two squares horizontally)
        if (rowDiff == 0 && colDiff == 2) {
            return isValidCastling(piece.getColor(), from, to);
        }
        
        return false;
    }
    
    /**
     * Check if castling move is valid.
     */
    private boolean isValidCastling(PieceColor color, Position from, Position to) {
        boolean isWhite = color == PieceColor.WHITE;
        int row = isWhite ? 7 : 0;
        
        // King must be on starting square
        if (from.getRow() != row || from.getColumn() != 4) {
            return false;
        }
        
        // King must not have moved
        if (isWhite && whiteKingMoved || !isWhite && blackKingMoved) {
            return false;
        }
        
        boolean isKingSide = to.getColumn() == 6;
        
        // Check rook position and movement
        if (isKingSide) {
            if (isWhite && whiteKingsideRookMoved || !isWhite && blackKingsideRookMoved) {
                return false;
            }
            // Check rook is in place
            Piece rook = getPiece(new Position(row, 7));
            if (rook == null || rook.getType() != PieceType.ROOK || rook.getColor() != color) {
                return false;
            }
            // Path must be clear (f and g files)
            if (getPiece(new Position(row, 5)) != null || getPiece(new Position(row, 6)) != null) {
                return false;
            }
        } else {
            if (isWhite && whiteQueensideRookMoved || !isWhite && blackQueensideRookMoved) {
                return false;
            }
            // Check rook is in place
            Piece rook = getPiece(new Position(row, 0));
            if (rook == null || rook.getType() != PieceType.ROOK || rook.getColor() != color) {
                return false;
            }
            // Path must be clear (b, c, and d files)
            if (getPiece(new Position(row, 1)) != null || 
                getPiece(new Position(row, 2)) != null || 
                getPiece(new Position(row, 3)) != null) {
                return false;
            }
        }
        
        // King must not be in check
        if (isInCheck(color)) {
            return false;
        }
        
        // King must not pass through or end up in check
        Position intermediate = new Position(row, isKingSide ? 5 : 3);
        
        // Simulate intermediate position
        Piece king = getPiece(from);
        setPiece(intermediate, king);
        setPiece(from, null);
        boolean passesThoughCheck = isInCheck(color);
        setPiece(from, king);
        setPiece(intermediate, null);
        
        if (passesThoughCheck) {
            return false;
        }
        
        // Simulate final position
        setPiece(to, king);
        setPiece(from, null);
        boolean endsInCheck = isInCheck(color);
        setPiece(from, king);
        setPiece(to, null);
        
        return !endsInCheck;
    }

    /**
     * Check that all squares between from (exclusive) and to (exclusive) are empty.
     */
    private boolean isPathClear(Position from, Position to) {
        int rowStep = Integer.compare(to.getRow(), from.getRow());
        int colStep = Integer.compare(to.getColumn(), from.getColumn());
        int row = from.getRow() + rowStep;
        int col = from.getColumn() + colStep;
        while (row != to.getRow() || col != to.getColumn()) {
            if (squares[row][col] != null) {
                return false;
            }
            row += rowStep;
            col += colStep;
        }
        return true;
    }

    /**
     * Determine if the given color's king is currently in check.
     */
    public boolean isInCheck(PieceColor color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;
        return isSquareAttacked(kingPos, opposite(color));
    }

    /**
     * Determine if the given color has any legal move available.
     */
    public boolean hasAnyLegalMove(PieceColor color) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece == null || piece.getColor() != color) continue;
                Position from = new Position(row, col);
                for (int r2 = 0; r2 < BOARD_SIZE; r2++) {
                    for (int c2 = 0; c2 < BOARD_SIZE; c2++) {
                        Position to = new Position(r2, c2);
                        if (from.getRow() == to.getRow() && from.getColumn() == to.getColumn()) continue;
                        if (isValidMove(from, to, color)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private Position findKing(PieceColor color) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece p = squares[row][col];
                if (p != null && p.getType() == PieceType.KING && p.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    private boolean isSquareAttacked(Position square, PieceColor byColor) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece attacker = squares[row][col];
                if (attacker == null || attacker.getColor() != byColor) continue;
                Position from = new Position(row, col);
                if (attacker.getType() == PieceType.PAWN) {
                    int dir = byColor == PieceColor.WHITE ? -1 : 1;
                    if (square.getRow() == row + dir && Math.abs(square.getColumn() - col) == 1) {
                        return true;
                    }
                } else if (attacker.getType() == PieceType.KNIGHT) {
                    int dr = Math.abs(square.getRow() - row);
                    int dc = Math.abs(square.getColumn() - col);
                    if ((dr == 2 && dc == 1) || (dr == 1 && dc == 2)) {
                        return true;
                    }
                } else if (attacker.getType() == PieceType.KING) {
                    int dr = Math.abs(square.getRow() - row);
                    int dc = Math.abs(square.getColumn() - col);
                    if (dr <= 1 && dc <= 1 && (dr > 0 || dc > 0)) {
                        return true;
                    }
                } else {
                    // Sliding pieces: rook, bishop, queen
                    if (canSlideTo(from, square, attacker.getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canSlideTo(Position from, Position to, PieceType type) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getColumn() - from.getColumn());
        boolean pattern;
        if (type == PieceType.ROOK) {
            pattern = (rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0);
        } else if (type == PieceType.BISHOP) {
            pattern = rowDiff == colDiff && rowDiff > 0;
        } else if (type == PieceType.QUEEN) {
            pattern = (rowDiff == 0 || colDiff == 0 || rowDiff == colDiff) && (rowDiff > 0 || colDiff > 0);
        } else {
            return false;
        }
        if (!pattern) return false;
        return isPathClear(from, to);
    }

    private PieceColor opposite(PieceColor color) {
        return color == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
    }
    
    /**
     * Perform a castling move.
     */
    private void performCastling(Position kingFrom, Position kingTo, PieceColor color) {
        // Move the king
        Piece king = getPiece(kingFrom);
        setPiece(kingTo, king);
        setPiece(kingFrom, null);
        
        // Move the rook
        boolean isKingSide = kingTo.getColumn() > kingFrom.getColumn();
        int rookFromCol = isKingSide ? 7 : 0;
        int rookToCol = isKingSide ? 5 : 3;
        int row = color == PieceColor.WHITE ? 7 : 0;
        
        Position rookFrom = new Position(row, rookFromCol);
        Position rookTo = new Position(row, rookToCol);
        
        Piece rook = getPiece(rookFrom);
        setPiece(rookTo, rook);
        setPiece(rookFrom, null);
        
        // Update castling rights
        if (color == PieceColor.WHITE) {
            whiteKingMoved = true;
            whiteKingsideRookMoved = true;
            whiteQueensideRookMoved = true;
        } else {
            blackKingMoved = true;
            blackKingsideRookMoved = true;
            blackQueensideRookMoved = true;
        }
        
        // Add to move history
        String castlingNotation = isKingSide ? "O-O" : "O-O-O";
        moveHistory.add(castlingNotation);
        
        // Clear en passant
        enPassantTarget = null;
    }
    
    /**
     * Perform an en passant capture.
     */
    private void performEnPassantCapture(Position from, Position to, PieceColor color) {
        // Move the pawn to the en passant target square
        Piece pawn = getPiece(from);
        setPiece(to, pawn);
        setPiece(from, null);
        
        // Remove the captured pawn
        int capturedPawnRow = color == PieceColor.WHITE ? to.getRow() + 1 : to.getRow() - 1;
        Position capturedPawnPos = new Position(capturedPawnRow, to.getColumn());
        setPiece(capturedPawnPos, null);
        
        // Add to move history
        String moveNotation = positionToNotation(from) + positionToNotation(to) + " e.p.";
        moveHistory.add(moveNotation);
        
        // Clear en passant target
        enPassantTarget = null;
    }
    
    /**
     * Update castling rights based on piece movement.
     */
    private void updateCastlingRights(Piece piece, Position from) {
        if (piece.getType() == PieceType.KING) {
            if (piece.getColor() == PieceColor.WHITE) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        } else if (piece.getType() == PieceType.ROOK) {
            if (piece.getColor() == PieceColor.WHITE) {
                if (from.getRow() == 7) {
                    if (from.getColumn() == 0) {
                        whiteQueensideRookMoved = true;
                    } else if (from.getColumn() == 7) {
                        whiteKingsideRookMoved = true;
                    }
                }
            } else {
                if (from.getRow() == 0) {
                    if (from.getColumn() == 0) {
                        blackQueensideRookMoved = true;
                    } else if (from.getColumn() == 7) {
                        blackKingsideRookMoved = true;
                    }
                }
            }
        }
    }
    
    /**
     * Convert position to chess notation (e.g., Position(0,0) -> "a8").
     */
    private String positionToNotation(Position pos) {
        char file = (char) ('a' + pos.getColumn());
        int rank = 8 - pos.getRow();
        return "" + file + rank;
    }
    
    /**
     * Get all valid moves for a specific player color.
     */
    public List<String> getAllValidMoves(PieceColor playerColor) {
        List<String> validMoves = new ArrayList<>();
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position from = new Position(row, col);
                Piece piece = getPiece(from);
                
                if (piece != null && piece.getColor() == playerColor) {
                    // Check all possible destination squares
                    for (int toRow = 0; toRow < BOARD_SIZE; toRow++) {
                        for (int toCol = 0; toCol < BOARD_SIZE; toCol++) {
                            Position to = new Position(toRow, toCol);
                            
                            if (isValidMove(from, to, playerColor)) {
                                String moveNotation = positionToNotation(from) + positionToNotation(to);
                                
                                // Add special notations
                                Piece targetPiece = getPiece(to);
                                if (targetPiece != null) {
                                    moveNotation += " (capture " + targetPiece.getType().toString().toLowerCase() + ")";
                                } else if (piece.getType() == PieceType.KING && Math.abs(toCol - col) == 2) {
                                    moveNotation += (toCol > col) ? " (O-O)" : " (O-O-O)";
                                } else if (piece.getType() == PieceType.PAWN && enPassantTarget != null && to.equals(enPassantTarget)) {
                                    moveNotation += " (en passant)";
                                }
                                
                                validMoves.add(moveNotation);
                            }
                        }
                    }
                }
            }
        }
        
        return validMoves;
    }
    
    /**
     * Get move history as a formatted string.
     */
    public List<String> getMoveHistory() {
        return new ArrayList<>(moveHistory);
    }
    
    /**
     * Save game state to a string (for file saving).
     */
    public String saveGameState() {
        StringBuilder sb = new StringBuilder();
        
        // Save board state
        sb.append("BOARD:\n");
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece != null) {
                    sb.append(positionToNotation(new Position(row, col)))
                      .append(":").append(piece.getColor()).append("_").append(piece.getType())
                      .append(" ");
                }
            }
        }
        sb.append("\n");
        
        // Save castling rights
        sb.append("CASTLING:").append(whiteKingMoved).append(",").append(blackKingMoved)
          .append(",").append(whiteKingsideRookMoved).append(",").append(whiteQueensideRookMoved)
          .append(",").append(blackKingsideRookMoved).append(",").append(blackQueensideRookMoved).append("\n");
        
        // Save en passant target
        sb.append("ENPASSANT:");
        if (enPassantTarget != null) {
            sb.append(positionToNotation(enPassantTarget));
        } else {
            sb.append("none");
        }
        sb.append("\n");
        
        // Save move history
        sb.append("MOVES:");
        for (String move : moveHistory) {
            sb.append(move).append(" ");
        }
        sb.append("\n");
        
        // Save move number
        sb.append("MOVENUMBER:").append(lastMoveNumber).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Load game state from a string (for file loading).
     */
    public boolean loadGameState(String gameState) {
        try {
            String[] lines = gameState.split("\n");
            
            // Clear current board
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    squares[row][col] = null;
                }
            }
            
            // Load board state
            for (String line : lines) {
                if (line.startsWith("BOARD:")) {
                    String boardData = line.substring(6).trim();
                    if (!boardData.isEmpty()) {
                        String[] pieces = boardData.split(" ");
                        for (String pieceData : pieces) {
                            if (pieceData.contains(":")) {
                                String[] parts = pieceData.split(":");
                                Position pos = notationToPosition(parts[0]);
                                String[] colorType = parts[1].split("_");
                                PieceColor color = PieceColor.valueOf(colorType[0]);
                                PieceType type = PieceType.valueOf(colorType[1]);
                                setPiece(pos, new Piece(type, color));
                            }
                        }
                    }
                } else if (line.startsWith("CASTLING:")) {
                    String[] castlingData = line.substring(9).split(",");
                    whiteKingMoved = Boolean.parseBoolean(castlingData[0]);
                    blackKingMoved = Boolean.parseBoolean(castlingData[1]);
                    whiteKingsideRookMoved = Boolean.parseBoolean(castlingData[2]);
                    whiteQueensideRookMoved = Boolean.parseBoolean(castlingData[3]);
                    blackKingsideRookMoved = Boolean.parseBoolean(castlingData[4]);
                    blackQueensideRookMoved = Boolean.parseBoolean(castlingData[5]);
                } else if (line.startsWith("ENPASSANT:")) {
                    String enPassantData = line.substring(10);
                    if (!enPassantData.equals("none")) {
                        enPassantTarget = notationToPosition(enPassantData);
                    } else {
                        enPassantTarget = null;
                    }
                } else if (line.startsWith("MOVES:")) {
                    String movesData = line.substring(6).trim();
                    moveHistory.clear();
                    if (!movesData.isEmpty()) {
                        String[] moves = movesData.split(" ");
                        for (String move : moves) {
                            if (!move.trim().isEmpty()) {
                                moveHistory.add(move);
                            }
                        }
                    }
                } else if (line.startsWith("MOVENUMBER:")) {
                    lastMoveNumber = Integer.parseInt(line.substring(11));
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Convert chess notation to Position (e.g., "a8" -> Position(0,0)).
     */
    private Position notationToPosition(String notation) {
        char file = notation.charAt(0);
        int rank = Integer.parseInt(notation.substring(1));
        int col = file - 'a';
        int row = 8 - rank;
        return new Position(row, col);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   a   b   c   d   e   f   g   h\n");
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            sb.append(8 - row).append(" ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece != null) {
                    sb.append(piece.toString()).append(" ");
                } else {
                    sb.append(".   ");  // Empty square with spacing to match piece width
                }
            }
            sb.append(8 - row).append("\n");
        }
        
        sb.append("   a   b   c   d   e   f   g   h\n");
        return sb.toString();
    }
}
