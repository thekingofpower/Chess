DESCRIPTION: 
This program allows you to play chess against a bot using the java terminal. 

INSTRUCTIONS:
To run the program, run the main method in Game.java. Make sure unicode characters display properly.

To move, enter the position of the piece you want to move (i.e. A4 or G2). 

Then enter the position of the space you want to move onto. 

Before you move a piece, the piece's legal moves will be marked with an "O". 

Note that any moves that involve capturing an opposing piece will not be marked so that all pieces are always displayed on the board.

The bot's moves are random, but every move the bot makes is a legal move.

EXPLANATION:
This program was made using a 2D array to simulate a chess board. 

The chess pieces use ASCII characters so that every unique piece can be displayed. 

Every type of chess piece has a class, and all of those classes are subclasses of the piece class.

All the chess piece classes have methods for generating all of their legal moves, checking if they have at least one move, and checking if they are able to kill the opposing king

Checkmate detection is the most complicated part of the program. 

To see if the king is in check, every type of piece has a method that checks if they are able to kill the king.

Before a move is added as a "legal move" for a piece, the program ensures the moving team's king would not be in check if the piece made the move.
