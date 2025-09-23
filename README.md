# Console Chess

A clean, console-based two-player chess game written in Java.

## Features

- Board setup with standard starting positions
- Turn-based two-player gameplay
- Legal move validation (includes path blocking for sliders)
- King safety: moves that leave your king in check are rejected
- Check, checkmate, and stalemate detection with messages
- Pawn promotion (Q/R/B/N) when reaching the back rank
- Simple console UI with friendly prompts and input validation

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

## How to Play

1. Start the program and enter player names when prompted.
2. Enter moves in coordinate notation: `e2e4` means from `e2` to `e4`.
   - Files: `a` to `h`, Ranks: `1` to `8`.
3. Commands:
   - `help`: show usage
   - `quit` or `q`: exit
4. Special rules implemented:
   - Check: after a move, if the opponent is in check, you’ll see “Check!”.
   - Checkmate: if the opponent has no legal moves while in check, the winner is announced and the game ends.
   - Stalemate: if the opponent has no legal moves and is not in check, the game is a draw.
   - Pawn promotion: when a pawn reaches the last rank, choose `Q`, `R`, `B`, or `N`.

### Quick demo sequences

- Scholar’s Mate (White wins):
  - White: `e2e4`, Black: `e7e5`, White: `d1h5`, Black: `b8c6`, White: `f1c4`, Black: `g7g6`, White: `h5f7`

## Project Structure

```
src/
  main/java/com/consolechess/
    ChessGame.java    # Game loop and CLI
    Board.java        # Board state and validation (including check logic)
    Piece.java        # Piece model
    Position.java     # Coordinate model
    Player.java       # Player info
    PieceType.java    # Enum of piece types
    PieceColor.java   # Enum of colors
  test/java/com/consolechess/
    BoardTest.java    # Sample unit tests
    PieceTest.java
    PositionTest.java
```

## Testing

```bash
mvn test
```

## Troubleshooting

- “mvn: command not found”: install Maven and ensure it’s on your PATH.
- Windows PowerShell quirks with piping: prefer Option 1 or 2 above.
- If Java version is too new/old, use JDK 11+.

## License

MIT (or your preferred license). Update this section if you choose a different license.
