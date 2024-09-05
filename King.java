import java.util.ArrayList;

//A class for a King on a chess board
public class King extends Piece{
    
    public King(char letter, int number, boolean white){
        super(letter, number, white);

        this.setName("King");

        this.setSymbol(white ? '♚' : '♔');
    }
   
    public ArrayList<Position> getMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> possibleMoves = new ArrayList<Position>();

        int kingX = this.getPosition().getX();
        int kingY = this.getPosition().getY();

        for (int moveX = -1; moveX <= 1; moveX++){
            for (int moveY = -1; moveY <= 1; moveY++){
                if (moveX == 0 && moveY == 0){
                    continue;
                }
                if (this.canMakeMove(new Position(kingX + moveX, kingY + moveY), samePieces, oppositePieces, sameKing)){
                    possibleMoves.add(new Position(kingX + moveX, kingY + moveY));
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean hasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        
        int kingX = this.getPosition().getX();
        int kingY = this.getPosition().getY();

        for (int moveX = -1; moveX <= 1; moveX++){
            for (int moveY = -1; moveY <= 1; moveY++){
                if (moveX == 0 && moveY == 0){
                    continue;
                }
                if (this.canMakeMove(new Position(kingX + moveX, kingY + moveY), samePieces, oppositePieces, sameKing)){
                    return true;
                }
            }
        }
   
        return false;
    }

    @Override
    public ArrayList<Position> getSpecialMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> specialMoves = new ArrayList<Position>();

        //In order for the King to castle, It must have not moved and must not be in check
        if (!this.getMoved() && !this.isChecked(samePieces, oppositePieces)){
            
            //Check if the King can castle on the right
            if (this.canCastle(samePieces, oppositePieces, 1)){
                specialMoves.add(new Position('H', this.getPosition().getNumber()));
            }
            //Check if the King can castle on the left
            if (this.canCastle(samePieces, oppositePieces, -1)){
                specialMoves.add(new Position('A', this.getPosition().getNumber()));
            }
        }

        return specialMoves;
    }

    @Override
    public boolean hasSpecialMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        
        //If the King can castle on the left or the right, return true
        if (!this.getMoved() && !this.isChecked(samePieces, oppositePieces)){
            return this.canCastle(samePieces, oppositePieces, 1) || this.canCastle(samePieces, oppositePieces, -1);
        }

        return false;                
    }
    
    @Override
    public boolean specialMove(Position newPosition, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, ArrayList<Piece> deadPieces){

        Rook castleRook = (Rook)newPosition.getPieceOnSpace(samePieces);

        //King and Rook movement depends on whether the King is to the left or the right of the Rook
        int kingModifier = this.getPosition().getLetter() < castleRook.getPosition().getLetter() ? 2 : -2;
        int rookModifier = this.getPosition().getLetter() < castleRook.getPosition().getLetter() ? -1 : 1;

        //Move the King and the Rook
        this.move(new Position(this.getPosition().getX() + kingModifier, this.getPosition().getY()), samePieces);
        castleRook.move(new Position(this.getPosition().getX() + rookModifier, this.getPosition().getY()), samePieces);
        
        //Castling does not capture any Pieces, so return false
        return false;
    }

    @Override
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){
        
        int oppositeKingX = this.getPosition().getX();
        int oppositeKingY = this.getPosition().getY();
        
        int kingX = oppositeKing.getPosition().getX();
        int kingY = oppositeKing.getPosition().getY();

        for (int moveX = -1; moveX <= 1; moveX++){
            for (int moveY = -1; moveY <= 1; moveY++){
                if (moveX == 0 && moveY == 0){
                    continue;
                }
                if (oppositeKingX + moveX == kingX && oppositeKingY + moveY == kingY){
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * Checks if the King can Castle in a certain direction
     * @param samePieces The Pieces on the same team as the King
     * @param oppositePieces The Pieces on the opposite team as the King
     * @param xMod The direction to check for castling (1 for right, -1 for left)
     * @return true if the King can castle in that direction and false otherwise
     */
    public boolean canCastle(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, int xMod){

        //SAME SPACE AS ROOK DOESNT MATTER IF CHECK? Change castling logic to account for this, if the space could be checked but its the rook

        //Look at every position between the King and the Rook to castle with
        for (int i = this.getPosition().getX() + xMod; i <= 7 && i >= 0; i += xMod){
            
            Position checkedPosition = new Position(i, this.getPosition().getY());

            if (checkedPosition.isOccupied(samePieces)){
                
                //If there is a Piece on the same team found, it must be a Rook that has not moved. Otherwise, it can't castle
                Piece occupier = checkedPosition.getPieceOnSpace(samePieces);

                if (occupier.getName().equals("Rook") && !occupier.getMoved()){
                    return true;
                }

                //If the space is occupied by a Piece that isn't a Rook that hasn't moved, the King can't castle
                return false;
            }
            else if (checkedPosition.isOccupied(oppositePieces) || this.wouldPutKingInCheck(checkedPosition, samePieces, oppositePieces, this)){
                //If an opposite Piece is blocking the way or the King would be in check if it was on one of the spaces, the King can't castle
                return false;
            }
        }
        return false;
    }

    /**
     * Determines whether the King is currently in check
     * @param sameAsKingPieces The Pieces on the same team as the King
     * @param notSameAsKingPieces The Pieces on the opposite team as the King
     * @return true if the King is in check and false otherwise
     */
    boolean isChecked(ArrayList<Piece> sameAsKingPieces, ArrayList<Piece> notSameAsKingPieces){

        //Go through every opposing Piece
        for (int i = 0; i < notSameAsKingPieces.size(); i++){
            //Check if the opposing Piece can capture the King and return true if they can
            if (notSameAsKingPieces.get(i).canCaptureKing(notSameAsKingPieces, sameAsKingPieces, this)){
                return true;
            }
        }

        //If none of the opposing Pieces can capture the King, return false
        return false;
    }
}