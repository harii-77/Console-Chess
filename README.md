# Console Chess

A clean, console-based two-player chess game written in Java with comprehensive logging, advanced chess rules, and modern display formatting.

## Features

### Core Game Features

- Board setup with standard starting positions
- Turn-based two-player gameplay
- Legal move validation (includes path blocking for sliders)
- King safety: moves that leave your king in check are rejected
- Check, checkmate, and stalemate detection with messages
- Simple console UI with friendly prompts and input validation

### Advanced Chess Rules ✨

- **Castling**: Both kingside (O-O) and queenside (O-O-O) castling
  - Automatic validation of castling conditions (king/rook not moved, path clear, not in check)
  - Special notation support: `O-O`, `0-0`, `O-O-O`, `0-0-0`
- **En Passant**: Automatic pawn capture when opponent's pawn moves two squares
  - Proper timing validation (must be captured immediately)
  - Automatic removal of captured pawn
- **Pawn Promotion**: Interactive promotion when pawn reaches back rank
  - Choose from Queen, Rook, Bishop, or Knight
  - Logged with special notation
- **Complete Move Validation**: All chess rules properly implemented
  - Prevents moves that would put own king in check
  - Validates piece-specific movement patterns

### Enhanced Game Features ✨

- **Move Analysis System**:
  - `pip` command shows all valid moves for current player
  - Move categorization (captures, castling, en passant, etc.)
  - Move counting and organization
- **Save/Load Game State**: Full game persistence
  - Save games with custom filenames: `save mygame`
  - Load previous games: `load mygame`
  - Preserves board state, castling rights, en passant status, move history
  - Automatic `.sav` file extension handling
- **Comprehensive Move Logging**: All game events logged with timestamps
  - Game start/end logging with player names
  - Move notation in proper chess format (1. e2e4, 1... e7e5)
  - Capture detection and special move logging (castling, en passant)
  - Special events: check, checkmate, stalemate, pawn promotions
  - Automatic log file creation with timestamps

### Visual & UI Features

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
- **Advanced Help System**: Comprehensive command reference with categories

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
java -jar console-chess-1.0.1.jar
```

The JAR includes all latest advanced features, improvements, and bug fixes.

### Option 5: Windows JAR launcher (recommended for end users)

```bat
run-jar.bat
```

Convenient Windows batch script that launches the JAR directly with version display and pause functionality for easy distribution.

### Option 6: Manual compile and run on Windows (no Maven)

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
3. **Available Commands**:

   - **Movement**: `e2e4` (standard notation), `O-O` (kingside castling), `O-O-O` (queenside castling)
   - **Information**: `pip` (show all valid moves), `help` (show all commands)
   - **Game Control**: `quit` or `q` (exit game)
   - **File Operations**: `save <name>` (save game), `load <name>` (load game)

4. **Advanced Rules Implemented**:
   - **Castling**: Use `O-O` for kingside or `O-O-O` for queenside castling
   - **En Passant**: Automatic - just move your pawn diagonally to capture
   - **Pawn Promotion**: When pawn reaches end, choose `Q`, `R`, `B`, or `N`
   - **Check/Checkmate/Stalemate**: Automatically detected and announced

### Game Display

- **White pieces**: `[P] [R] [N] [B] [Q] [K]` - displayed in bright yellow
- **Black pieces**: `(p) (r) (n) (b) (q) (k)` - displayed in cyan
- **Empty squares**: `.` with proper spacing alignment
- **Move logging**: All moves logged to console and `logs/chess_game_TIMESTAMP.log`

### Advanced Command Examples

#### Valid Moves Display (pip command)

```
Enter your move: pip
Valid moves for Alice (WHITE):
a2a3  a2a4  b1a3  b1c3  b2b3  b2b4
c2c3  c2c4  d2d3  d2d4  e2e3  e2e4
f2f3  f2f4  g1f3  g1h3  g2g3  g2g4
h2h3  h2h4
Total moves: 20
```

#### Castling Examples

```
Enter your move: O-O          # Kingside castling
Enter your move: O-O-O        # Queenside castling
```

#### Save/Load Examples

```
Enter your move: save mygame    # Saves to saves/mygame.sav
Enter your move: load mygame    # Loads from saves/mygame.sav
```

### Sample Log Output

```
[2025-09-25 09:16:43] Game started - White: Alice, Black: Bob
[2025-09-25 09:16:48] 1. Alice: e2e4 (P)
[2025-09-25 09:16:52] 1... Bob: e7e5 (P)
[2025-09-25 09:17:10] 2. Alice: O-O
[2025-09-25 09:17:15] 2... Bob: d7d6 (P)
[2025-09-25 09:17:25] Alice (WHITE) promoted pawn at d8 to QUEEN
[2025-09-25 09:17:30] Bob (BLACK) is in CHECK!
[2025-09-25 09:17:35] CHECKMATE! Alice (WHITE) defeats Bob (BLACK)
```

### Quick demo sequences

- **Scholar's Mate** (White wins): `e2e4`, `e7e5`, `d1h5`, `b8c6`, `f1c4`, `g7g6`, `h5f7`
- **Castling Demo**: `e2e4`, `e7e5`, `g1f3`, `b8c6`, `f1c4`, `f8c5`, `O-O`
- **En Passant Setup**: `e2e4`, `d7d5`, `e4d5`, `c7c5`, `d5d6` (en passant)

## Project Structure

```
src/
  main/java/com/consolechess/
    ChessGame.java     # Game loop and CLI with advanced command processing
    Board.java         # Board state with castling, en passant, save/load ✨
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
saves/                 # Auto-created directory for saved games ✨
  *.sav              # Saved game files with full state preservation
```

## Testing

Run the comprehensive test suite:

```bash
mvn test
```

**Current Status**: ✅ All tests passing with advanced features

## Advanced Features Guide

### Castling

- **Requirements**: King and rook haven't moved, path is clear, king not in check
- **Commands**: `O-O` (kingside), `O-O-O` (queenside), or `e1g1`/`e1c1`
- **Validation**: Automatic checking of all castling conditions

### En Passant

- **Trigger**: Opponent pawn moves two squares, landing next to your pawn
- **Capture**: Move diagonally to the square the pawn "passed over"
- **Timing**: Must be done immediately after the two-square move

### Save/Load System

- **Save Location**: `saves/` directory (auto-created)
- **File Format**: Custom format preserving all game state
- **Content**: Board position, castling rights, en passant status, move history
- **Usage**: `save <filename>` and `load <filename>` (`.sav` auto-added)

### Move Analysis (pip command)

- **Display**: All legal moves for current player
- **Categories**: Regular moves, captures, special moves (castling, en passant)
- **Format**: Algebraic notation with move descriptions
- **Count**: Total number of available moves

## Logs Directory

The game automatically creates a `logs/` directory where all game sessions are saved with timestamps:

- **Format**: `chess_game_YYYYMMDD_HHMMSS.log`
- **Content**: Complete game history with move notation, player actions, and special events
- **Features**: Automatic file creation, proper resource cleanup, error handling

Example log files:

- `logs/chess_game_20250925_091641.log`
- `logs/chess_game_20250925_091735.log`

## Recent Improvements 🚀

- **v1.0.1 Latest** (Current JAR):
  - 🐛 **FIXED**: Save/load functionality - pieces now correctly appear after loading
  - 🐛 **FIXED**: Current player state compatibility between save and load operations
  - 🐛 **FIXED**: Piece type enum format mismatch in save files
  - 🐛 **FIXED**: Multi-line board data parsing for proper game restoration
  - ✅ **NEW**: Added backward compatibility for existing save files
  - ✅ **NEW**: run-jar.bat convenience script for easy JAR distribution
- **v1.0.0**:
  - ✅ **NEW**: Complete castling implementation (O-O, O-O-O)
  - ✅ **NEW**: En passant capture with proper validation
  - ✅ **NEW**: pip command for move analysis and suggestion
  - ✅ **NEW**: Save/load game state with full preservation
  - ✅ **NEW**: Advanced help system with categorized commands
  - ✅ **NEW**: Castling notation support (O-O, O-O-O)
  - ✅ Enhanced piece display with brackets `[P]` and parentheses `(p)`
  - ✅ Fixed move numbering logic in chess notation
  - ✅ Added comprehensive MoveLogger with file and console output
  - ✅ Organized logs in dedicated directory
  - ✅ Fixed double logging warnings and resource management
  - ✅ Improved board spacing and alignment
  - ✅ Added shutdown hooks for proper cleanup

## Troubleshooting

- "mvn: command not found": install Maven and ensure it's on your PATH.
- Windows PowerShell quirks with piping: prefer Option 1 or 2 above.
- If Java version is too new/old, use JDK 11+.
- **Unicode display issues**: Latest version uses ASCII-safe characters.
- **Save file location**: Check `saves/` directory in project root.
- **Log file warnings**: Fixed in latest version with proper resource management.
- **Display issues**: Latest version includes enhanced piece formatting and spacing.
