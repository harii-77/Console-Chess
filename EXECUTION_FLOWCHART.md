# Console Chess - Detailed Execution Flowchart

## Overview
This document describes the complete execution order and flow of the Console Chess application, from startup to game completion.

---

## Main Execution Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    PROGRAM START                            │
│                  ChessGame.main()                           │
│                  (Line 159-170)                             │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│              CREATE CHESSGAME INSTANCE                      │
│                Constructor (Line 122-132)                   │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ 1. Create Board instance                             │  │
│  │    → Board constructor (Line 105-117)                │  │
│  │    → initializeBoard() (Line 133-166)                │  │
│  │    → Places all pieces in standard starting position │  │
│  │                                                       │  │
│  │ 2. Create Scanner for input                          │  │
│  │                                                       │  │
│  │ 3. Create MoveLogger instance                        │  │
│  │    → MoveLogger constructor (Line 53-62)             │  │
│  │    → initializeLogFile() (Line 71-85)                │  │
│  │    → Creates logs directory                          │  │
│  │    → Creates timestamped log file                    │  │
│  │                                                       │  │
│  │ 4. Set display mode (COLORED_BRACKETED)              │  │
│  │                                                       │  │
│  │ 5. Create saves directory                            │  │
│  │    → createSaveDirectoryIfNeeded() (Line 139-148)    │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│              INITIALIZE GAME                                │
│         game.initializeGame() (Line 186-192)                │
│  ┌──────────────────────────────────────────────────────┐  │
│  │ 1. displayWelcomeMessage() (Line 197-204)            │  │
│  │    → Print game title and banner                     │  │
│  │                                                       │  │
│  │ 2. setupPlayers() (Line 211-231)                     │  │
│  │    → getPlayerName("White") (Line 240-253)           │  │
│  │    → getPlayerName("Black")                          │  │
│  │    → Create Player objects (Line 224-225)            │  │
│  │       • new Player(name, PieceColor.WHITE)           │  │
│  │       • new Player(name, PieceColor.BLACK)           │  │
│  │    → Set currentPlayer = whitePlayer                 │  │
│  │                                                       │  │
│  │ 3. logGameStart() (Line 258-260)                     │  │
│  │    → moveLogger.logGameStart()                       │  │
│  │                                                       │  │
│  │ 4. displayGameFeatures() (Line 265-276)              │  │
│  │    → Show available chess features                   │  │
│  │                                                       │  │
│  │ 5. displayInitialInstructions() (Line 281-290)       │  │
│  │    → Show command list and how to play               │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────────┐
│                  MAIN GAME LOOP                             │
│           game.playGame() (Line 307-329)                    │
│        while (gameRunning && !gameEnded)                    │
└────────────────────┬────────────────────────────────────────┘
                     │
                     ▼
           ╔═════════╩═════════╗
           ║  Loop Iteration   ║
           ╚═════════╦═════════╝
                     │
      ┌──────────────┴──────────────┐
      │                             │
      ▼                             ▼
┌──────────────┐           ┌──────────────────┐
│   Display    │           │  Check Game Over │
│  Game State  │           │  isGameOver()    │
│ (Line 334)   │           │  (Line 313-316)  │
└──────┬───────┘           └────────┬─────────┘
       │                            │
       │ Calls:                     │ If true:
       │ • displayBoard()           │ handleGameEnd()
       │ • displayCurrentPlayerInfo()│ → break loop
       │ • displayGameStatus()      │
       │   - Check if in CHECK      │
       │                            │
       └──────────┬─────────────────┘
                  │
                  ▼
        ┌────────────────────┐
        │  Get Player Input  │
        │  getPlayerInput()  │
        │   (Line 382-385)   │
        │                    │
        │ Scanner reads line │
        │ Trim and lowercase │
        └─────────┬──────────┘
                  │
                  ▼
      ┌───────────────────────────┐
      │   Process Player Input    │
      │  processPlayerInput()     │
      │     (Line 405-420)        │
      │           │               │
      │     Validates input       │
      │           │               │
      │           ▼               │
      │  processInput() ────────────────────┐
      │   (Line 428-496)        │           │
      └───────────────────────────┘           │
                                              │
              ┌───────────────────────────────┘
              │
              ▼
  ┌─────────────────────────────────────────────────────────┐
  │              INPUT PROCESSING DECISION TREE             │
  │                                                         │
  │  ┌────────────────────────────────────────────┐        │
  │  │ Check Input Type (Line 428-496)            │        │
  │  │                                             │        │
  │  │ "quit" or "q"?                              │        │
  │  │   ├─YES→ Set gameRunning=false, gameEnded=true      │
  │  │   │      Log game end, EXIT                │        │
  │  │   │                                         │        │
  │  │ "help"?                                     │        │
  │  │   ├─YES→ displayHelp() (Line 524-552)      │        │
  │  │   │      Show all commands, CONTINUE        │        │
  │  │   │                                         │        │
  │  │ "pip"?                                      │        │
  │  │   ├─YES→ displayValidMoves() (Line 677)    │        │
  │  │   │      Show all legal moves, CONTINUE     │        │
  │  │   │                                         │        │
  │  │ "save <name>"?                              │        │
  │  │   ├─YES→ saveGame(filename) (Line 702)     │        │
  │  │   │      Serialize game state, CONTINUE     │        │
  │  │   │                                         │        │
  │  │ "load <name>"?                              │        │
  │  │   ├─YES→ loadGame(filename) (Line 738)     │        │
  │  │   │      Load game state, CONTINUE          │        │
  │  │   │                                         │        │
  │  │ "display"?                                  │        │
  │  │   ├─YES→ toggleDisplayMode() (Line 827)    │        │
  │  │   │      Switch UNICODE/BRACKETED, CONTINUE │        │
  │  │   │                                         │        │
  │  │ "o-o" or "0-0"? (Kingside castling)        │        │
  │  │   ├─YES→ handleCastling(true) (Line 803)   │        │
  │  │   │      Attempt castling, CONTINUE         │        │
  │  │   │                                         │        │
  │  │ "o-o-o" or "0-0-0"? (Queenside castling)   │        │
  │  │   ├─YES→ handleCastling(false)             │        │
  │  │   │      Attempt castling, CONTINUE         │        │
  │  │   │                                         │        │
  │  │ Length == 4? (e.g., "e2e4")                │        │
  │  │   ├─YES→ Parse as move (Line 476-486)      │        │
  │  │   │      Extract from/to squares            │        │
  │  │   │      │                                  │        │
  │  │   │      └─→ MOVE VALIDATION & EXECUTION ──┼─────┐  │
  │  │   │                                         │     │  │
  │  │   └─NO→ Invalid command message             │     │  │
  │  └────────────────────────────────────────────┘     │  │
  └──────────────────────────────────────────────────────┼──┘
                                                         │
                                                         ▼
  ┌──────────────────────────────────────────────────────────┐
  │              MOVE VALIDATION & EXECUTION                 │
  │                 makeMove() (Line 565-611)                │
  │  ┌────────────────────────────────────────────────────┐ │
  │  │ 1. board.isValidMove(from, to, playerColor)        │ │
  │  │    (Board.java Line 238-269)                       │ │
  │  │    │                                               │ │
  │  │    ├─→ Check position validity                     │ │
  │  │    ├─→ Check piece exists and is player's          │ │
  │  │    ├─→ Check target not occupied by own piece      │ │
  │  │    ├─→ isValidPieceMove() (Line 315-335)          │ │
  │  │    │   • PAWN: forward 1/2, diagonal capture,      │ │
  │  │    │           en passant (Line 337-366)           │ │
  │  │    │   • ROOK: straight lines, path clear          │ │
  │  │    │           (Line 368-373)                      │ │
  │  │    │   • KNIGHT: L-shape (2,1) or (1,2)           │ │
  │  │    │           (Line 375-377)                      │ │
  │  │    │   • BISHOP: diagonal, path clear              │ │
  │  │    │           (Line 379-384)                      │ │
  │  │    │   • QUEEN: rook or bishop move                │ │
  │  │    │           (Line 386-390)                      │ │
  │  │    │   • KING: one square any direction or         │ │
  │  │    │           castling (Line 392-404)             │ │
  │  │    │                                               │ │
  │  │    └─→ Simulate move, check if leaves king in      │ │
  │  │        check (Line 260-268)                        │ │
  │  │        • Temporarily make move                     │ │
  │  │        • Call isInCheck(playerColor)               │ │
  │  │        • Revert move                               │ │
  │  │        • Return result                             │ │
  │  │                                                     │ │
  │  │ 2. IF INVALID:                                     │ │
  │  │    → Print "Invalid move!"                         │ │
  │  │    → Return false, CONTINUE game loop              │ │
  │  │                                                     │ │
  │  │ 3. IF VALID:                                       │ │
  │  │    board.makeMove(from, to) (Line 274-310)        │ │
  │  │    ┌────────────────────────────────────────────┐ │ │
  │  │    │ a. Check for CASTLING                      │ │ │
  │  │    │    → King moves 2 squares horizontally     │ │ │
  │  │    │    → performCastling() (Line 611-647)     │ │ │
  │  │    │    → Move king and rook simultaneously     │ │ │
  │  │    │    → Update castling rights               │ │ │
  │  │    │                                            │ │ │
  │  │    │ b. Check for EN PASSANT capture            │ │ │
  │  │    │    → Pawn captures to en passant target   │ │ │
  │  │    │    → performEnPassantCapture() (Line 652) │ │ │
  │  │    │    → Remove captured pawn                 │ │ │
  │  │    │                                            │ │ │
  │  │    │ c. Check for PAWN TWO-SQUARE MOVE          │ │ │
  │  │    │    → Set en passant target for next turn  │ │ │
  │  │    │                                            │ │ │
  │  │    │ d. REGULAR MOVE                            │ │ │
  │  │    │    → setPiece(to, piece)                  │ │ │
  │  │    │    → setPiece(from, null)                 │ │ │
  │  │    │    → updateCastlingRights() (Line 674)   │ │ │
  │  │    │    → Update en passant target             │ │ │
  │  │    │    → Add to move history                  │ │ │
  │  │    └────────────────────────────────────────────┘ │ │
  │  │                                                     │ │
  │  │ 4. Log the move                                    │ │
  │  │    → moveLogger.logMove() (Line 577-578)          │ │
  │  │                                                     │ │
  │  │ 5. Check for PAWN PROMOTION                        │ │
  │  │    → handlePromotionIfNeeded() (Line 616-645)     │ │
  │  │    → If pawn reached back rank:                   │ │
  │  │       • Prompt for promotion piece (Q/R/B/N)      │ │
  │  │       • Replace pawn with chosen piece            │ │
  │  │       • Log promotion                             │ │
  │  │                                                     │ │
  │  │ 6. Check OPPONENT's status                         │ │
  │  │    → board.isInCheck(opponentColor) (Line 512)    │ │
  │  │    ┌────────────────────────────────────────────┐ │ │
  │  │    │ a. Find king position (Line 541-551)      │ │ │
  │  │    │ b. isSquareAttacked() (Line 553-585)      │ │ │
  │  │    │    → Check if any opponent piece attacks  │ │ │
  │  │    │      king's square                        │ │ │
  │  │    │    → Iterate all pieces of attacking color│ │ │
  │  │    │    → Check if piece can reach king square │ │ │
  │  │    └────────────────────────────────────────────┘ │ │
  │  │                                                     │ │
  │  │    IF IN CHECK:                                    │ │
  │  │    ├─→ Print "Check!"                              │ │
  │  │    ├─→ Log check                                   │ │
  │  │    └─→ Check for CHECKMATE                         │ │
  │  │        → hasAnyLegalMove(opponentColor)            │ │
  │  │          (Line 521-539)                            │ │
  │  │        → Try all possible moves                    │ │
  │  │        → If NO legal moves:                        │ │
  │  │           • Print "Checkmate!"                     │ │
  │  │           • Log checkmate                          │ │
  │  │           • Set gameRunning=false, gameEnded=true  │ │
  │  │           • EXIT game loop                         │ │
  │  │                                                     │ │
  │  │    IF NOT IN CHECK:                                │ │
  │  │    └─→ Check for STALEMATE                         │ │
  │  │        → hasAnyLegalMove(opponentColor)            │ │
  │  │        → If NO legal moves:                        │ │
  │  │           • Print "Stalemate!"                     │ │
  │  │           • Log stalemate                          │ │
  │  │           • Set gameRunning=false, gameEnded=true  │ │
  │  │           • EXIT game loop                         │ │
  │  │                                                     │ │
  │  │ 7. switchPlayer() (Line 650-652)                   │ │
  │  │    → currentPlayer = (currentPlayer == whitePlayer)│ │
  │  │       ? blackPlayer : whitePlayer                  │ │
  │  │                                                     │ │
  │  │ 8. Return to main game loop                        │ │
  │  └────────────────────────────────────────────────────┘ │
  └──────────────────────────────────────────────────────────┘
                           │
                           ▼
                 ┌─────────────────┐
                 │  Loop continues │
                 │  until game end │
                 └────────┬────────┘
                          │
                          ▼
              ┌──────────────────────┐
              │    GAME CLEANUP      │
              │  cleanupGame()       │
              │  (Line 501-519)      │
              │  ┌────────────────┐  │
              │  │ • Log game end │  │
              │  │ • Close scanner│  │
              │  │ • Display bye  │  │
              │  │   message      │  │
              │  └────────────────┘  │
              └──────────┬───────────┘
                         │
                         ▼
              ┌────────────────────┐
              │   PROGRAM EXIT     │
              │                    │
              │ Logger shutdown    │
              │ hook closes log    │
              │ file automatically │
              └────────────────────┘
```

---

## Detailed Component Interaction

### 1. Object Creation Sequence
```
main()
  └─→ new ChessGame()
      ├─→ new Board()
      │   └─→ initializeBoard()
      │       └─→ new Piece(type, color) × 32 pieces
      ├─→ new Scanner(System.in)
      ├─→ new MoveLogger()
      │   └─→ initializeLogFile()
      │       └─→ Creates "logs/chess_game_TIMESTAMP.log"
      └─→ createSaveDirectoryIfNeeded()
          └─→ Creates "saves/" directory
```

### 2. Player Initialization Sequence
```
initializeGame()
  ├─→ displayWelcomeMessage()
  ├─→ setupPlayers()
  │   ├─→ getPlayerName("White")
  │   │   └─→ Scanner.nextLine() [BLOCKS for input]
  │   ├─→ getPlayerName("Black")
  │   │   └─→ Scanner.nextLine() [BLOCKS for input]
  │   ├─→ new Player(whiteName, WHITE)
  │   └─→ new Player(blackName, BLACK)
  ├─→ logGameStart()
  ├─→ displayGameFeatures()
  └─→ displayInitialInstructions()
```

### 3. Move Validation Chain
```
processInput("e2e4")
  └─→ makeMove(from, to, fromSquare, toSquare)
      └─→ board.isValidMove(from, to, playerColor)
          ├─→ Check position bounds
          ├─→ Check piece ownership
          ├─→ Check target square
          ├─→ isValidPieceMove(piece, from, to)
          │   └─→ Type-specific validation:
          │       • Pawn: forward/capture rules
          │       • Rook: straight lines + isPathClear()
          │       • Knight: L-shape
          │       • Bishop: diagonal + isPathClear()
          │       • Queen: rook OR bishop rules
          │       • King: 1 square OR castling
          └─→ Simulate move + check if leaves king in check
              ├─→ Temporary: setPiece(to, piece)
              ├─→ Temporary: setPiece(from, null)
              ├─→ isInCheck(playerColor)
              │   ├─→ findKing(playerColor)
              │   └─→ isSquareAttacked(kingPos, opponentColor)
              ├─→ Revert: setPiece(from, piece)
              ├─→ Revert: setPiece(to, captured)
              └─→ return !leavesKingInCheck
```

### 4. Check/Checkmate Detection Flow
```
After move is made:
  └─→ Get opponent = (currentPlayer == white) ? black : white
      └─→ board.isInCheck(opponent.color)
          ├─→ IF TRUE (in check):
          │   ├─→ Print "Check!"
          │   ├─→ Log check event
          │   └─→ board.hasAnyLegalMove(opponent.color)
          │       ├─→ IF FALSE (no legal moves):
          │       │   ├─→ CHECKMATE!
          │       │   ├─→ Print winner
          │       │   ├─→ Log checkmate
          │       │   └─→ End game
          │       └─→ IF TRUE: continue (check only)
          └─→ IF FALSE (not in check):
              └─→ board.hasAnyLegalMove(opponent.color)
                  └─→ IF FALSE (no legal moves):
                      ├─→ STALEMATE!
                      ├─→ Print draw message
                      ├─→ Log stalemate
                      └─→ End game
```

### 5. Special Move Handling

#### Castling Flow
```
Input: "o-o" (kingside) or "o-o-o" (queenside)
  └─→ handleCastling(isKingside)
      └─→ Construct king positions (start and target)
      └─→ makeMove(kingPos, kingDestPos)
          └─→ board.isValidMove() checks:
              ├─→ King hasn't moved
              ├─→ Rook hasn't moved
              ├─→ Path is clear
              ├─→ King not in check
              ├─→ King doesn't pass through check
              └─→ King doesn't end in check
          └─→ board.makeMove() detects castling:
              └─→ performCastling()
                  ├─→ Move king 2 squares
                  ├─→ Move rook to other side of king
                  ├─→ Update castling rights (all set to moved)
                  └─→ Add "O-O" or "O-O-O" to history
```

#### En Passant Flow
```
Turn N: Pawn moves two squares
  └─→ board.makeMove() detects two-square pawn move
      └─→ Set enPassantTarget = square behind pawn

Turn N+1: Opponent tries to capture en passant
  └─→ board.isValidMove() checks:
      ├─→ Moving piece is pawn
      ├─→ Diagonal move to enPassantTarget
      └─→ Valid if target matches
  └─→ board.makeMove() detects en passant:
      └─→ performEnPassantCapture()
          ├─→ Move pawn to target square
          ├─→ Remove captured pawn (one row back)
          ├─→ Add "e.p." notation to history
          └─→ Clear enPassantTarget

Turn N+2: En passant opportunity expires
  └─→ enPassantTarget cleared if not used
```

#### Pawn Promotion Flow
```
Pawn reaches back rank (row 0 for white, row 7 for black)
  └─→ After makeMove() completes:
      └─→ handlePromotionIfNeeded(position)
          ├─→ Check if piece is pawn
          ├─→ Check if at back rank
          └─→ IF TRUE:
              ├─→ Print prompt: "Promote pawn to (Q/R/B/N):"
              ├─→ Scanner.nextLine() [BLOCKS for input]
              ├─→ Parse choice (q/r/b/n)
              ├─→ Loop until valid choice
              ├─→ board.setPiece(pos, new Piece(type, color))
              ├─→ Print confirmation
              └─→ Log promotion
```

---

## Key State Variables Tracking

### Game State
- `gameRunning`: Controls main loop (set false on quit)
- `gameEnded`: Prevents duplicate end processing
- `currentPlayer`: Switches between white and black
- `moveNumber`: Increments after each black move

### Board State
- `squares[8][8]`: Piece positions
- `enPassantTarget`: Set after two-square pawn move, cleared after one turn
- `whiteKingMoved`, `blackKingMoved`: Castling rights
- `whiteKingsideRookMoved`, `whiteQueensideRookMoved`: Castling rights
- `blackKingsideRookMoved`, `blackQueensideRookMoved`: Castling rights
- `moveHistory`: List of all moves in notation
- `displayMode`: Controls piece display (UNICODE vs BRACKETED)

### Logger State
- `moveNumber`: Current move number (1, 2, 3...)
- `isWhiteTurn`: Tracks whose turn for move notation
- `logWriter`: BufferedWriter for log file
- `isClosed`: Prevents writing after close

---

## Execution Timing

### Blocking Points (User Input Required)
1. **Startup**: Player name input (white and black)
2. **Every Turn**: Move/command input
3. **Pawn Promotion**: Piece selection input

### Asynchronous Operations
- None (single-threaded, console-based)

### File I/O Operations
1. **Startup**: Create logs directory and log file
2. **Startup**: Create saves directory
3. **During Game**: Write to log file (buffered, flushed immediately)
4. **Save Command**: Write game state to .sav file
5. **Load Command**: Read game state from .sav file
6. **Shutdown**: Close log file (via shutdown hook)

---

## Error Handling Flow

```
Try-Catch Hierarchy:

main() try-catch:
  └─→ Catches any exception during game execution
      └─→ Print error message
      └─→ Print stack trace
      └─→ System.exit(1)

playGame() try-catch:
  └─→ Catches exceptions during game loop
      └─→ Print error message
      └─→ Log event to move logger
      
finally block in playGame():
  └─→ Always calls cleanupGame()
      └─→ Log game end (if not already logged)
      └─→ Close scanner
      └─→ Display farewell message

Shutdown hook (MoveLogger):
  └─→ Runtime.getRuntime().addShutdownHook()
      └─→ Ensures log file is closed
      └─→ Even if program terminates unexpectedly
```

---

## Performance Considerations

### Move Validation Complexity
- **isValidMove()**: O(1) for piece pattern check
- **isPathClear()**: O(n) where n = distance between squares (max 7)
- **isInCheck()**: O(64) - checks all squares for attacking pieces
- **hasAnyLegalMove()**: O(64 × 64) = O(4096) - tries all possible moves
  - Called after each move to check checkmate/stalemate
  - Most expensive operation in the game

### Memory Usage
- Board: 8×8 array of Piece objects = 64 references
- MoveHistory: ArrayList grows with each move
- Scanner: Buffers stdin
- Logger: Buffers output to file

---

## Summary of Execution Order

1. **Start** → main()
2. **Create** → ChessGame, Board, MoveLogger, Scanner
3. **Initialize Board** → Place 32 pieces in starting position
4. **Initialize Game** → Get player names, show instructions
5. **Main Loop START**
   - Display board and game state
   - Check for game over
   - Get user input (BLOCKING)
   - Process input (command or move)
   - If move: validate → execute → check → switch player
6. **Main Loop END** (when gameRunning=false or gameEnded=true)
7. **Cleanup** → Close scanner, log final events
8. **Exit** → Shutdown hook closes log file

The game uses a **synchronous, single-threaded, turn-based** execution model with **blocking input** at each turn, making the flow very predictable and easy to follow.
