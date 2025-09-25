package com.consolechess;

import java.util.Scanner;

/**
 * Main class for the Console Chess game.
 * This class handles the game flow, user input, and coordinates between different game components.
 */
public class ChessGame {
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private Scanner scanner;
    private boolean gameRunning;
    private boolean gameEnded;
    private MoveLogger moveLogger;

    public ChessGame() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
        this.gameEnded = false;
        this.moveLogger = new MoveLogger();
    }

    /**
     * Main method to start the chess game.
     */
    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        game.initializeGame();
        game.playGame();
    }

    /**
     * Initialize the game by setting up players and the initial board state.
     */
    public void initializeGame() {
        System.out.println("Welcome to Console Chess!");
        System.out.println("========================");
        
        // Get player names
        System.out.print("Enter White player name: ");
        String whiteName = scanner.nextLine().trim();
        if (whiteName.isEmpty()) whiteName = "White Player";
        
        System.out.print("Enter Black player name: ");
        String blackName = scanner.nextLine().trim();
        if (blackName.isEmpty()) blackName = "Black Player";
        
        this.whitePlayer = new Player(whiteName, PieceColor.WHITE);
        this.blackPlayer = new Player(blackName, PieceColor.BLACK);
        this.currentPlayer = whitePlayer; // White always starts
        
        // Log game start
        moveLogger.logGameStart(whitePlayer.getName(), blackPlayer.getName());
        
        System.out.println("\nGame initialized!");
        System.out.println("White: " + whitePlayer.getName());
        System.out.println("Black: " + blackPlayer.getName());
        System.out.println("\nCommands:");
        System.out.println("- Enter moves in format: e2e4 (from square to square)");
        System.out.println("- Type 'quit' or 'q' to exit");
        System.out.println("- Type 'help' for more commands");
    }

    /**
     * Main game loop that continues until the game ends.
     */
    public void playGame() {
        while (gameRunning) {
            displayBoard();
            displayCurrentPlayer();
            
            if (isGameOver()) {
                break;
            }
            
            String input = getPlayerInput();
            processInput(input);
        }
        
        // Log game end only if not already logged
        if (!gameEnded) {
            moveLogger.logGameEnd("Game completed");
        }
        scanner.close();
        System.out.println("Thanks for playing!");
    }

    /**
     * Display the current state of the chess board.
     */
    private void displayBoard() {
        System.out.println("\n" + board.toString());
    }

    /**
     * Display the current player's turn.
     */
    private void displayCurrentPlayer() {
        System.out.println("\n" + currentPlayer.getName() + "'s turn (" + 
                          currentPlayer.getColor() + ")");
    }

    /**
     * Get input from the current player.
     */
    private String getPlayerInput() {
        System.out.print("Enter your move: ");
        return scanner.nextLine().trim().toLowerCase();
    }

    /**
     * Process the player's input command.
     */
    private void processInput(String input) {
        if (input.equals("quit") || input.equals("q")) {
            moveLogger.logGameEnd("Game quit by player");
            gameRunning = false;
            gameEnded = true;
            return;
        }
        
        if (input.equals("help")) {
            displayHelp();
            return;
        }
        
        if (input.length() == 4) {
            // Try to parse as a move (e.g., "e2e4")
            try {
                String from = input.substring(0, 2);
                String to = input.substring(2, 4);
                
                if (isValidSquareNotation(from) && isValidSquareNotation(to)) {
                    Position fromPos = parsePosition(from);
                    Position toPos = parsePosition(to);
                    
                    if (makeMove(fromPos, toPos, from, to)) {
                        switchPlayer();
                    }
                } else {
                    System.out.println("Invalid square notation. Use format like 'e2e4'");
                }
            } catch (Exception e) {
                System.out.println("Invalid move format. Use format like 'e2e4'");
            }
        } else {
            System.out.println("Invalid command. Type 'help' for available commands.");
        }
    }

    /**
     * Display help information.
     */
    private void displayHelp() {
        System.out.println("\nAvailable commands:");
        System.out.println("- Move: e2e4 (from square to square)");
        System.out.println("- Quit: quit or q");
        System.out.println("- Help: help");
        System.out.println("\nSquare notation: a1 to h8");
    }

    /**
     * Check if the game is over.
     */
    private boolean isGameOver() {
        // In a full implementation, this would check for additional conditions
        return false;
    }

    /**
     * Attempt to make a move on the board.
     */
    private boolean makeMove(Position from, Position to, String fromSquare, String toSquare) {
        if (board.isValidMove(from, to, currentPlayer.getColor())) {
            // Get piece information before making the move
            Piece movingPiece = board.getPiece(from);
            Piece capturedPiece = board.getPiece(to);
            boolean isCapture = (capturedPiece != null);
            
            // Make the move
            board.makeMove(from, to);
            System.out.println("Move successful!");
            
            // Log the move
            moveLogger.logMove(currentPlayer.getName(), currentPlayer.getColor(), 
                             fromSquare, toSquare, movingPiece.getType().toString(), isCapture);
            
            // Handle pawn promotion if applicable
            handlePromotionIfNeeded(to, toSquare);
            
            // Announce check if the opponent's king is in check after this move
            Player opponent = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
            if (board.isInCheck(opponent.getColor())) {
                System.out.println("Check!");
                moveLogger.logCheck(opponent.getName(), opponent.getColor());
                
                // Checkmate detection: opponent is in check and has no legal move
                if (!board.hasAnyLegalMove(opponent.getColor())) {
                    System.out.println("Checkmate! Winner: " + currentPlayer.getName());
                    moveLogger.logCheckmate(currentPlayer.getName(), currentPlayer.getColor(),
                                          opponent.getName(), opponent.getColor());
                    gameRunning = false;
                    gameEnded = true;
                }
            } else {
                // Stalemate detection: not in check and no legal moves
                if (!board.hasAnyLegalMove(opponent.getColor())) {
                    System.out.println("Stalemate! The game is a draw.");
                    moveLogger.logStalemate();
                    gameRunning = false;
                    gameEnded = true;
                }
            }
            return true;
        } else {
            System.out.println("Invalid move! Please try again.");
            return false;
        }
    }

    /**
     * If the moved piece is a pawn that reached the back rank, prompt for promotion.
     */
    private void handlePromotionIfNeeded(Position to, String square) {
        Piece moved = board.getPiece(to);
        if (moved == null || moved.getType() != PieceType.PAWN) {
            return;
        }
        boolean whiteAtBackRank = moved.getColor() == PieceColor.WHITE && to.getRow() == 0;
        boolean blackAtBackRank = moved.getColor() == PieceColor.BLACK && to.getRow() == 7;
        if (!(whiteAtBackRank || blackAtBackRank)) {
            return;
        }
        while (true) {
            System.out.print("Promote pawn to (Q/R/B/N): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            PieceType promoteTo = null;
            if (choice.equals("q") || choice.equals("queen")) promoteTo = PieceType.QUEEN;
            else if (choice.equals("r") || choice.equals("rook")) promoteTo = PieceType.ROOK;
            else if (choice.equals("b") || choice.equals("bishop")) promoteTo = PieceType.BISHOP;
            else if (choice.equals("n") || choice.equals("knight")) promoteTo = PieceType.KNIGHT;
            if (promoteTo != null) {
                board.setPiece(to, new Piece(promoteTo, moved.getColor()));
                System.out.println("Pawn promoted to " + promoteTo + "!");
                
                // Log the promotion
                moveLogger.logPromotion(currentPlayer.getName(), currentPlayer.getColor(),
                                      square, promoteTo);
                break;
            }
            System.out.println("Invalid choice. Enter Q, R, B, or N.");
        }
    }

    /**
     * Switch to the other player's turn.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == whitePlayer) ? blackPlayer : whitePlayer;
    }

    /**
     * Check if a square notation is valid (e.g., "e4").
     */
    private boolean isValidSquareNotation(String square) {
        if (square.length() != 2) return false;
        char file = square.charAt(0);
        char rank = square.charAt(1);
        return file >= 'a' && file <= 'h' && rank >= '1' && rank <= '8';
    }

    /**
     * Parse a square notation into a Position object.
     */
    private Position parsePosition(String square) {
        int file = square.charAt(0) - 'a';
        int rankNum = square.charAt(1) - '0'; // ranks 1-8
        int row = 8 - rankNum; // rank 1 -> row 7 (bottom), rank 8 -> row 0 (top)
        return new Position(row, file);
    }
}
