#!/usr/bin/env bash
set -euo pipefail

DOT_FILE="$(pwd)/chess_game_flow.dot"
SVG_FILE="$(pwd)/chess_game_flow.svg"
PNG_FILE="$(pwd)/chess_game_flow.png"

printf "Generating flowchart DOT: %s\n" "$DOT_FILE"

cat > "$DOT_FILE" <<'DOT'
digraph ChessGame {
  rankdir=TB;
  fontname="Helvetica";
  node [fontname="Helvetica", shape=box];
  edge [fontname="Helvetica"];

  Start [shape=circle, label="Start"];
  End [shape=doublecircle, label="End"];

  subgraph cluster_boot {
    label="Boot";
    style=dashed;
    main [label="ChessGame.main(args)"];
    construct [label="new ChessGame()\n- new Board\n- new Scanner\n- new MoveLogger\n- displayMode=COLORED_BRACKETED\n- create saves/"];
    init [label="initializeGame()\n- welcome\n- setupPlayers\n- logGameStart\n- features\n- instructions"];
  }

  subgraph cluster_loop {
    label="Game Loop";
    style=dashed;
    loop_chk [shape=diamond, label="gameRunning && !gameEnded"];
    display [label="displayGameState()\n- displayBoard()\n- displayCurrentPlayerInfo()\n- displayGameStatus()"];
    incheck [shape=diamond, label="Board.isInCheck(current)"];
    getinput [label="getPlayerInput() (Scanner)"];
    proc [label="processInput(input)"];

    help [label="displayHelp()"]; 
    pip [label="displayValidMoves()\nBoard.getAllValidMoves(currentColor)"]; 
    displaycmd [label="toggleDisplayMode()\nBoard.setDisplayMode()\ndisplayBoard()"]; 
    save [label="save <name>\nsaveGame(name)\nBoard.saveGameState()\nwrite saves/<name>.sav"]; 
    load [label="load <name>\nread file\nBoard.loadGameState()\nrestore players/current"]; 
    quit [label="quit | q\nlogGameEnd('quit by player')\nset gameEnded=true, gameRunning=false"]; 

    castle [label="handleCastling(k/q)\nmakeMove() with king 2 cols"]; 
    parsemove [label="parse 'e2e4' -> Positions"]; 
    valid [shape=diamond, label="Board.isValidMove?"]; 
    execmove [label="Board.makeMove()\n- castling/en passant/regular\n- update rights/en passant/history"]; 
    logmove [label="MoveLogger.logMove()"]; 
    promo [shape=diamond, label="Pawn on back rank?"]; 
    dopromo [label="Promote pawn\nboard.setPiece\nlogPromotion"]; 

    checkOpp [shape=diamond, label="Board.isInCheck(opponent)?"]; 
    mate [shape=diamond, label="hasAnyLegalMove(opponent)?"]; 
    stalemate [shape=diamond, label="hasAnyLegalMove(opponent)?"]; 

    switchp [label="switchPlayer()"]; 
    invalid [label="Print 'Invalid' and continue"]; 
    loop_cont1 [label="Continue", shape=point]; 

    win [label="Checkmate\nlogCheckmate\nend game"]; 
    draw [label="Stalemate\nlogStalemate\nend game"]; 
  }

  Start -> main -> construct -> init -> loop_chk;
  loop_chk -> display [label="Yes"];
  loop_chk -> cleanup [label="No"];

  display -> incheck;
  incheck -> getinput [label="log check if true"];
  incheck -> getinput [label="false"];

  getinput -> proc;

  proc -> help [label="help"];
  help -> loop_chk;

  proc -> pip [label="pip"];
  pip -> loop_chk;

  proc -> displaycmd [label="display"];
  displaycmd -> loop_chk;

  proc -> save [label="save <name>"];
  save -> loop_chk;

  proc -> load [label="load <name>"];
  load -> loop_chk;

  proc -> quit [label="quit | q"];
  quit -> cleanup;

  proc -> castle [label="o-o | 0-0 | o-o-o | 0-0-0"]; 
  castle -> switchp; 
  switchp -> loop_chk;

  proc -> parsemove [label="e2e4-like (len==4)"]; 
  parsemove -> valid; 
  valid -> invalid [label="No"]; 
  invalid -> loop_chk; 

  valid -> execmove [label="Yes"]; 
  execmove -> logmove -> promo; 
  promo -> dopromo [label="Yes"]; 
  dopromo -> checkOpp; 
  promo -> checkOpp [label="No"]; 

  checkOpp -> mate [label="In check"]; 
  mate -> win [label="No (checkmate)"]; 
  win -> cleanup; 

  mate -> loop_cont1 [label="Yes (has moves)"]; 
  loop_cont1 -> switchp; 

  checkOpp -> stalemate [label="Not in check"]; 
  stalemate -> draw [label="No moves"]; 
  draw -> cleanup; 
  stalemate -> switchp [label="Has moves"]; 

  cleanup [label="cleanupGame()\n- log end if needed\n- close scanner\n- farewell", shape=box]; 
  cleanup -> End; 
}
DOT

if command -v dot >/dev/null 2>&1; then
  printf "Rendering SVG -> %s\n" "$SVG_FILE"
  dot -Tsvg "$DOT_FILE" -o "$SVG_FILE"
  printf "Rendering PNG -> %s\n" "$PNG_FILE"
  dot -Tpng "$DOT_FILE" -o "$PNG_FILE"
  printf "Done. Open the SVG/PNG in your viewer.\n"
else
  printf "Graphviz 'dot' not found. Generated DOT only.\n"
  printf "Install graphviz (e.g., apt-get install -y graphviz) to render.\n"
fi
