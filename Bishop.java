import java.util.ArrayList;

//A class for a Bishop on a chess board
public class Bishop extends Piece{

    public Bishop(char letter, int number, boolean white){
        super(letter, number, white);

        this.setName("Bishop"); 

        this.setSymbol(white ? '♝' : '♗'); 
        
    }

    @Override
    public ArrayList<Position> getMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> possibleMoves = new ArrayList<>();

        //Top right
        this.addDirectionalMoves(possibleMoves, 1, 1, samePieces, oppositePieces, sameKing);

        //Bottom right
        this.addDirectionalMoves(possibleMoves, 1, -1, samePieces, oppositePieces, sameKing);

        //Top left
        this.addDirectionalMoves(possibleMoves, -1, 1, samePieces, oppositePieces, sameKing);

        //Bottom left
        this.addDirectionalMoves(possibleMoves, -1, -1, samePieces, oppositePieces, sameKing);

        return possibleMoves;
    }

    @Override
    public boolean hasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        return this.hasDirectionalMoves(1, 1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(1, -1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(-1, 1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(-1, -1, samePieces, oppositePieces, sameKing);
    }

     
    @Override
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){

        int bishopX = this.getPosition().getX();
        int bishopY = this.getPosition().getY();

        int kingX = oppositeKing.getPosition().getX();
        int kingY = oppositeKing.getPosition().getY();

        //If the King is not Diagonal to the Bishop, the Bishop can't capture the King
        if (Math.abs(bishopX - kingX) != Math.abs(bishopY - kingY)){
            return false;
        }
        
        int xMod = bishopX < kingX ? 1 : -1;
        int yMod = bishopY < kingY ? 1 : -1;

        return this.canCaptureKingInDirection(xMod, yMod, samePieces, oppositePieces, oppositeKing);
    }
}
