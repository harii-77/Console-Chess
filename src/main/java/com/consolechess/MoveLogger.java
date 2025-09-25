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
 */
public class MoveLogger {
    private BufferedWriter logWriter;
    private SimpleDateFormat dateFormat;
    private int moveNumber;
    private boolean isWhiteTurn;
    private boolean isClosed;
    
    public MoveLogger() {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.moveNumber = 1;
        this.isWhiteTurn = true;
        this.isClosed = false;
        initializeLogFile();
        
        // Add shutdown hook to ensure log file is closed properly
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeLogFile));
    }
    
    /**
     * Initialize the log file for writing.
     */
    private void initializeLogFile() {
        try {
            // Create logs directory if it doesn't exist
            Files.createDirectories(Paths.get("logs"));
            
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "logs/chess_game_" + timestamp + ".log";
            logWriter = new BufferedWriter(new FileWriter(fileName));
            logToFile("=== CHESS GAME LOG STARTED ===");
        } catch (IOException e) {
            System.err.println("Warning: Could not create log file. Logging to console only.");
            logWriter = null;
        }
    }
    
    /**
     * Log the start of a new game with player information.
     */
    public void logGameStart(String whitePlayerName, String blackPlayerName) {
        String message = String.format("Game started - White: %s, Black: %s", 
                                     whitePlayerName, blackPlayerName);
        logEvent(message);
    }
    
    /**
     * Log a successful move made by a player.
     */
    public void logMove(String playerName, PieceColor playerColor, String fromSquare, 
                       String toSquare, String pieceType, boolean isCapture) {
        String moveNotation = fromSquare + toSquare;
        if (isCapture) {
            moveNotation += " (capture)";
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
     */
    public void logPromotion(String playerName, PieceColor playerColor, 
                           String square, PieceType promotedTo) {
        String message = String.format("%s (%s) promoted pawn at %s to %s", 
                                     playerName, playerColor, square, promotedTo);
        logEvent(message);
    }
    
    /**
     * Log a check event.
     */
    public void logCheck(String playerName, PieceColor playerColor) {
        String message = String.format("%s (%s) is in CHECK!", playerName, playerColor);
        logEvent(message);
    }
    
    /**
     * Log a checkmate event.
     */
    public void logCheckmate(String winnerName, PieceColor winnerColor, 
                           String loserName, PieceColor loserColor) {
        String message = String.format("CHECKMATE! %s (%s) defeats %s (%s)", 
                                     winnerName, winnerColor, loserName, loserColor);
        logEvent(message);
    }
    
    /**
     * Log a stalemate event.
     */
    public void logStalemate() {
        String message = "STALEMATE! Game ends in a draw.";
        logEvent(message);
    }
    
    /**
     * Log the end of the game.
     */
    public void logGameEnd(String result) {
        if (!isClosed) {
            logEvent("Game ended: " + result);
            logEvent("=== CHESS GAME LOG ENDED ===");
            closeLogFile();
        }
    }
    
    /**
     * Log a general game event.
     */
    public void logEvent(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = String.format("[%s] %s", timestamp, message);
        
        // Log to console
        System.out.println("LOG: " + logMessage);
        
        // Log to file if available
        logToFile(logMessage);
    }
    
    /**
     * Write a message to the log file.
     */
    private void logToFile(String message) {
        if (logWriter != null && !isClosed) {
            try {
                logWriter.write(message);
                logWriter.newLine();
                logWriter.flush();
            } catch (IOException e) {
                System.err.println("Warning: Failed to write to log file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close the log file properly.
     */
    private void closeLogFile() {
        if (logWriter != null && !isClosed) {
            try {
                isClosed = true;
                logWriter.close();
                logWriter = null;
            } catch (IOException e) {
                System.err.println("Warning: Failed to close log file: " + e.getMessage());
            }
        }
    }
    
    /**
     * Get the current move number.
     */
    public int getMoveNumber() {
        return moveNumber;
    }
} 