@echo off
setlocal ENABLEDELAYEDEXPANSION
set SRC=src\main\java
set OUT=target\classes

if not exist "%OUT%" mkdir "%OUT%"
echo Compiling Java sources...
javac -d "%OUT%" -cp "%OUT%" "%SRC%\com\consolechess\PieceColor.java" "%SRC%\com\consolechess\PieceType.java" "%SRC%\com\consolechess\Piece.java" "%SRC%\com\consolechess\Position.java" "%SRC%\com\consolechess\Player.java" "%SRC%\com\consolechess\MoveLogger.java" "%SRC%\com\consolechess\Board.java" "%SRC%\com\consolechess\ChessGame.java"
if errorlevel 1 (
  echo.
  echo Build failed. Fix the errors above.
  exit /b 1
)

echo.
echo Starting Console Chess...
java -cp "%OUT%" com.consolechess.ChessGame
