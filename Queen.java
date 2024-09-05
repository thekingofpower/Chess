import java.util.ArrayList;

//A class for a Queen on a chess board
public class Queen extends Piece{

    public Queen(char letter, int number, boolean white){
        super(letter, number, white);

        this.setSymbol(white ? '♛' : '♕'); 
        
        this.setName("Queen");
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
        
        //Check if it can move vertically or horizontally, then check if it can move diagonally
        if (this.hasDirectionalMoves(0, 1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(0, -1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(1, 0, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(-1, 0, samePieces, oppositePieces, sameKing)){
            return true;
        }
        else if (this.hasDirectionalMoves(1, 1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(1, -1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(-1, 1, samePieces, oppositePieces, sameKing) || this.hasDirectionalMoves(-1, -1, samePieces, oppositePieces, sameKing)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){
        
        int queenX = this.getPosition().getX();
        int queenY = this.getPosition().getY();

        int kingX = oppositeKing.getPosition().getX();
        int kingY = oppositeKing.getPosition().getY();
        
        int xMod = 0;
        int yMod = 0;
    
        //If the Queen is not directly vertical, horizontal, or diagonal to the King, it cannot capture the King
        if (queenX != kingX && queenY != kingY && Math.abs(queenX - kingX) != Math.abs(queenY - kingY)){
            return false;
        }
        else if (queenX == kingX){
            yMod = queenY < kingY ? 1 : -1;
        }
        else if (queenY == kingY){
            xMod = queenX < kingX ? 1 : -1;
        }
        else{
            xMod = queenX < kingX ? 1 : -1;
            yMod = queenY < kingY ? 1 : -1;
        }
    
        return this.canCaptureKingInDirection(xMod, yMod, samePieces, oppositePieces, oppositeKing);
    }
}   


