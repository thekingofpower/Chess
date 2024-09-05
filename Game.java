import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//A class for a game of chess
public class Game {
    
    //Used for user input
    static Scanner sc = new Scanner(System.in);

    //Used for bot input
    static Random rand = new Random();

    //The Board that stores piece positions
    static Board gameBoard = new Board();

    //The number of turns that have gone by with no captures (A turn meaning a white move plus a black move)
    static int numTurnsWithNoCaptures = 0;

    //Contains all board layouts that have shown up once in a game
    static ArrayList<String> layoutsOnce = new ArrayList<String>();

    //Contains all board layouts that have shown up twice in a game
    static ArrayList<String> layoutsTwice = new ArrayList<String>();

    //chcp 65001 (used to get unicode chess pieces to work)
    public static void main(String[] args){

        //Whether or not the game has ended
        boolean gameActive = true;

        //Draw the Board before the game starts
        gameBoard.drawBoard();

        while (gameActive){

            //Make the white turn and record whether or not the white team captured a Piece
            boolean didWhiteCapture = whiteTurn();

            //Now that the white team has moved, draw the board again
            gameBoard.drawBoard();

            //Check if the game should end and if it should, break out of the loop
            if (gameOver(true, false)){
                gameActive = false;
                break;
            }

            System.out.println("Bot's Turn...");

            //Wait a few seconds so the user can look at the updated board
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Make the black turn and record whether or not the black team captured a Piece
            boolean didBlackCapture = blackTurn();

            gameBoard.drawBoard();

            //Check if the game should end 
            if (gameOver(false, didBlackCapture || didWhiteCapture)){
                gameActive = false;
            }
        }
    }

    /**
     * Executes the white team's turn
     * @return true if the white team ends up capturing an opposing Piece and false otherwise
     */
    public static boolean whiteTurn(){
        
        //Get the user to select the piece they will move
        Piece whiteMover = selectMoverPlayer();

        //Get a list of the potential moves for that piece
        ArrayList<Position> whiteMoves = whiteMover.getMoves(gameBoard.whitePieces, gameBoard.blackPieces, gameBoard.whiteKing);
        ArrayList<Position> whiteSpecialMoves = whiteMover.getSpecialMoves(gameBoard.whitePieces, gameBoard.blackPieces, gameBoard.whiteKing);

        //Draw the board with all potential moves identified to make it easier for the user to choose
        gameBoard.drawBoardWithMoves(whiteMoves, whiteSpecialMoves);
        
        //Get the user to select the move they want to make
        Position whiteMove = askForMove(whiteMoves, whiteSpecialMoves);
        
        //Assume the move does not capture a Piece until proven otherwise
        boolean didWhiteCapture = false;

        //Special moves are moves that don't obey the traditional rules of chess moves. They require a separate method
        if (!whiteMove.getSpecial()){
            //Make the move for the white team. If it ends up capturing another Piece, update the didWhiteCapture variable
            whiteMover.move(whiteMove, gameBoard.whitePieces);  

            Piece capturedPiece = whiteMover.getPosition().getPieceOnSpace(gameBoard.blackPieces);

            if (capturedPiece != null){
                capturedPiece.setAlive(false);

                gameBoard.deadPieces.add(capturedPiece);

                gameBoard.blackPieces.remove(capturedPiece);

                didWhiteCapture = true;
            }
        }
        else{
            //Special moves handle captures differently, so the specialMove method directly returns whether or not the move captured
            didWhiteCapture = whiteMover.specialMove(whiteMove, gameBoard.whitePieces, gameBoard.blackPieces, gameBoard.deadPieces);
        }

        //Any black Pieces that were previously vulnerable to en passant should be updated to no longer be vulnerable
        updatePassants(gameBoard.blackPieces);

        return didWhiteCapture;
    }

    /**
     * Executes the black team's turn
     * @return true if the black team ends up capturing an opposing Piece and false otherwise
     */
    public static boolean blackTurn(){
        
        //A list of all the pieces that have at least one legal move
        ArrayList<Piece> possibleMovers = new ArrayList<Piece>();

        //Check every black piece to see if it has at least one possible move, and if it does, add it to the ArrayList
        for (int i = 0; i < gameBoard.blackPieces.size(); i++){
            if (gameBoard.blackPieces.get(i).hasMoves(gameBoard.blackPieces, gameBoard.whitePieces, gameBoard.blackKing) || gameBoard.blackPieces.get(i).hasSpecialMoves(gameBoard.blackPieces, gameBoard.whitePieces, gameBoard.blackKing)){
                possibleMovers.add(gameBoard.blackPieces.get(i));
            }
        }
        
        //Randomly select a black Piece to move
        Piece blackMover = possibleMovers.get(rand.nextInt(possibleMovers.size()));  

        ArrayList<Position> blackMoves = blackMover.getMoves(gameBoard.blackPieces, gameBoard.whitePieces, gameBoard.blackKing);
        ArrayList<Position> blackSpecialMoves = blackMover.getSpecialMoves(gameBoard.blackPieces, gameBoard.whitePieces, gameBoard.blackKing);

        //The move the black team will play, to be determined later
        Position blackMove;

        boolean didBlackCapture = false;

        //If the black mover has a special move, it will always use a special move
        if (blackSpecialMoves.size() == 0){

            //Pick the move randomly
            blackMove = blackMoves.get(rand.nextInt(blackMoves.size()));

            blackMover.move(blackMove, gameBoard.blackPieces);

            Piece capturedPiece = blackMover.getPosition().getPieceOnSpace(gameBoard.whitePieces);

            if (capturedPiece != null){
                capturedPiece.setAlive(false);

                gameBoard.deadPieces.add(capturedPiece);

                gameBoard.whitePieces.remove(capturedPiece);

                didBlackCapture = true;
            }
        }
        else{

            blackMove = blackSpecialMoves.get(rand.nextInt(blackSpecialMoves.size()));

            didBlackCapture = blackMover.specialMove(blackMove, gameBoard.blackPieces, gameBoard.whitePieces, gameBoard.deadPieces);
        }

        //Any white pieces that were previously vulnerable to en passant should no longer be vulnerable
        updatePassants(gameBoard.whitePieces);

        return didBlackCapture;
    }

    /**
     * Asks the player to enter the position of the Piece they want to move
     * @return The Piece the player is going to move
    */
    public static Piece selectMoverPlayer(){
        System.out.println("What's the position of the piece you want to move?");
        
        String answer = sc.nextLine();

        Position moverPosition = new Position(Character.toUpperCase(answer.charAt(0)), Character.getNumericValue(answer.charAt(1)));

        for (int i = 0; i < gameBoard.whitePieces.size(); i++){
            if (gameBoard.whitePieces.get(i).getPosition().samePosition(moverPosition)){
                if (gameBoard.whitePieces.get(i).hasMoves(gameBoard.whitePieces, gameBoard.blackPieces, gameBoard.whiteKing) || gameBoard.whitePieces.get(i).hasSpecialMoves(gameBoard.whitePieces, gameBoard.blackPieces, gameBoard.blackKing)){
                    //If the Piece on the space the user selected has at least one valid move, return the Piece
                    return gameBoard.whitePieces.get(i);
                }
                else{
                    //If the Piece on the space the user selected doesn't have any legal moves, get them to try again
                    System.out.println("That piece doesn't have any legal moves. Try again.");   
                    return selectMoverPlayer();
                }
            }
            
        }
        
        //If the user entered an invalid Position (A blank space, an enemy Piece, or a Position not on the board), get them to try again
        System.out.println("That's not one of your pieces.");
        return selectMoverPlayer();
    }

    /**
     * Ask the user what move they want to make for the Piece they previously selected
     * @param possibleMoves The list of possible moves for the Piece the user previously selected.
     * @param specialMoves The list of special moves for the Piece the user previously selected.
     * @return The Position of the move the user selected (which must be legal)
    */
    public static Position askForMove(ArrayList<Position> possibleMoves, ArrayList<Position> specialMoves){

        System.out.print("What move do you want to make? ");

        String answer = sc.nextLine();

        //The first character of the user's response dictates the letter (x component) of the move
        char letter = Character.toUpperCase(answer.charAt(0));

        //The second character of the user's response dictates the number (y component) of the move
        int number = Character.getNumericValue(answer.charAt(1));

        Position selectedMove = new Position(letter, number);

        //Check if the move they entered is a possible move
        for (int i = 0; i < possibleMoves.size(); i++){
            if (selectedMove.samePosition(possibleMoves.get(i))){
                return new Position(letter, number, false);
            }
        } 

        //Check if the move is a possible special move
        for (int i = 0; i < specialMoves.size(); i++){
            if (selectedMove.samePosition(specialMoves.get(i))){
                return new Position(letter, number, true);
            }
        }   
        
        //If they didn't enter a valid move, ask them to try again
        System.out.println("That move is invalid. Try again.");
        return askForMove(possibleMoves, specialMoves);
    }

    /**
     * Determines if the game is over 
     * @param whiteJustMoved Whether or not the white team just moved
     * @param captured Whether or not either the white and the black team have captured (only applicable after a full turn)
     * @return true if the game is over and false otherwise
     */
    public static boolean gameOver(boolean whiteJustMoved, boolean captured){

        if (checkCheckmateOrStalemate(whiteJustMoved)){
            return true;
        }

        //If 50 turns go by with no captures, the game has to end. This check should only be carried out after a black turn because black moves second
        if (!whiteJustMoved){
            if (check50Turns(captured)){
                System.out.println("50 turns with no captures! Draw!");
                return true;
            }
        }
        
        //If the same board layout shows up 3 times in a game, the game ends in a draw
        if (checkPreviousLayouts(gameBoard.toString())){
            System.out.println("Same position 3 times! Draw!");
            return true;
        }

        //Some combinations of pieces result in it being impossible to get a checkmate for either team
        if (checkInsufficientMaterial(gameBoard.whitePieces) && checkInsufficientMaterial(gameBoard.blackPieces)){
            System.out.println("Insufficient material! Draw!");
            return true;
        }

        //If none of the conditions for the game ending are met, return false
        return false;
    }

    /**
     * Checks whether the game should end in checkmate or stalemate
     * @param whiteJustMoved Whether the white team just moved
     * @return true if the game should end in checkmate or stalemate and false otherwise
     */
    public static boolean checkCheckmateOrStalemate(boolean whiteJustMoved){

        //These depend on the team that just moved
        ArrayList<Piece> samePieces = whiteJustMoved ?  gameBoard.blackPieces: gameBoard.whitePieces;
        ArrayList<Piece> oppositePieces = whiteJustMoved ? gameBoard.whitePieces: gameBoard.blackPieces;
        King sameKing = whiteJustMoved ? gameBoard.blackKing : gameBoard.whiteKing;

        //Whether or not the opposing team has any moves. Assumed to be false until proven otherwise
        boolean hasMoves = false;
        
        //Go through all of the opposite team's pieces and see if any of them has at least one move
        for (int i = 0; i < samePieces.size(); i++){
            if (samePieces.get(i).hasMoves(samePieces, oppositePieces, sameKing)){
                hasMoves = true;
                break;
            }
        }

        //If the opposite team has at least one legal move, the game can continue. Otherwise the game ends in checkmate or stalemate
        if (!hasMoves){

            //If the opposite king is in check, the game ends in checkmate. Otherwise, it ends in stalemate
            if (sameKing.isChecked(samePieces, oppositePieces)){
                System.out.println("Checkmate!");
                System.out.println(whiteJustMoved ? "White wins!" : "Black wins!");
            }
            else{
                System.out.println("Stalemate! Draw!");
            }
            return true;
        }

        return false;
    }

    /**
     * Updates the counter for the number of turns that have gone by with no captures and checks if 50 turns have gone by with no captures
     * @param captured Whether or not a Piece was captured on the previous turn
     * @return true if 50 turns have gone by without a capture and false otherwise
     */
    public static boolean check50Turns(boolean captured){

        if (captured){
            //Reset the counter
            numTurnsWithNoCaptures = 0;

            //If a capture happened, it is impossible for a previous board layout to be repeated, so all records of previous positions are cleared
            clearPreviousPositions();
        }
        else{
            //Increase the counter
            numTurnsWithNoCaptures++;

            //If at least 50 turns have gone by with no captures, the game ends in a draw
            if (numTurnsWithNoCaptures >= 50){
                return true;
            }
        }

        return false;
    }

    /**
     * Updates the previous layouts of the Board and checks whether or not any layout has shown up 3 times in one game
     * @param layout A String representing the current layout of the Board
     * @return true if any layouts have shown up 3 or more times and false otherwise
     */
    public static boolean checkPreviousLayouts(String layout){

        //If the layout is in the list of layouts that have shown up twice, return true
        for (int i = 0; i < layoutsTwice.size(); i++){
            if (layout.equals(layoutsTwice.get(i))){
                return true;
            }
        }

        //If the layout is in the list of layouts that have shown up once, add it to the list of layouts that have shown up twice
        for (int i = 0; i < layoutsOnce.size(); i++){
            if (layout.equals(layoutsOnce.get(i))){
                layoutsTwice.add(layoutsOnce.get(i));
                layoutsOnce.remove(i);
                return false;
            }
        }

        //If the layout has not shown up before, add it to the list of layouts that have shown up once
        layoutsOnce.add(layout);
        return false;
    }

    /**
     * Clears the lists storing previous board layouts
     */
    public static void clearPreviousPositions(){
        layoutsOnce.clear();
        layoutsTwice.clear();
    }

    /**
     * Check if either team has sufficient material to get a checkmate
     * @param pieces the list of pieces to check (black or white)
     * @return true if the team can get a checkmate and false otherwise
    */
    public static boolean checkInsufficientMaterial(ArrayList<Piece> pieces){

        //If a team has one bishop and one horse, that is not enough. But if they have 2 bishops, 2 horses, or one of each, that is enough
        int numBishopsAndHorses = 0;

        //Go through all of the pieces
        for (Piece piece: pieces){

            //Update the number of bishops and horses
            if (piece.getName().equals("Horse") || piece.getName().equals("Bishop")){
                numBishopsAndHorses++;
            }

            //If the white team has at least one pawn, queen, or rook, or at least 2 bishops and horses, they have sufficient material
            if (piece.getName().equals("Pawn") || piece.getName().equals("Queen") || piece.getName().equals("Rook") || numBishopsAndHorses >= 2){
                return false;
            }
        }

        //If the team does not have sufficient material to get a checkmate, return true
        return true;
    }

    /**
     * Update all Pawns on a certain team to no longer be vulnerable to en passant
     * @param pieces The Pieces to upddate
     */
    public static void updatePassants(ArrayList<Piece> pieces){

        for (int i = 0; i < pieces.size(); i++){
            if (pieces.get(i).getName().equals("Pawn")){
                ((Pawn)(pieces.get(i))).passantVulnerableTimeUp();
            }
        }
    }
}