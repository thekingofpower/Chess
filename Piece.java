import java.util.ArrayList;

//A class for a Piece on a chess board
public class Piece {

    //The Position of the Piece
    private Position piecePosition;

    //Whether or not the Piece has been captured
    private boolean alive;

    //The color of the Piece (white or black)
    private boolean white;

    //The text symbol used to represent the Piece
    private char symbol;

    //The type of the Piece (Pawn, Bishop, Queen, etc.)
    private String name;

    //Whether or not the piece has already moved
    private boolean moved;

    /**
     * Creates a new Piece
     * @param letter The letter of the Position of the Piece
     * @param number The number of the Position of the Piece
     * @param white Whether or not the Piece is white
     */
    public Piece(char letter, int number, boolean white){
      
        piecePosition = new Position(letter, number);
        
        alive = true;
        
        this.white = white;

        symbol = white ? '♟' : '♙';

        name = "Piece";

        moved = false;
    }

    //Getter methods

    public Position getPosition(){
        return piecePosition;
    }

    public boolean getAlive(){
        return alive;
    }

    public boolean getWhite(){
        return white;
    }

    public char getSymbol(){
        return symbol;
    }

    public String getName(){
        return name;
    }

    public boolean getMoved(){
        return moved;
    }

    //Setter methods

    public void setAlive(boolean newAlive){
        alive = newAlive;
    }

    public void setName(String newName){
        name = newName;
    }

    public void setSymbol(char newSymbol){
        symbol = newSymbol;
    }
   
    /**
     * Generates all legal moves for a Piece (Not including special cases such as castling or en passant)
     * @param samePieces The list of all Pieces on the same team as the moving Piece
     * @param oppositePieces The list of all Pieces on the opposite team as the moving Piece
     * @param sameKing The King on the same team as the moving Piece
     * @return An ArrayList containing all possible moves for the Piece
     */
    public ArrayList<Position> getMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        ArrayList<Position> possibleMoves = new ArrayList<Position>();          
        return possibleMoves;             
    }

    /**
     * Generates all legal special moves for a Piece (Such as castling or en passant)
     * @param samePieces The list of all Pieces on the same team as the moving Piece
     * @param oppositePieces The list of all Pieces on the opposite team as the moving Piece
     * @param sameKing The King on the same team as the moving Piece
     * @return An ArrayList containing all possible special moves for the Piece
     */
    public ArrayList<Position> getSpecialMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        ArrayList<Position> possibleSpecialMoves = new ArrayList<Position>();
        return possibleSpecialMoves;
    }
        
    /**
     * Checks whether or not a Piece has at least one legal move (Not including special cases such as castling or en passant)
     * @param samePieces The list of all Pieces on the same team as the moving Piece
     * @param oppositePieces The list of all Pieces on the opposite team as the moving Piece
     * @param sameKing The King on the same team as the moving Piece
     * @return true if the Piece has at least one possible move and false otherwise
     */
    public boolean hasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        return false;                
    }

    /**
     * Checks whether or not a Piece has at least one legal special move (Such as castling or en passant)
     * @param samePieces The list of all Pieces on the same team as the moving Piece
     * @param oppositePieces The list of all Pieces on the opposite team as the moving Piece
     * @param sameKing The King on the same team as the moving Piece
     * @return true if the Piece has at least one possible special move and false otherwise
     */
    public boolean hasSpecialMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        return false;                
    }
        
    /**
     * Moves the Piece to a new Position
     * @param move The Position of the move
     * @param samePieces The Pieces on the same team as the moving Piece
     */
    void move(Position move, ArrayList<Piece> samePieces){
        moved = true;
        this.piecePosition.setPosition(move);
    }

    /**
     * Moves the Piece to a new Position using a special move and captures another Piece if necessary
     * @param move The Position of the special move
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @param deadPieces The Pieces that have died
     * @return true if the special move captures another Piece and false otherwise
     */
    public boolean specialMove(Position specialMove, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, ArrayList<Piece> deadPieces){
        moved = true;
        this.piecePosition.setPosition(specialMove);
        return false;
    }

    /**
     * Check if a Piece can make a move (Meaning the move is within the bounds of the board, is not occupied by another Piece on the same team, and it would not put the King in check)
     * @param newPosition The Position of the move
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @param sameKing The King on the moving Piece's team
     * @return true if the move is valid and false otherwise
     */
    public boolean canMakeMove(Position newPosition, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        if (!newPosition.isInBounds() || newPosition.isOccupied(samePieces) || this.wouldPutKingInCheck(newPosition, samePieces, oppositePieces, sameKing)){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Figures out if a move would put a team's own King in check
     * @param virtualPosition The move to test
     * @param samePieces The Pieces on the same team as the moving Piece
     * @param oppositePieces The Pieces on the opposite team as the moving Piece
     * @param sameKing The King on the same team as the moving Piece
     * @return true if the move would put the King in check and false otherwise
     */
    boolean wouldPutKingInCheck(Position virtualPosition, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        /*
        To test if the move would put the king in check, the Piece will make the move, then the move will be undone at the end.
        This stores the original Position of the Piece so that the move can be undone later.
        */

        char originalLetter = this.piecePosition.getLetter();
        int originalNumber = this.piecePosition.getNumber();

        //Make the move (Will be undone later)
        this.piecePosition.setPosition(virtualPosition);

        //If the move captures a Piece, it is important to take note of that so the Piece can be revived when the move is undone
        Piece capturedPiece = this.piecePosition.getPieceOnSpace(oppositePieces);

        if (capturedPiece != null){
            capturedPiece.setAlive(false);
            oppositePieces.remove(capturedPiece);
        }

        //Assume the move is valid. If it is found that this move would put the King in check, this will be set to false.
        boolean wouldKingBeChecked = false;

        //Check if the King is in check now that the move has been made
        if (sameKing.isChecked(samePieces, oppositePieces)){
            wouldKingBeChecked = true;
        }         
    
        //Undo the move by returning the Position to what it was before
        this.piecePosition.setPosition(new Position(originalLetter, originalNumber));

        //If the move captured a Piece, revive the Piece
        if (capturedPiece != null){
            capturedPiece.setAlive(true);
            oppositePieces.add(capturedPiece);
        }
        
        return wouldKingBeChecked;
    }

    /**
     * Checks if a Piece is able to capture the oppose team's King
     * @param samePieces The Pieces on the same team as the Piece
     * @param oppositePieces The Pieces on the opposite team as the Piece
     * @param oppositeKing The King on the opposite team as the Piece
     * @return true if the Piece can capture the King and false otherwise
     */
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){
        return false;
    }

    /**
     * Adds a series of possible moves for a Piece in a specific direction (Used for Rook, Bishop, and Queen)
     * @param possibleMoves The list of possible moves to add to
     * @param xMod The modifier for the x axis (1 for right, -1 for left, 0 for no change)
     * @param yMod The modifier for the y axis (1 for up, -1 for down, 0 for no change)
     * @param samePieces The Pieces on the same team as the Piece that is getting moves
     * @param oppositePieces The Pieces on the opposite team as the Piece that is getting moves
     * @param sameKing The King on the same team as the Piece that is getting moves
     */
    public void addDirectionalMoves(ArrayList<Position> possibleMoves, int xMod, int yMod, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        //moveX and moveY represent the positions for all of the moves to check for
        int moveX = this.getPosition().getX() + xMod;
        int moveY = this.getPosition().getY() + yMod;

        //If the move is out of bounds or if one of the moves is blocked by a Piece on the same team, stop looking for more moves.
        while (moveX <= 7 && moveX >= 0 && moveY <= 7 && moveY >= 0 && !(new Position(moveX, moveY).isOccupied(samePieces))){

            //As long as the move would not put the King in check, it is a valid move
            if (!this.wouldPutKingInCheck(new Position(moveX, moveY), samePieces, oppositePieces, sameKing)){
                possibleMoves.add(new Position(moveX, moveY));
            }
            if (new Position(moveX, moveY).isOccupied(oppositePieces)){
                //If a move is blocked by a Piece on the opposite team, the Piece cannot go further than capturing that Piece
                break;
            }

            //Modify moveX and moveY depending on the direction
            moveX += xMod;
            moveY += yMod;
        }
    }

    /**
     * Checks if a Piece can make any moves in a specific direction (used for Rook, Bishop, and Queen)
     * @param xMod The modifier for the x axis (1 for right, -1 for left, 0 for no change)
     * @param yMod The modifier for the y axis (1 for up, -1 for down, 0 for no change)
     * @param samePieces The Pieces on the same team as the Piece that is being checked for moves
     * @param oppositePieces The Pieces on the opposite team as the Piece that is being checked for moves
     * @param sameKing The King on the same team as the Piece that is being checked for moves
     * @return true if the Piece has any valid moves in the direction and false otherwise
     */
    public boolean hasDirectionalMoves(int xMod, int yMod, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        int moveX = this.getPosition().getX() + xMod;
        int moveY = this.getPosition().getY() + yMod;

        while (moveX <= 7 && moveX >= 0 && moveY <= 7 && moveY >= 0 && !(new Position(moveX, moveY).isOccupied(samePieces))){

            if (this.canMakeMove(new Position(moveX, moveY), samePieces, oppositePieces, sameKing)){
                //If a legal move is found, return true
                return true;
            }
            if (new Position(moveX, moveY).isOccupied(oppositePieces)){
                break;
            }

            moveX += xMod;
            moveY += yMod;
        }

        //If no legal moves are found, return false
        return false;
    }

    /**
     * Checks if a Piece can capture the oppsite team's King by moving in a specific direction (Used for Rook, Bishop, and Queen)
     * @param xMod The modifier for the x axis (1 for right, -1 for left, 0 for no change)
     * @param yMod The modifier for the y axis (1 for up, -1 for down, 0 for no change)
     * @param samePieces The Pieces on the same team as the Piece that is being checked 
     * @param oppositePieces The Pieces on the opposite team as the Piece that is being checked
     * @param oppositeKing The King on the opposite team as the Piece that is being checked
     * @return true if the Piece can capture the King by moving in the direction and false otherwise
     */
    public boolean canCaptureKingInDirection(int xMod, int yMod, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){

        int moveX = this.getPosition().getX() + xMod;
        int moveY = this.getPosition().getY() + yMod;

        while (moveX <= 7 && moveX >= 0 && moveY <= 7 && moveY >= 0 && !(new Position(moveX, moveY).isOccupied(samePieces))){

            if (new Position(moveX, moveY).samePosition(oppositeKing.getPosition())){
                //If the move has the same Position as the King, return true
                return true;
            }
            if (new Position(moveX, moveY).isOccupied(oppositePieces)){
                //If the move is blocked by an opposing Piece that is not the King, break
                break;
            }

            moveX += xMod;
            moveY += yMod;
        }

        //If no moves are found that could capture the King, return false
        return false;
    }
}
