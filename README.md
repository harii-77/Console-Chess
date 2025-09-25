# Console Chess

A clean, console-based two-player chess game written in Java with comprehensive logging and modern display formatting.

## Features

### Core Game Features

- Board setup with standard starting positions
- Turn-based two-player gameplay
- Legal move validation (includes path blocking for sliders)
- King safety: moves that leave your king in check are rejected
- Check, checkmate, and stalemate detection with messages
- Pawn promotion (Q/R/B/N) when reaching the back rank
- Simple console UI with friendly prompts and input validation

### New Features ✨

- **Comprehensive Move Logging**: All game events logged with timestamps
  - Game start/end logging with player names
  - Move notation in proper chess format (1. e2e4, 1... e7e5)
  - Capture detection and logging
  - Special events: check, checkmate, stalemate, pawn promotions
  - Automatic log file creation with timestamps
- **Enhanced Visual Display**:
  - White pieces: `[P] [R] [N] [B] [Q] [K]` (uppercase in brackets)
  - Black pieces: `(p) (r) (n) (b) (q) (k)` (lowercase in parentheses)
  - Color-coded pieces: bright yellow for white, cyan for black
  - Improved board spacing and alignment
- **Organized Logging System**:
  - Dedicated `logs/` directory for all game logs
  - Timestamped log files: `chess_game_YYYYMMDD_HHMMSS.log`
  - Dual logging: console output and persistent file storage
  - Automatic cleanup and resource management

## Prerequisites

- Java 11+ (JDK)
- Maven 3.6+
- Windows users: optional `run.bat` convenience script included

Verify tools:

```bash
java -version
javac -version
mvn -v
```

### Install Maven on Windows (if `mvn` is not recognized)

1. Download Maven (binary zip) from `https://maven.apache.org/download.cgi`.
2. Extract to a folder, e.g., `C:\Program Files\Apache\maven-3.9.x`.
3. Add `C:\Program Files\Apache\maven-3.9.x\bin` to your System PATH.
   - Windows Search → "Edit the system environment variables" → Environment Variables → Path → Edit → New.
4. Open a new terminal and run `mvn -v` to verify.
5. Alternatively, use the Windows script in Option 3 (no Maven required).

## Run the Game

### Option 1: Maven (cross-platform)

```bash
mvn compile exec:java
```

### Option 2: Build then run

```bash
mvn compile
java -cp target/classes com.consolechess.ChessGame
```

### Option 3: Windows batch script

```bat
run.bat
```

This compiles with `javac` and runs the game without Maven.

### Option 4: Pre-built JAR file

```bash
java -jar console-chess-1.0.0.jar
```

The JAR includes all latest features and improvements.

### Option 5: Manual compile and run on Windows (no Maven)

If you prefer direct `javac`/`java` commands:

```bat
REM Create output folder if it doesn't exist
if not exist target\classes mkdir target\classes

REM Compile all sources (including MoveLogger)
javac -d target\classes -cp target\classes src\main\java\com\consolechess\*.java

REM Run the game
java -cp target/classes com.consolechess.ChessGame
```

## How to Play

1. Start the program and enter player names when prompted.
2. Enter moves in coordinate notation: `e2e4` means from `e2` to `e4`.
   - Files: `a` to `h`, Ranks: `1` to `8`.
3. Commands:
   - `help`: show usage
   - `quit` or `q`: exit (properly logged)
4. Special rules implemented:
   - Check: after a move, if the opponent is in check, you'll see "Check!".
   - Checkmate: if the opponent has no legal moves while in check, the winner is announced and the game ends.
   - Stalemate: if the opponent has no legal moves and is not in check, the game is a draw.
   - Pawn promotion: when a pawn reaches the last rank, choose `Q`, `R`, `B`, or `N`.

### Game Display

- **White pieces**: `[P] [R] [N] [B] [Q] [K]` - displayed in bright yellow
- **Black pieces**: `(p) (r) (n) (b) (q) (k)` - displayed in cyan
- **Empty squares**: `.` with proper spacing alignment
- **Move logging**: All moves logged to console and `logs/chess_game_TIMESTAMP.log`

### Sample Log Output

```
[2025-09-25 09:16:43] Game started - White: Alice, Black: Bob
[2025-09-25 09:16:48] 1. Alice: e2e4 (P)
[2025-09-25 09:16:52] 1... Bob: e7e5 (P)
[2025-09-25 09:16:55] 2. Alice: d1h5 (Q)
[2025-09-25 09:16:58] 2... Bob: b8c6 (N)
```

### Quick demo sequences

- Scholar's Mate (White wins):
  - White: `e2e4`, Black: `e7e5`, White: `d1h5`, Black: `b8c6`, White: `f1c4`, Black: `g7g6`, White: `h5f7`

## Project Structure

```
src/
  main/java/com/consolechess/
    ChessGame.java     # Game loop and CLI with logging integration
    Board.java         # Board state and validation (including check logic)
    MoveLogger.java    # Comprehensive game event logging system ✨
    Piece.java         # Piece model with enhanced display formatting
    Position.java      # Coordinate model
    Player.java        # Player info
    PieceType.java     # Enum of piece types
    PieceColor.java    # Enum of colors
  test/java/com/consolechess/
    BoardTest.java     # Unit tests for board functionality
    PieceTest.java     # Unit tests for piece behavior
    PositionTest.java  # Unit tests for position handling
logs/                  # Auto-created directory for game logs ✨
  chess_game_YYYYMMDD_HHMMSS.log  # Timestamped game logs
```

## Testing

Run the comprehensive test suite:

```bash
mvn test
```

**Current Status**: ✅ All 18 tests passing (0 failures, 0 errors)

## Logs Directory

The game automatically creates a `logs/` directory where all game sessions are saved with timestamps:

- **Format**: `chess_game_YYYYMMDD_HHMMSS.log`
- **Content**: Complete game history with move notation, player actions, and special events
- **Features**: Automatic file creation, proper resource cleanup, error handling

Example log files:

- `logs/chess_game_20250925_091641.log`
- `logs/chess_game_20250925_091735.log`

## Recent Improvements 🚀

- **v1.0.0 Latest** (Current JAR):
  - ✅ Fixed move numbering logic in chess notation
  - ✅ Added comprehensive MoveLogger with file and console output
  - ✅ Enhanced piece display with brackets `[P]` and parentheses `(p)`
  - ✅ Organized logs in dedicated directory
  - ✅ Fixed double logging warnings and resource management
  - ✅ Improved board spacing and alignment
  - ✅ Added shutdown hooks for proper cleanup

## Troubleshooting

- "mvn: command not found": install Maven and ensure it's on your PATH.
- Windows PowerShell quirks with piping: prefer Option 1 or 2 above.
- If Java version is too new/old, use JDK 11+.
- **Log file warnings**: Fixed in latest version with proper resource management.
- **Display issues**: Latest version includes enhanced piece formatting and spacing.

## License

MIT (or your preferred license). Update this section if you choose a different license.
