import java.util.ArrayList;

//A class representing a chess board
public class Board{
    
    //The board's layout (including Pieces as well as blank spaces)
    char[][] layout = new char[8][8];

    //Contains all active white pieces
    ArrayList<Piece> whitePieces = new ArrayList<>();

    //Contains all active black pieces
    ArrayList<Piece> blackPieces = new ArrayList<>();

    //Contains all pieces that have died (including white and black)
    ArrayList<Piece> deadPieces = new ArrayList<>();

    //The white team's King
    King whiteKing = new King('E', 1, true);

    //The black team's King
    King blackKing = new King('E', 8, false);

    /**
     * Creates a new blank Board, adds all the Pieces to the game, then adds all the Pieces to the Board
     */
    public Board(){

        //Sets all spaces to the right color
        this.resetBoard();

        //Add all pieces to the ArrayLists at the correct starting positions
        this.resetPieces();
        
        //Add all the pieces to the board
        this.addPieces();
    }

    /**
     * Clears all pieces off the board, generating only blank spaces
    */
    public void resetBoard(){
        
        //Goes through every position and sets it to be alternating white and black spaces
        for (int i = 0; i < layout.length; i++){
            for (int j = 0; j < layout[0].length; j++){
                layout[i][j] = i % 2 == j % 2 ? '▮' : '▯';
            }
        }
    }

    /**
     * Adds all Pieces to whitePieces and blackPieces in their default starting positions
    */
    public void resetPieces(){

        whitePieces.clear();
        blackPieces.clear();
        deadPieces.clear();

        //Pawns
        for (char i = 'A'; i <= 'H'; i++){
            whitePieces.add(new Pawn(i, 2, true));
            blackPieces.add(new Pawn(i, 7, false));
        }
        
        //Knights
        whitePieces.add(new Knight('B', 1, true));
        whitePieces.add(new Knight('G', 1, true));
        blackPieces.add(new Knight('B', 8, false));
        blackPieces.add(new Knight('G', 8, false));

        //Bishops
        whitePieces.add(new Bishop('C', 1, true));
        whitePieces.add(new Bishop('F', 1, true));
        blackPieces.add(new Bishop('C', 8, false));
        blackPieces.add(new Bishop('F', 8, false));
        
        //Rooks
        whitePieces.add(new Rook('A', 1, true));
        whitePieces.add(new Rook('H', 1, true));
        blackPieces.add(new Rook('A', 8, false));
        blackPieces.add(new Rook('H', 8, false));
         
        //Queens
        whitePieces.add(new Queen('D', 1, true));
        blackPieces.add(new Queen('D', 8, false));

        //Kings
        whitePieces.add(whiteKing);
        blackPieces.add(blackKing);
    }

    /**
     * Adds all pieces from whitePieces and blackPieces to the layout
    */
    public void addPieces(){

        //Add the white pieces to the board
        for (int i = 0; i < whitePieces.size(); i++){
            layout[whitePieces.get(i).getPosition().getY()][whitePieces.get(i).getPosition().getX()] = whitePieces.get(i).getSymbol();
        }

        //Add the black pieces to the board
        for (int i = 0; i < blackPieces.size(); i++){
            layout[blackPieces.get(i).getPosition().getY()][blackPieces.get(i).getPosition().getX()] = blackPieces.get(i).getSymbol();
        }
    }

    /**
     * Marks the possible moves for a Piece on the Board's layout
     * @param moves The list of possible moves for the Piece
     */
    public void addMoves(ArrayList<Position> moves){

        //Any possible move will be marked with an 'O'. If a possible move would capture another Piece, the Piece's symbol will display, not the O.
        for (int i = 0; i < moves.size(); i++){
            layout[moves.get(i).getY()][moves.get(i).getX()] = 'O';
        }
    }

    /**
     * Generates the Board's layout in text form
     * @return A string representing the Board's layout
     */
    @Override
    public String toString(){
        
        //Start with the letters at the top
        String result = " A B C D E F G H\n";

        //Go through every row in layout
        for (int i = 0; i < layout.length; i++){

            //Add the numbers to the left side
            result += (8 - i);

            //Add one full row to the String
            for (int j = 0; j < layout[0].length; j++){
                result += layout[i][j] + " ";
            }
            
            //Add the numbers to the right side
            result += (8 - i) + "\n";
        }

        //End with the letters at the bottom
        result += " A B C D E F G H          ";

        //At the very bottom, add all of the dead pieces in the order they died
        for (Piece deadPiece: deadPieces){
            result += deadPiece.getSymbol() + " ";
        }

        return result;
    }

    /**
     * Prints out the Board's layout in a format easy to understand
    */
    public void drawBoard(){

        //Clear all Pieces off the Board
        this.resetBoard();

        //Add the Pieces back to the board in their current positions
        this.addPieces();

        //Print the Board
        System.out.println(this.toString());
    }

    /**
     * Prints out the Board's layout and marks any possible moves for a Piece
     * @param possibleMoves The list of possible normal moves 
     * @param specialMoves The list of pssible special moves 
     */
    public void drawBoardWithMoves(ArrayList<Position> possibleMoves, ArrayList<Position> specialMoves){

        //Clear all Pieces off the Board
        this.resetBoard();

        //Add the possible moves to the Board. This is done before adding the Pieces so that if a Piece can be captureed, it will be drawn over the move
        this.addMoves(possibleMoves);
        this.addMoves(specialMoves);

        //Add the Pieces back to the Board in their current positions
        this.addPieces();

        //Print the Board
        System.out.println(this.toString());
    }
}
 
