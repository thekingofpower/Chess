import java.util.ArrayList;

//A class for a Position on a chess board
public class Position {
    
    //The x value of the Position on the game board's 2D array
    private int x;

    //The y value of the Position on the game board's 2D array
    private int y;

    //The letter (x position) of the Position on the game board as seen by the player
    private char letter;

    //The number (y position) of the Position on the game board as seen by the player
    private int number;

    //Whether or not the Position is for a special move (A unique type of move such as en passant or castling)
    private boolean special;

    /**
     * Creates a new Position using the letter and number of the game board
     * @param letter The letter of the Position on the game board
     * @param number The number of the Position on the game board
     */
    public Position(char letter, int number){

        this.number = number;
        this.letter = letter;
        
        special = false;

        //The x and y positions of the board's 2D array layout must be calculated based on the number and letter
        y = 8 - number;
        x = "ABCDEFGH".indexOf(letter);
    }

    /**
     * Creates a new Position using the x and y values of the game board's 2D array
     * @param x The x value of the Position on the board's 2D array
     * @param y The y value of the Position on the board's 2D array
     */
    public Position(int x, int y){

        this.x = x;
        this.y = y;

        //The number and letter on the game board seen by the user must be calculated based on the x and y values
        if (x >= 0 && y >= 0 && x < 8 && y < 8){
            letter = "ABCDEFGH".charAt(x);
            number = 8 - y;
        }
    }

    /**
     * Creates a new Position using the letter and number of the game board, and allows for the Position to be made special or not
     * @param letter The letter of the Position on the game board
     * @param number The number of the Position on the game board
     * @param special Whether or not the Position is for a special move
     */
    public Position(char letter, int number, boolean special){

        this.number = number;
        this.letter = letter;
        this.special = special;

        y = 8 - number;
        x = "ABCDEFGH".indexOf(letter);
    }

    //Getter methods

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public char getLetter(){
        return letter;
    }

    public int getNumber(){
        return number;
    }

    public boolean getSpecial(){
        return special;
    }

    //Setter methods

    public void setX(int newX){
        x = newX;

        //The letter must be recalculated based on this new x value
        letter = "ABCDEFGH".charAt(x);
    }

    public void setY(int newY){
        y = newY;

        //The number must be recalculated based on this new y value
        number = 8 - y;
    }

    public void setLetter(char newLetter){
        letter = newLetter;

        //The x value must be recalculated based on this new letter
        x = "ABCDEFGH".indexOf(letter);

    }

    public void setNumber(int newNumber){
        number = newNumber;

        //The y value must be recalculated based on this new number
        y = 8 - number;
    }

    /**
     * Changes the letter and number of the Position
     * @param newPosition A Position containing the updated letter and number for the Position
     */
    public void setPosition(Position newPosition){
        this.number = newPosition.number;
        y = 8 - this.number;

        this.letter = newPosition.letter;
        x = "ABCDEFGH".indexOf(this.letter);
    }

    /**
     * Checks whether or not two Positions are the same (meaning they have the same x value and the same y value)
     * @param secondPosition The Position to compare
     * @return true if the two Positions are the same and false otherwise
     */
    public boolean samePosition(Position secondPosition){

        //Check if the Positions occupy the same space
        return this.x == secondPosition.x && this.y == secondPosition.y;
    }

    /**
     * Checks whether or not the Position is within the bounds of a chess board
     * @return true if the Position is within the bounds and false otherwise
     */
    public boolean isInBounds(){

        //If a move is outside the limits of the board or is blocked, return false
        if (this.x > 7 || this.x < 0 || this.y > 7 || this.y < 0){
            return false;
        }
        else{
            return true;
        }
    }


    /**
     * Determines whether a Position is occupied by a piece
     * @param pieces The set of Pieces that may occupy the Position 
     * @return true if the Position is occupied by one of the Pieces and false otherwise
    */
    public boolean isOccupied(ArrayList<Piece> pieces){
        
        //If the Position is occupied by any of the Pieces, return true
        for (Piece piece: pieces){
            if (this.samePosition(piece.getPosition())){
                return true;
            }
        }
        
        return false;
    }

    /**
     * Checks what Piece currently occupies a certain Position
     * @param pieces The set of Pieces that may occupy the Position 
     * @return The Piece that is on the space or null if none of the Pieces occupy the space
    */
    public Piece getPieceOnSpace(ArrayList<Piece> pieces){

        //If there is a Piece on the Position, return it
        for (int i = 0; i < pieces.size(); i++){
            if (this.samePosition(pieces.get(i).getPosition())){
                return pieces.get(i);
            }
        }

        return null;
    }
}
