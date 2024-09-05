import java.util.ArrayList;

//A class for a Knight on a chess board
public class Knight extends Piece{

    public Knight(char letter, int number, boolean white){
        super(letter, number, white);

        this.setName("Knight");

        this.setSymbol(white ? '♞' : '♘');
    }

    @Override
    public ArrayList<Position> getMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> possibleMoves = new ArrayList<Position>();
        
        int horseX = this.getPosition().getX();
        int horseY = this.getPosition().getY();

        for (int moveX = -2; moveX <= 2; moveX++){
            if (moveX == 0){
                continue;
            }
            for (int moveY = -2; moveY <= 2; moveY++){
                //Certain combinations must be skipped
                if (moveY == 0 || Math.abs(moveX) == Math.abs(moveY)){
                    continue;
                }
                if (this.canMakeMove(new Position(horseX + moveX, horseY + moveY), samePieces, oppositePieces, sameKing)){
                    possibleMoves.add(new Position(horseX + moveX, horseY + moveY));
                }
            }
        }

        return possibleMoves;
    }

    @Override
    public boolean hasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        
        int horseX = this.getPosition().getX();
        int horseY = this.getPosition().getY();

        for (int moveX = -2; moveX <= 2; moveX++){
            if (moveX == 0){
                continue;
            }
            for (int moveY = -2; moveY <= 2; moveY++){
                if (moveY == 0 || Math.abs(moveX) == Math.abs(moveY)){
                    continue;
                }
                if (this.canMakeMove(new Position(horseX + moveX, horseY + moveY), samePieces, oppositePieces, sameKing)){
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){
        
        int horseX = this.getPosition().getX();
        int horseY = this.getPosition().getY();
        
        int kingX = oppositeKing.getPosition().getX();
        int kingY = oppositeKing.getPosition().getY();

        for (int moveX = -2; moveX <= 2; moveX++){
            if (moveX == 0){
                continue;
            }
            for (int moveY = -2; moveY <= 2; moveY++){
                if (moveY == 0 || Math.abs(moveX) == Math.abs(moveY)){
                    continue;
                }
                if (horseX + moveX == kingX && horseY + moveY == kingY){
                    return true;
                }
            }
        }
        
        return false;
    }
}