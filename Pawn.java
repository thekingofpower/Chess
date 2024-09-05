import java.util.ArrayList;
import java.util.Scanner;

//A class for a Pawn on a chess board
public class Pawn extends Piece{

    //Whether or not the Pawn is vulnerable to en passant from another Pawn
    private boolean passantVulnerable;

    //Scanner used to ask the user what they want to promote into
    static Scanner sc = new Scanner(System.in);

    public Pawn(char letter, int number, boolean white){
        super(letter, number, white);

        this.setName("Pawn");
        
        //By default, the Pawn is not vulnerable to en passant
        passantVulnerable = false;
    }

    boolean getPassantVulnerable(){
        return passantVulnerable;
    }

    void passantVulnerableTimeUp(){
        passantVulnerable = false;
    }

    @Override
    public ArrayList<Position> getMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> possibleMoves = new ArrayList<Position>();
        
        int pawnX = this.getPosition().getX();
        int pawnY = this.getPosition().getY();

        //The direction the Pawn moves in depends on the color of the Pawn
        int yMod = this.getWhite() ? -1 : 1;

        //If the space in front of the Pawn is not blocked by a white or black piece, it becomes an optional move
        if (this.canMakeMove(new Position(pawnX, pawnY + yMod), samePieces, oppositePieces, sameKing) && !(new Position(pawnX, pawnY + yMod).isOccupied(oppositePieces))){
            possibleMoves.add(new Position(pawnX, pawnY + yMod));
                
            //Knowing that the space immediately in front of the Pawn isn't blocked, check the space after that as well, but only if the Pawn hasn't moved yet
            if (!this.getMoved() && this.canMakeMove(new Position(pawnX, pawnY + 2 * yMod), samePieces, oppositePieces, sameKing) && !(new Position(pawnX, pawnY + 2 * yMod).isOccupied(oppositePieces))){
                possibleMoves.add(new Position(pawnX, pawnY + 2 * yMod));
            }
        }
                
        //If there's a piece diagonal to the Pawn in the direction the Pawn moves in, add that move so that it can capture the piece
        if (new Position(pawnX + 1, pawnY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pawnX + 1, pawnY + yMod), samePieces, oppositePieces, sameKing)){
            possibleMoves.add(new Position(pawnX + 1, pawnY + yMod));
        }
        if (new Position(pawnX - 1, pawnY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pawnX - 1, pawnY + yMod), samePieces, oppositePieces, sameKing)){
            possibleMoves.add(new Position(pawnX - 1, pawnY + yMod));
        }
    
        return possibleMoves;
    }

    @Override
    public ArrayList<Position> getSpecialMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        ArrayList<Position> specialMoves = new ArrayList<Position>();

        //The direction the Piece moves in depends on its color
        int yMod = this.getWhite() ? -1 : 1;

        //Check for en passant on the right then on the left
        if (this.canEnPassant(samePieces, oppositePieces, sameKing, 1, yMod)){
            specialMoves.add(new Position(this.getPosition().getX() + 1, this.getPosition().getY() + yMod));
        }
        if (this.canEnPassant(samePieces, oppositePieces, sameKing, -1, yMod)){
            specialMoves.add(new Position(this.getPosition().getX() - 1, this.getPosition().getY() + yMod));
        }

        return specialMoves;       
    }

    @Override
    public boolean hasMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){

        int pawnX = this.getPosition().getX();
        int pawnY = this.getPosition().getY();

        int yMod = this.getWhite() ? -1 : 1;

        //If the space in front is not blocked by a white or black Piece, it becomes an optional move
        if (this.canMakeMove(new Position(pawnX, pawnY + yMod), samePieces, oppositePieces, sameKing) && !(new Position(pawnX, pawnY + yMod).isOccupied(oppositePieces))){
            return true;
        }
                
        //If there's a piece diagonal to the Pawn in the direction the pawn moves in, add that move so that it can capture the Piece
        if (new Position(pawnX + 1, pawnY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pawnX + 1, pawnY + yMod), samePieces, oppositePieces, sameKing)){
            return true;
        }
        if (new Position(pawnX - 1, pawnY + yMod).isOccupied(oppositePieces) && this.canMakeMove(new Position(pawnX - 1, pawnY + yMod), samePieces, oppositePieces, sameKing)){
            return true;
        }
        
        return false;
    }

    @Override
    public boolean hasSpecialMoves(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing){
        
        //The direction the Piece moves in depends on its color
        int yMod = this.getWhite() ? -1 : 1;

        //Check for en passant on the right then on the left
        return this.canEnPassant(samePieces, oppositePieces, sameKing, 1, yMod) || this.canEnPassant(samePieces, oppositePieces, sameKing, -1, yMod);
    }

    @Override
    void move(Position newPosition, ArrayList<Piece> samePieces){

        //If the Pawn is about to move 2 spaces forward, set it to be vulnerable to en passant
        if (Math.abs(this.getPosition().getNumber() - newPosition.getNumber()) == 2){
            passantVulnerable = true;
        }

        super.move(newPosition, samePieces);
        
        //If the Pawn is at the edge of the board, promote it
        if (this.getPosition().getNumber() == 1 || this.getPosition().getNumber() == 8){
            if (this.getWhite()){
                this.promotePlayer(samePieces);
            }
            else{
                this.promoteBot(samePieces);
            }
        }
    }

    @Override
    public boolean specialMove(Position newPosition, ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, ArrayList<Piece> deadPieces){
        
        //Position of the captured Pawn depends on the color of the moving Pawn
        int yMod = this.getWhite() ? -1 : 1;

        Pawn capturedPawn = ((Pawn)(new Position(newPosition.getLetter(), newPosition.getNumber() + yMod).getPieceOnSpace(oppositePieces)));
        
        capturedPawn.setAlive(false);

        deadPieces.add(capturedPawn);

        oppositePieces.remove(capturedPawn);

        this.move(newPosition, samePieces);

        //En passant is guaranteed to capture a Piece
        return true;
    }

    @Override
    public boolean canCaptureKing(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King oppositeKing){
        
        int pawnX = this.getPosition().getX();
        int pawnY = this.getPosition().getY();
        
        int kingX = oppositeKing.getPosition().getX();
        int kingY = oppositeKing.getPosition().getY();

        int yMod = this.getWhite() ? -1 : 1;
         
        if (kingY == pawnY + yMod && Math.abs(kingX - pawnX) == 1){
            return true;
        }
                
        return false;
    }

    /**
     * Checks whether or not a Pawn can use en passant in a specific direction
     * @param samePieces The Pieces on the same team as the Pawn
     * @param oppositePieces The Pieces on the opposite team as the Pawn
     * @param sameKing The King on the same team as the Pawn
     * @param xMod The x direction to check en passant for (1 for right, -1 for left)
     * @param yMod The y direction to check en passant for (depends on the color of the Pawn)
     * @return true if the Pawn can use en passant in the direction and false otherwise
     */
    public boolean canEnPassant(ArrayList<Piece> samePieces, ArrayList<Piece> oppositePieces, King sameKing, int xMod, int yMod){

        //The Piece that would die as a result of the en passant
        Piece victim = new Position(this.getPosition().getX() + xMod, this.getPosition().getY()).getPieceOnSpace(oppositePieces);

        //First check if a potential victim exists, then check if the move would be legal
        if (victim != null && victim.getName().equals("Pawn") && ((Pawn)(victim)).passantVulnerable){
            if (this.canMakeMove(new Position(this.getPosition().getX() + xMod, this.getPosition().getY() + yMod), samePieces, oppositePieces, sameKing)){
                return true;
            }
        }
        return false;
    }

    /**
     * Promotes a Pawn into a more powerful Piece
     * @param samePieces The Pieces on the same team as the Pawn
     */
    void promotePlayer(ArrayList<Piece> samePieces){

        System.out.println("Congratulations! Your Pawn can promote. What kind of piece should your pawn turn into?");
        
        //Whether or not the player failed to pick a valid piece type to promote into
        boolean failed;

        //Store the promoting Piece before removing it (it has to be replaced by a newer, more powerful Piece)
        Piece promoter = this;
        samePieces.remove(this);
        
        do{
            //Assume the player does not fail
            failed = false;

            String ans = sc.nextLine();

            switch (ans.toLowerCase()){
                case "queen":
                    //If they pick Queen, add a new Queen to the board with the same position as the pawn
                    samePieces.add(new Queen(promoter.getPosition().getLetter(), promoter.getPosition().getNumber(), true));
                    break;
                case "bishop":
                    samePieces.add(new Bishop(promoter.getPosition().getLetter(), promoter.getPosition().getNumber(), true));
                    break;
                
                case "rook":
                    samePieces.add(new Rook(promoter.getPosition().getLetter(), promoter.getPosition().getNumber(), true));
                    break;
                
                case "knight":
                    samePieces.add(new Knight(promoter.getPosition().getLetter(), promoter.getPosition().getNumber(), true));
                    break;
                default:
                    failed = true;
                    System.out.println("You must pick one of the following: Queen, Bishop, Rook, or Knight.");
            }
        }
        while(failed);
    }

    /**
     * Promotes a Pawn into a Queen
     * @param samePieces The pieces on the same team as the Pawn
     */
    void promoteBot(ArrayList<Piece> samePieces){
        Piece promoter = this;
        samePieces.remove(this);
        //Bot always promotes to Queen
        samePieces.add(new Queen(promoter.getPosition().getLetter(), promoter.getPosition().getNumber(), false));
        System.out.println("The bot promoted!");
    }
}