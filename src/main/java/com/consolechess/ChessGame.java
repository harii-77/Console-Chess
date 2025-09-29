package com.consolechess;

import java.util.Scanner;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.InvalidPathException;

/**
 * Main controller class for the Console Chess game application.
 * 
 * <p>This class serves as the primary game controller, orchestrating all aspects
 * of a chess game including:</p>
 * <ul>
 *   <li>Player initialization and management</li>
 *   <li>Game flow control and turn management</li>
 *   <li>User input processing and command handling</li>
 *   <li>Board display and game state visualization</li>
 *   <li>Game persistence (save/load functionality)</li>
 *   <li>Move validation and execution</li>
 *   <li>Game end conditions (checkmate, stalemate, draw)</li>
 * </ul>
 * 
 * <p>The game supports advanced chess features including:</p>
 * <ul>
 *   <li>Full chess rule implementation with castling and en passant</li>
 *   <li>Move validation and legal move generation</li>
 *   <li>Check and checkmate detection</li>
 *   <li>Comprehensive move logging and history</li>
 *   <li>Game state persistence with save/load</li>
 *   <li>Interactive help system and move assistance</li>
 * </ul>
 * 
 * <p><strong>Usage:</strong> Run the main method to start a new chess game.
 * Players interact via console commands with algebraic notation for moves.</p>
 * 
 * <p><strong>Thread Safety:</strong> This class is not thread-safe. It's designed
 * for single-threaded console interaction.</p>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public class ChessGame {
    
    // Game configuration constants
    /** Directory name for saving game files */
    private static final String SAVE_DIR = "saves";
    
    /** File extension for save files */
    private static final String SAVE_EXTENSION = ".sav";
    
    /** Default white player name when none provided */
    private static final String DEFAULT_WHITE_NAME = "White Player";
    
    /** Default black player name when none provided */
    private static final String DEFAULT_BLACK_NAME = "Black Player";
    
    // Command constants
    /** Command to quit the game */
    private static final String CMD_QUIT = "quit";
    
    /** Short command to quit the game */
    private static final String CMD_QUIT_SHORT = "q";
    
    /** Command to display help */
    private static final String CMD_HELP = "help";
    
    /** Command to show valid moves */
    private static final String CMD_SHOW_MOVES = "pip";
    
    /** Command prefix for saving games */
    private static final String CMD_SAVE_PREFIX = "save ";
    
    /** Command prefix for loading games */
    private static final String CMD_LOAD_PREFIX = "load ";
    
    /** Command to change display mode */
    private static final String CMD_DISPLAY = "display";
    
    /** Kingside castling notation */
    private static final String CASTLING_KINGSIDE = "o-o";
    
    /** Alternative kingside castling notation */
    private static final String CASTLING_KINGSIDE_ALT = "0-0";
    
    /** Queenside castling notation */
    private static final String CASTLING_QUEENSIDE = "o-o-o";
    
    /** Alternative queenside castling notation */
    private static final String CASTLING_QUEENSIDE_ALT = "0-0-0";
    
    /** Expected length for standard move notation (e.g., "e2e4") */
    private static final int STANDARD_MOVE_LENGTH = 4;
    
    // Game state
    private final Board board;
    private final Scanner scanner;
    private final MoveLogger moveLogger;
    
    private Player whitePlayer;
    private Player blackPlayer;
    private Player currentPlayer;
    private boolean gameRunning;
    private boolean gameEnded;
    private Piece.DisplayMode currentDisplayMode;
    
    /**
     * Constructs a new ChessGame instance with default settings.
     * 
     * <p>Initializes:</p>
     * <ul>
     *   <li>A new chess board in starting position</li>
     *   <li>Scanner for user input</li>
     *   <li>Move logger for game tracking</li>
     *   <li>Save directory creation if needed</li>
     * </ul>
     * 
     * @throws RuntimeException if save directory cannot be created
     */
    public ChessGame() {
        this.board = new Board();
        this.scanner = new Scanner(System.in);
        this.gameRunning = true;
        this.gameEnded = false;
        this.moveLogger = new MoveLogger();
        this.currentDisplayMode = Piece.DisplayMode.COLORED_BRACKETED; // Default to standard bracketed
        
        // Create saves directory if it doesn't exist
        createSaveDirectoryIfNeeded();
    }
    
    /**
     * Creates the save directory if it doesn't exist.
     * 
     * @throws RuntimeException if directory creation fails
     */
    private void createSaveDirectoryIfNeeded() {
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create saves directory: " + e.getMessage());
            System.err.println("Save/load functionality may not work properly.");
        } catch (InvalidPathException e) {
            System.err.println("Error: Invalid save directory path: " + e.getMessage());
        }
    }

    /**
     * Main entry point for the Console Chess application.
     * 
     * <p>Creates a new game instance, initializes it with player setup,
     * and starts the main game loop. Handles any critical errors during
     * game startup.</p>
     * 
     * @param args command line arguments (currently unused)
     */
    public static void main(String[] args) {
        try {
            ChessGame game = new ChessGame();
            game.initializeGame();
            game.playGame();
        } catch (Exception e) {
            System.err.println("Fatal error during game execution: " + e.getMessage());
            System.err.println("The game will now exit.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Initialize the game by setting up players and displaying welcome information.
     * 
     * <p>This method handles:</p>
     * <ul>
     *   <li>Welcome message and game branding</li>
     *   <li>Player name input with validation</li>
     *   <li>Player object creation</li>
     *   <li>Initial game logging</li>
     *   <li>Feature overview and command help</li>
     * </ul>
     * 
     * @throws IllegalStateException if player creation fails
     */
    public void initializeGame() {
        displayWelcomeMessage();
        setupPlayers();
        logGameStart();
        displayGameFeatures();
        displayInitialInstructions();
    }
    
    /**
     * Display the welcome message and game title.
     */
    private void displayWelcomeMessage() {
        System.out.println("=============================");
        System.out.println("|    Welcome to Console     |");
        System.out.println("|         CHESS!            |");
        System.out.println("|    Advanced Edition       |");
        System.out.println("=============================");
        System.out.println();
    }
    
    /**
     * Set up both players by getting their names and creating Player objects.
     * 
     * @throws IllegalStateException if player creation fails
     */
    private void setupPlayers() {
        try {
            // Get player names with input validation
            String whiteName = getPlayerName("White", DEFAULT_WHITE_NAME);
            String blackName = getPlayerName("Black", DEFAULT_BLACK_NAME);
            
            // Ensure players have different names for clarity
            if (whiteName.equalsIgnoreCase(blackName)) {
                blackName = blackName + " (Black)";
                System.out.println("Note: Both players had the same name. Black player renamed to: " + blackName);
            }
            
            // Create player objects
            this.whitePlayer = new Player(whiteName, PieceColor.WHITE);
            this.blackPlayer = new Player(blackName, PieceColor.BLACK);
            this.currentPlayer = whitePlayer; // White always starts first
            
        } catch (Exception e) {
            throw new IllegalStateException("Failed to set up players: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get a player name with input validation and default fallback.
     * 
     * @param colorName the color name for the player (e.g., "White", "Black")
     * @param defaultName the default name to use if no input provided
     * @return a valid player name (never null or empty)
     */
    private String getPlayerName(String colorName, String defaultName) {
        System.out.printf("Enter %s player name (press Enter for '%s'): ", colorName, defaultName);
        String name = scanner.nextLine().trim();
        
        if (name.isEmpty()) {
            name = defaultName;
            System.out.println("Using default name: " + name);
        } else if (name.length() > Player.MAX_NAME_LENGTH) {
            name = name.substring(0, Player.MAX_NAME_LENGTH);
            System.out.println("Name truncated to: " + name);
        }
        
        return name;
    }
    
    /**
     * Log the game start with both player names.
     */
    private void logGameStart() {
        moveLogger.logGameStart(whitePlayer.getName(), blackPlayer.getName());
    }
    
    /**
     * Display the game features and capabilities.
     */
    private void displayGameFeatures() {
        System.out.println("\n*** Game initialized with advanced features! ***");
        System.out.printf("White: %s%n", whitePlayer.getName());
        System.out.printf("Black: %s%n", blackPlayer.getName());
        
        System.out.println("\n>> Chess Features Available:");
        System.out.println("+ Castling (O-O, O-O-O)        + En passant capture");
        System.out.println("+ Pawn promotion               + Check/Checkmate detection");
        System.out.println("+ Complete move validation     + Save/Load games");
        System.out.println("+ Valid moves display (pip)    + Comprehensive logging");
        System.out.println("+ Undo/Redo functionality     + Game state tracking");
    }
    
    /**
     * Display initial command instructions for players.
     */
    private void displayInitialInstructions() {
        System.out.println("\n>> How to Play:");
        System.out.println("- Enter moves: e2e4 (from square to square)");
        System.out.println("- Castling: O-O (kingside) or O-O-O (queenside)");
        System.out.println("- Type 'pip' to see all valid moves");
        System.out.println("- Type 'display' to switch to Unicode symbols (if supported)");
        System.out.println("- Type 'help' for complete command list");
        System.out.println("- Type 'quit' or 'q' to exit game");
        System.out.println("\n" + "=".repeat(50));
    }

    /**
     * Main game loop that continues until the game ends.
     * 
     * <p>This method orchestrates the complete game flow:</p>
     * <ul>
     *   <li>Display current board state</li>
     *   <li>Show current player information</li>
     *   <li>Check for game end conditions</li>
     *   <li>Process player input and commands</li>
     *   <li>Handle move execution and validation</li>
     * </ul>
     * 
     * <p>The loop continues until game termination via quit command,
     * checkmate, stalemate, or other end conditions.</p>
     */
    public void playGame() {
        try {
            while (gameRunning && !gameEnded) {
                displayGameState();
                
                // Check for game end conditions before accepting input
                if (isGameOver()) {
                    handleGameEnd();
                    break;
                }
                
                // Get and process player input
                String input = getPlayerInput();
                processPlayerInput(input);
            }
            
        } catch (Exception e) {
            System.err.println("Error during game play: " + e.getMessage());
            moveLogger.logEvent("Game terminated due to error: " + e.getMessage());
        } finally {
            cleanupGame();
        }
    }
    
    /**
     * Display the complete current game state.
     */
    private void displayGameState() {
        displayBoard();
        displayCurrentPlayerInfo();
        displayGameStatus();
    }
    
    /**
     * Display the current chess board.
     */
    private void displayBoard() {
        System.out.println("\n" + board.toString());
    }
    
    /**
     * Display comprehensive information about the current player's turn.
     */
    private void displayCurrentPlayerInfo() {
        System.out.printf("%n=== %s's Turn ===%n", currentPlayer.getName());
        System.out.printf("Playing as: %s pieces%n", currentPlayer.getColor().getDisplayName());
        System.out.printf("Move #: %d%n", moveLogger.getMoveNumber());
    }
    
    /**
     * Display current game status information.
     */
    private void displayGameStatus() {
        if (board.isInCheck(currentPlayer.getColor())) {
            System.out.println("*** CHECK! Your king is under attack! ***");
            moveLogger.logCheck(currentPlayer.getName(), currentPlayer.getColor());
        }
        
        // Display any relevant warnings or status messages
        displayGameWarnings();
    }
    
    /**
     * Display any relevant game warnings or status messages.
     */
    private void displayGameWarnings() {
        // Could include warnings about time, repeated positions, etc.
        // Currently serves as placeholder for future enhancements
    }
    
    /**
     * Get input from the current player.
     * 
     * @return the player's input command, trimmed and converted to lowercase
     */
    private String getPlayerInput() {
        System.out.print("\nEnter your move: ");
        return scanner.nextLine().trim().toLowerCase();
    }
    
    /**
     * Handle the end of the game with appropriate logging and cleanup.
     */
    private void handleGameEnd() {
        gameEnded = true;
        gameRunning = false;
        
        // For now, just handle basic game end - can be enhanced later with proper checkmate/stalemate detection
        System.out.println("\n*** Game ended ***");
        moveLogger.logGameEnd("Game completed naturally");
    }
    
    /**
     * Process player input commands with comprehensive error handling.
     * This method provides a clean interface for input processing.
     * 
     * @param input the player's input command (not null)
     */
    private void processPlayerInput(String input) {
        if (input == null) {
            System.out.println("Invalid input. Please try again.");
            return;
        }
        
        input = input.trim().toLowerCase();
        
        if (input.isEmpty()) {
            System.out.println("Please enter a command. Type 'help' for assistance.");
            return;
        }
        
        // Process the actual command
        processInput(input);
    }
    
    /**
     * Process the player's input command.
     * This is a simplified version of the original method for now.
     * 
     * @param input the player's input command (not null, already trimmed and lowercased)
     */
    private void processInput(String input) {
        if (input.equals(CMD_QUIT) || input.equals(CMD_QUIT_SHORT)) {
            moveLogger.logGameEnd("Game quit by player");
            gameRunning = false;
            gameEnded = true;
            return;
        }
        
        if (input.equals(CMD_HELP)) {
            displayHelp();
            return;
        }
        
        if (input.equals(CMD_SHOW_MOVES)) {
            displayValidMoves();
            return;
        }
        
        if (input.startsWith(CMD_SAVE_PREFIX)) {
            String filename = input.substring(CMD_SAVE_PREFIX.length()).trim();
            saveGame(filename);
            return;
        }
        
        if (input.startsWith(CMD_LOAD_PREFIX)) {
            String filename = input.substring(CMD_LOAD_PREFIX.length()).trim();
            loadGame(filename);
            return;
        }

        if (input.equals(CMD_DISPLAY)) {
            toggleDisplayMode();
            return;
        }
        
        // Handle special castling notation
        if (input.equals(CASTLING_KINGSIDE) || input.equals(CASTLING_KINGSIDE_ALT)) {
            handleCastling(true); // Kingside
            return;
        }
        
        if (input.equals(CASTLING_QUEENSIDE) || input.equals(CASTLING_QUEENSIDE_ALT)) {
            handleCastling(false); // Queenside
            return;
        }
        
        if (input.length() == STANDARD_MOVE_LENGTH) {
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
     * Clean up game resources and display farewell message.
     */
    private void cleanupGame() {
        // Log game end only if not already logged
        if (!gameEnded) {
            moveLogger.logGameEnd("Game completed");
        }
        
        // Close resources
        try {
            scanner.close();
        } catch (Exception e) {
            System.err.println("Warning: Error closing scanner: " + e.getMessage());
        }
        
        // Display farewell message
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Thank you for playing Console Chess!");
        System.out.println("We hope you enjoyed your game!");
        System.out.println("=".repeat(50));
    }

    /**
     * Display help information.
     */
    private void displayHelp() {
        System.out.println("\n=== AVAILABLE COMMANDS ===");
        System.out.println(">> Game Commands:");
        System.out.println("  help           - Show this help message");
        System.out.println("  quit or q      - Exit the game");
        System.out.println();
        System.out.println(">> Movement Commands:");
        System.out.println("  e2e4           - Move piece from e2 to e4 (standard notation)");
        System.out.println("  O-O or 0-0     - Kingside castling");
        System.out.println("  O-O-O or 0-0-0 - Queenside castling");
        System.out.println();
        System.out.println(">> Information Commands:");
        System.out.println("  pip            - Show all valid moves for current player");
        System.out.println("  display        - Toggle between bracketed [P] and Unicode ♔ symbols");
        System.out.println();
        System.out.println(">> File Commands:");
        System.out.println("  save <name>    - Save current game (e.g., 'save mygame')");
        System.out.println("  load <name>    - Load saved game (e.g., 'load mygame')");
        System.out.println();
        System.out.println(">> Display Modes:");
        System.out.println("  * Standard: [P] (p) - Colored bracketed notation (default)");
        System.out.println("  * Unicode: ♔ ♕ ♖ ♗ ♘ ♙ - Requires Unicode-capable terminal");
        System.out.println();
        System.out.println(">> Notes:");
        System.out.println("  * Square notation: a1 to h8 (files a-h, ranks 1-8)");
        System.out.println("  * Special moves: castling, en passant, pawn promotion");
        System.out.println("  * All moves and events are logged automatically");
        System.out.println("===========================");
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

    /**
     * Display all valid moves for the current player (pip command).
     */
    private void displayValidMoves() {
        List<String> validMoves = board.getAllValidMoves(currentPlayer.getColor());
        
        if (validMoves.isEmpty()) {
            System.out.println("No valid moves available for " + currentPlayer.getName());
        } else {
            System.out.println("\nValid moves for " + currentPlayer.getName() + " (" + currentPlayer.getColor() + "):");
            int count = 0;
            for (String move : validMoves) {
                System.out.print(move + "  ");
                count++;
                if (count % 4 == 0) { // Display 4 moves per line
                    System.out.println();
                }
            }
            if (count % 4 != 0) {
                System.out.println();
            }
            System.out.println("Total moves: " + validMoves.size());
        }
    }
    
    /**
     * Save the current game state to a file.
     */
    private void saveGame(String filename) {
        if (filename.isEmpty()) {
            System.out.println("Please provide a filename. Usage: save <filename>");
            return;
        }
        
        try {
            // Add .sav extension if not present
            if (!filename.endsWith(SAVE_EXTENSION)) {
                filename += SAVE_EXTENSION;
            }
            
            String filepath = SAVE_DIR + "/" + filename;
            String gameState = board.saveGameState();
            
            // Add player information
            StringBuilder fullSave = new StringBuilder();
            fullSave.append("PLAYERS:").append(whitePlayer.getName()).append(",").append(blackPlayer.getName()).append("\n");
            fullSave.append("CURRENT:").append(currentPlayer.getColor().name()).append("\n");
            fullSave.append(gameState);
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
                writer.write(fullSave.toString());
            }
            
            System.out.println("Game saved to " + filepath);
            moveLogger.logEvent("Game saved to " + filename);
            
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
        }
    }
    
    /**
     * Load a game state from a file.
     */
    private void loadGame(String filename) {
        if (filename.isEmpty()) {
            System.out.println("Please provide a filename. Usage: load <filename>");
            return;
        }
        
        try {
            // Add .sav extension if not present
            if (!filename.endsWith(SAVE_EXTENSION)) {
                filename += SAVE_EXTENSION;
            }
            
            String filepath = SAVE_DIR + "/" + filename;
            
            if (!Files.exists(Paths.get(filepath))) {
                System.out.println("Save file not found: " + filename);
                return;
            }
            
            String gameState = Files.readString(Paths.get(filepath));
            
            // Parse player information
            String[] lines = gameState.split("\n");
            for (String line : lines) {
                if (line.startsWith("PLAYERS:")) {
                    String[] playerNames = line.substring(8).split(",");
                    whitePlayer = new Player(playerNames[0], PieceColor.WHITE);
                    blackPlayer = new Player(playerNames[1], PieceColor.BLACK);
                } else if (line.startsWith("CURRENT:")) {
                    String colorString = line.substring(8);
                    PieceColor currentColor;
                    
                    // Handle both old format (White/Black) and new format (WHITE/BLACK)
                    if (colorString.equals("White") || colorString.equals("WHITE")) {
                        currentColor = PieceColor.WHITE;
                    } else if (colorString.equals("Black") || colorString.equals("BLACK")) {
                        currentColor = PieceColor.BLACK;
                    } else {
                        // Fallback: try direct enum valueOf
                        currentColor = PieceColor.valueOf(colorString);
                    }
                    
                    currentPlayer = (currentColor == PieceColor.WHITE) ? whitePlayer : blackPlayer;
                }
            }
            
            // Load board state
            if (board.loadGameState(gameState)) {
                System.out.println("Game loaded successfully from " + filepath);
                System.out.println("White: " + whitePlayer.getName());
                System.out.println("Black: " + blackPlayer.getName());
                System.out.println("Current player: " + currentPlayer.getName() + " (" + currentPlayer.getColor() + ")");
                moveLogger.logEvent("Game loaded from " + filename);
            } else {
                System.out.println("Error: Invalid save file format.");
            }
            
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
        }
    }
    
    /**
     * Handle castling notation (O-O or O-O-O).
     */
    private void handleCastling(boolean kingside) {
        int row = currentPlayer.getColor() == PieceColor.WHITE ? 7 : 0;
        Position kingPos = new Position(row, 4);
        Position kingDestPos = new Position(row, kingside ? 6 : 2);
        
        if (makeMove(kingPos, kingDestPos, 
                     positionToNotation(kingPos), 
                     positionToNotation(kingDestPos))) {
            switchPlayer();
        }
    }
    
    /**
     * Convert position to notation for castling handler.
     */
    private String positionToNotation(Position pos) {
        char file = (char) ('a' + pos.getColumn());
        int rank = 8 - pos.getRow();
        return "" + file + rank;
    }

    /**
     * Toggle the display mode between UNICODE and COLORED_BRACKETED.
     */
    private void toggleDisplayMode() {
        if (currentDisplayMode == Piece.DisplayMode.UNICODE) {
            currentDisplayMode = Piece.DisplayMode.COLORED_BRACKETED;
            System.out.println("Display mode changed to: Standard Bracketed (with colors)");
            System.out.println("Example: [P] (p) - Yellow for white, cyan for black");
        } else {
            currentDisplayMode = Piece.DisplayMode.UNICODE;
            System.out.println("Display mode changed to: Unicode Chess Symbols");
            System.out.println("Example: ♔ ♕ ♖ ♗ ♘ ♙ (if your terminal supports Unicode)");
        }
        
        // Update the board's display mode
        board.setDisplayMode(currentDisplayMode);
        
        System.out.println("Note: You'll see the change on the next board display.");
        System.out.println("Current board:");
        displayBoard(); // Show the change immediately
    }
}
