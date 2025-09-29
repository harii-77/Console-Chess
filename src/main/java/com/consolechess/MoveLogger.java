package com.consolechess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Logger class to track all moves and game events in the chess game.
 * Logs moves to both console and a log file for persistent storage.
 * 
 * <p>This class provides comprehensive logging functionality for chess games,
 * including move notation, game events, and automatic file management.
 * The logger creates timestamped log files and ensures proper resource cleanup.</p>
 * 
 * @author Console Chess Team
 * @version 1.1
 * @since 1.0
 */
public class MoveLogger {
    
    // Constants for better maintainability
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FILE_TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    private static final String LOGS_DIRECTORY = "logs";
    private static final String LOG_FILE_PREFIX = "chess_game_";
    private static final String LOG_FILE_EXTENSION = ".log";
    private static final String GAME_START_DELIMITER = "=== CHESS GAME LOG STARTED ===";
    private static final String GAME_END_DELIMITER = "=== CHESS GAME LOG ENDED ===";
    private static final String CONSOLE_LOG_PREFIX = "LOG: ";
    private static final String CAPTURE_NOTATION = " (capture)";
    private static final String CHECK_MESSAGE = "is in CHECK!";
    private static final String CHECKMATE_MESSAGE = "CHECKMATE!";
    private static final String STALEMATE_MESSAGE = "STALEMATE! Game ends in a draw.";
    private static final String GAME_ENDED_PREFIX = "Game ended: ";
    
    // Instance variables
    private BufferedWriter logWriter;
    private final SimpleDateFormat dateFormat;
    private int moveNumber;
    private boolean isWhiteTurn;
    private boolean isClosed;
    private String currentLogFileName;
    
    /**
     * Constructs a new MoveLogger instance.
     * Initializes the logging system, creates the log file, and sets up
     * a shutdown hook for proper resource cleanup.
     */
    public MoveLogger() {
        this.dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        this.moveNumber = 1;
        this.isWhiteTurn = true;
        this.isClosed = false;
        initializeLogFile();
        
        // Add shutdown hook to ensure log file is closed properly
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeLogFile));
    }
    
    /**
     * Initialize the log file for writing.
     * Creates the logs directory if it doesn't exist and generates a timestamped log file.
     * If file creation fails, logging will continue to console only.
     * 
     * @throws RuntimeException if directory creation fails critically (wrapped IOException)
     */
    private void initializeLogFile() {
        try {
            // Create logs directory if it doesn't exist
            Files.createDirectories(Paths.get(LOGS_DIRECTORY));
            
            String timestamp = new SimpleDateFormat(FILE_TIMESTAMP_FORMAT).format(new Date());
            String fileName = LOGS_DIRECTORY + "/" + LOG_FILE_PREFIX + timestamp + LOG_FILE_EXTENSION;
            this.currentLogFileName = fileName;
            logWriter = new BufferedWriter(new FileWriter(fileName));
            logToFile(GAME_START_DELIMITER);
        } catch (IOException e) {
            System.err.println("Warning: Could not create log file. Logging to console only.");
            logWriter = null;
        }
    }
    
    /**
     * Log the start of a new game with player information.
     * Records the names of both players and marks the beginning of game logging.
     * 
     * @param whitePlayerName the name of the player controlling white pieces (not null)
     * @param blackPlayerName the name of the player controlling black pieces (not null)
     * @throws IllegalArgumentException if either player name is null or empty
     */
    public void logGameStart(String whitePlayerName, String blackPlayerName) {
        if (whitePlayerName == null || whitePlayerName.trim().isEmpty() ||
            blackPlayerName == null || blackPlayerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player names cannot be null or empty");
        }
        
        String message = String.format("Game started - White: %s, Black: %s", 
                                     whitePlayerName, blackPlayerName);
        logEvent(message);
    }
    
    /**
     * Log a successful move made by a player.
     * Records the move in standard chess notation with move numbering.
     * White moves are numbered as "1. move", black moves as "1... move".
     * 
     * @param playerName the name of the player making the move (not null)
     * @param playerColor the color of the player's pieces (not null)
     * @param fromSquare the starting square in algebraic notation (e.g., "e2")
     * @param toSquare the destination square in algebraic notation (e.g., "e4")
     * @param pieceType the type of piece being moved (not null)
     * @param isCapture true if this move captures an opponent's piece, false otherwise
     * @throws IllegalArgumentException if any required parameter is null
     */
    public void logMove(String playerName, PieceColor playerColor, String fromSquare, 
                       String toSquare, String pieceType, boolean isCapture) {
        if (playerName == null || playerColor == null || fromSquare == null || 
            toSquare == null || pieceType == null) {
            throw new IllegalArgumentException("Move parameters cannot be null");
        }
        
        String moveNotation = fromSquare + toSquare;
        if (isCapture) {
            moveNotation += CAPTURE_NOTATION;
        }
        
        String moveInfo;
        if (playerColor == PieceColor.WHITE) {
            // White move: use current move number
            moveInfo = String.format("%d. %s: %s (%s)", moveNumber, playerName, 
                                   moveNotation, pieceType);
            isWhiteTurn = false;
        } else {
            // Black move: use current move number with "...", then increment
            moveInfo = String.format("%d... %s: %s (%s)", moveNumber, playerName, 
                                   moveNotation, pieceType);
            isWhiteTurn = true;
            moveNumber++;
        }
        
        logEvent(moveInfo);
    }
    
    /**
     * Log a pawn promotion event.
     * Records when a pawn reaches the opposite end of the board and is promoted.
     * 
     * @param playerName the name of the player whose pawn is being promoted (not null)
     * @param playerColor the color of the player's pieces (not null)
     * @param square the square where the promotion occurs (not null)
     * @param promotedTo the piece type the pawn is promoted to (not null)
     * @throws IllegalArgumentException if any parameter is null
     */
    public void logPromotion(String playerName, PieceColor playerColor, 
                           String square, PieceType promotedTo) {
        if (playerName == null || playerColor == null || square == null || promotedTo == null) {
            throw new IllegalArgumentException("Promotion parameters cannot be null");
        }
        
        String message = String.format("%s (%s) promoted pawn at %s to %s", 
                                     playerName, playerColor, square, promotedTo);
        logEvent(message);
    }
    
    /**
     * Log a check event.
     * Records when a player's king is under attack and must be moved to safety.
     * 
     * @param playerName the name of the player whose king is in check (not null)
     * @param playerColor the color of the player's pieces (not null)
     * @throws IllegalArgumentException if any parameter is null
     */
    public void logCheck(String playerName, PieceColor playerColor) {
        if (playerName == null || playerColor == null) {
            throw new IllegalArgumentException("Check parameters cannot be null");
        }
        
        String message = String.format("%s (%s) %s", playerName, playerColor, CHECK_MESSAGE);
        logEvent(message);
    }
    
    /**
     * Log a checkmate event.
     * Records the end of the game when a player's king is in check and cannot escape.
     * 
     * @param winnerName the name of the victorious player (not null)
     * @param winnerColor the color of the winner's pieces (not null)
     * @param loserName the name of the defeated player (not null)
     * @param loserColor the color of the loser's pieces (not null)
     * @throws IllegalArgumentException if any parameter is null
     */
    public void logCheckmate(String winnerName, PieceColor winnerColor, 
                           String loserName, PieceColor loserColor) {
        if (winnerName == null || winnerColor == null || loserName == null || loserColor == null) {
            throw new IllegalArgumentException("Checkmate parameters cannot be null");
        }
        
        String message = String.format("%s %s (%s) defeats %s (%s)", 
                                     CHECKMATE_MESSAGE, winnerName, winnerColor, loserName, loserColor);
        logEvent(message);
    }
    
    /**
     * Log a stalemate event.
     * Records when the game ends in a draw because a player has no legal moves
     * but their king is not in check.
     */
    public void logStalemate() {
        String message = STALEMATE_MESSAGE;
        logEvent(message);
    }
    
    /**
     * Log the end of the game.
     * Records the final result and properly closes the log file.
     * This method is idempotent - calling it multiple times has no additional effect.
     * 
     * @param result a description of how the game ended (not null)
     * @throws IllegalArgumentException if result is null
     */
    public void logGameEnd(String result) {
        if (result == null) {
            throw new IllegalArgumentException("Game result cannot be null");
        }
        
        if (!isClosed) {
            logEvent(GAME_ENDED_PREFIX + result);
            logEvent(GAME_END_DELIMITER);
            closeLogFile();
        }
    }
    
    /**
     * Log a general game event.
     * Creates a timestamped log entry and outputs it to both console and log file.
     * 
     * @param message the message to log (not null)
     * @throws IllegalArgumentException if message is null
     */
    public synchronized void logEvent(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Log message cannot be null");
        }
        
        String timestamp = dateFormat.format(new Date());
        String logMessage = String.format("[%s] %s", timestamp, message);
        
        // Log to console
        System.out.println(CONSOLE_LOG_PREFIX + logMessage);
        
        // Log to file if available
        logToFile(logMessage);
    }
    
    /**
     * Write a message to the log file.
     * This method safely handles file I/O and gracefully degrades if the file is unavailable.
     * 
     * @param message the message to write to the log file (not null)
     */
    private synchronized void logToFile(String message) {
        if (logWriter != null && !isClosed) {
            try {
                logWriter.write(message);
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                System.err.println("Warning: Failed to write to log file: " + e.getMessage());
                // Disable further file logging to prevent repeated errors
                try {
                    logWriter.close();
                } catch (IOException closeException) {
                    // Ignore close exception as we're already in error state
                }
                logWriter = null;
            }
        }
    }
    
    /**
     * Close the log file properly.
     * Ensures that all buffered data is written and resources are released.
     * This method is idempotent - calling it multiple times has no additional effect.
     * It's automatically called by the shutdown hook to ensure proper cleanup.
     */
    private synchronized void closeLogFile() {
        if (logWriter != null && !isClosed) {
            try {
                isClosed = true;
                logWriter.flush(); // Ensure all data is written
                logWriter.close();
                logWriter = null;
                System.out.println("Log file closed successfully: " + currentLogFileName);
            } catch (IOException e) {
                System.err.println("Warning: Failed to close log file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get the current move number.
     * Returns the number of the current move pair (one white move + one black move = one move number).
     * 
     * @return the current move number (starting from 1)
     */
    public int getMoveNumber() {
        return moveNumber;
    }
    
    /**
     * Check if the logger is currently closed.
     * 
     * @return true if the logger has been closed and cannot log to file, false otherwise
     */
    public boolean isClosed() {
        return isClosed;
    }
    
    /**
     * Get the current log file name.
     * 
     * @return the full path of the current log file, or null if no file is being used
     */
    public String getCurrentLogFileName() {
        return currentLogFileName;
    }
    
    /**
     * Check whose turn it is currently.
     * 
     * @return true if it's white's turn to move, false if it's black's turn
     */
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }
} 