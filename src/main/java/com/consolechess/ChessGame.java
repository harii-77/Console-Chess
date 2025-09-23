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

    public ChessGame() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
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
            gameRunning = false;
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
                    
                    if (makeMove(fromPos, toPos)) {
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
        // For now, we'll implement a simple win condition
        // In a full implementation, this would check for checkmate, stalemate, etc.
        return false;
    }

    /**
     * Attempt to make a move on the board.
     */
    private boolean makeMove(Position from, Position to) {
        if (board.isValidMove(from, to, currentPlayer.getColor())) {
            board.makeMove(from, to);
            System.out.println("Move successful!");
            return true;
        } else {
            System.out.println("Invalid move! Please try again.");
            return false;
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
