package com.consolechess;

/**
 * Represents the chess board and handles piece placement and movement validation.
 */
public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece[][] squares;
    
    public Board() {
        this.squares = new Piece[BOARD_SIZE][BOARD_SIZE];
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
        
        return isValidPieceMove(piece, from, to);
    }
    
    /**
     * Make a move on the board.
     */
    public void makeMove(Position from, Position to) {
        Piece piece = getPiece(from);
        setPiece(to, piece);
        setPiece(from, null);
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
                return isValidKingMove(rowDiff, colDiff);
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
            return getPiece(to) != null;
        }
        
        return false;
    }
    
    private boolean isValidRookMove(Position from, Position to, int rowDiff, int colDiff) {
        return (rowDiff == 0 && colDiff > 0) || (colDiff == 0 && rowDiff > 0);
    }
    
    private boolean isValidKnightMove(int rowDiff, int colDiff) {
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isValidBishopMove(Position from, Position to, int rowDiff, int colDiff) {
        return rowDiff == colDiff && rowDiff > 0;
    }
    
    private boolean isValidQueenMove(Position from, Position to, int rowDiff, int colDiff) {
        return isValidRookMove(from, to, rowDiff, colDiff) || 
               isValidBishopMove(from, to, rowDiff, colDiff);
    }
    
    private boolean isValidKingMove(int rowDiff, int colDiff) {
        return (rowDiff <= 1 && colDiff <= 1) && (rowDiff > 0 || colDiff > 0);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            sb.append(8 - row).append(" ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece != null) {
                    sb.append(piece.toString()).append(" ");
                } else {
                    sb.append(". ");
                }
            }
            sb.append(8 - row).append("\n");
        }
        
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
}
