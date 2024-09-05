import java.util.ArrayList;

//A class for a Rook on a chess board
public class Rook extends Piece{

    public Rook(char letter, int number, boolean white){
        super(letter, number, white);

        this.setName("Rook");

        this.setSymbol(white ? '♜' : '♖'); 
    }

    @Override
    public ArrayList<Position> getMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> possibleMoves = new ArrayList<Position>();

        //Up
        this.addDirectionalMoves(possibleMoves, 0, 1, samePieces, oppositePieces, sameKing);

        //Down
        this.addDirectionalMoves(possibleMoves, 0, -1, samePieces, oppositePieces, sameKing);

        //Right
        this.addDirectionalMoves(possibleMoves, 1, 0, samePieces, oppositePieces, sameKing);

        //Left
        this.addDirectionalMoves(possibleMoves, -1, 0, samePieces, oppositePieces, sameKing);

        return possibleMoves;
    }


    @Override
    public boolean hasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        
        if (this.hasDirectionalMoves(0, 1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(0, -1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(1, 0, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(-1, 0, samePieces, oppositePieces, sameKing)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){
        
        int rookX = this.getPosition().getX();
        int rookY = this.getPosition().getY();
        
        int kingX = oppositeKing.getPosition().getX();
        int kingY = oppositeKing.getPosition().getY();

        //If the Rook is not directly vertical or horizontal to the King, it cannot capture the King
        if (rookX != kingX && rookY != kingY){
            return false;
        }
        
        int xMod = 0;
        int yMod = 0;

        if (rookX == kingX){
            yMod = rookY < kingY ? 1 : -1;
        }
        else if (rookY == kingY){
            xMod = rookX < kingX ? 1 : -1;
        }

        return this.canCaptureKingInDirection(xMod, yMod, samePieces, oppositePieces, oppositeKing);
    }
}
