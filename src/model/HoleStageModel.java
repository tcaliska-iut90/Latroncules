package model;

import boardifier.control.Logger;
import boardifier.model.*;


/**
 * HoleStageModel defines the model for the single stage in "The Hole". Indeed,
 * there are no levels in this game: a party starts and when it's done, the game is also done.
 *
 * HoleStageModel must define all that is needed to manage a party : state variables and game elements.
 * In the present case, there are only 2 state variables that represent the number of pawns to play by each player.
 * It is used to detect the end of the party.
 * For game elements, it depends on what is chosen as a final UI design. For that demo, there are 12 elements used
 * to represent the state : the main board, 2 pots, 8 pawns, and a text for current player.
 *
 * WARNING ! HoleStageModel DOES NOT create itself the game elements because it would prevent the possibility to mock
 * game element classes for unit testing purposes. This is why HoleStageModel just defines the game elements and the methods
 * to set this elements.
 * The instanciation of the elements is done by the HoleStageFactory, which uses the provided setters.
 *
 * HoleStageModel must also contain methods to check/modify the game state when given events occur. This is the role of
 * setupCallbacks() method that defines a callback function that must be called when a pawn is put in a container.
 * This is done by calling onPutInContainer() method, with the callback function as a parameter. After that call, boardifier
 * will be able to call the callback function automatically when a pawn is put in a container.
 * NB1: callback functions MUST BE defined with a lambda expression (i.e. an arrow function).
 * NB2:  there are other methods to defines callbacks for other events (see onXXX methods in GameStageModel)
 * In "The Hole", everytime a pawn is put in the main board, we have to check if the party is ended and in this case, who is the winner.
 * This is the role of computePartyResult(), which is called by the callback function if there is no more pawn to play.
 *
 */
public class HoleStageModel extends GameStageModel {

    public final static int STATE_SELECTPAWN = 1; // the player must select a pawn
    public final static int STATE_SELECTDEST = 2; // the player must select a destination

    // define stage game elements
    private HoleBoard board;
    private Arrow[][] boardArrows1;
    private Arrow[][] boardArrows2;
    private Pawn[] bluePawns;
    private Pawn[] redPawns;
    private HolePawnPot HoleRedPawnPot;
    private HolePawnPot HoleBluePawnPot;
    private Arrow[] arrows;
    private TextElement playerName;

    public static int Equality = 3;


    public HoleStageModel(String name, Model model) {
        super(name, model);
        state = STATE_SELECTPAWN;
        setupCallbacks();
    }

    //vérification de s'il reste des pions sur le plateau
    public boolean isBlueMissing(){
        for (Pawn bluePawn : bluePawns) {
            //System.out.println("Blue pawn: " + bluePawn);
            if (bluePawn != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isRedMissing(){
        for (Pawn redPawn : redPawns) {
            //System.out.println("Red pawn : " + redPawn);
            if (redPawn != null) {
                return false;
            }
        }
        return true;
    }

    public HoleBoard getBoard() {
        return board;
    }
    public void setBoard(HoleBoard board) {
        this.board = board;
        addContainer(board);
    }

    public void setBoardArrows1(Arrow[][] a){
        this.boardArrows1 = a;
    }

    public Arrow[][] getBoardArrows1() {return boardArrows1;}

    public void setBoardArrows2(Arrow[][] a){
        this.boardArrows2 = a;
    }

    public Arrow[][] getBoardArrows2() {return boardArrows2;}

    public Pawn[] getBluePawns() {
        return bluePawns;
    }
    public void setBluePawns(Pawn[] bluePawns) {
        this.bluePawns = bluePawns;
        for(int i=0;i<bluePawns.length;i++) {
            addElement(bluePawns[i]);
        }
    }

    public Pawn[] getRedPawns() {
        return redPawns;
    }
    public void setRedPawns(Pawn[] redPawns) {
        this.redPawns = redPawns;
        for(int i=0;i<redPawns.length;i++) {
            addElement(redPawns[i]);
        }
    }

    /*
    Ajoute le pion passé en paramètre dans le tableau de pion capturé par l'équipe bleue
     */
    public void addBluePawnsTaking(Pawn p){
        for (int i = 0; i < HoleRedPawnPot.getNbRows(); i++) {
            for (int j = 0; j < HoleRedPawnPot.getNbCols(); j++) {
                if (HoleRedPawnPot.getElement(i, j) == null){
                    HoleRedPawnPot.addElement(p, i, j);
                    return;
                }
            }
        }
    }
    /*
    Permet de retirer le pion passé en paramètre du tableau des pions bleue
     */
    public void removeBluePawns(Pawn p){
        for (int i = 0; i < bluePawns.length; i++) {
            if (bluePawns[i].equals(p)) {
                bluePawns[i] = null;
            }
        }
    }

    public void setHoleBluePawnPot(HolePawnPot holeBluePawnPot) {
        HoleBluePawnPot = holeBluePawnPot;
        addContainer(HoleBluePawnPot);
    }

    public HolePawnPot getHoleBluePawnPot() {
        return HoleBluePawnPot;
    }

    public void setHoleRedPawnPot(HolePawnPot holeRedPawnPot) {
        HoleRedPawnPot = holeRedPawnPot;
        addContainer(HoleRedPawnPot);
    }

    public HolePawnPot getHoleRedPawnPot() {
        return HoleRedPawnPot;
    }

    /*
     Ajoute le pion passé en paramètre dans le tableau de pion capturé par l'équipe rouge
    */
    public void addRedPawnsTaking(Pawn p){
        for (int i = 0; i < HoleBluePawnPot.getNbRows(); i++) {
            for (int j = 0; j < HoleBluePawnPot.getNbCols(); j++) {
                if (HoleBluePawnPot.getElement(i, j) == null){
                    HoleBluePawnPot.addElement(p, i, j);
                    /*
                    System.out.println("HoleBluePawnPot");
                    for (int k = 0; k < HoleBluePawnPot.getNbRows(); k++) {
                        for (int a = 0; a < HoleBluePawnPot.getNbCols(); a++) {
                            if ((HoleBluePawnPot.getElement(k, a) != null)) System.out.println(HoleBluePawnPot.getElement(k, a) + "i: " + k + ", j: " + a);
                        }
                    }
                    System.out.println("bluePawns");
                    for (int a = 0; a< bluePawns.length; a++) {
                        System.out.println(bluePawns[a]);
                    }

                     */
                    return;
                }
            }
        }




    }

    /*
    Permet de retirer le pion passé en paramètre du tableau des pions rouges
     */
    public void removeRedPawns(Pawn p){
        for (int i = 0; i < redPawns.length; i++) {
            if (redPawns[i].equals(p)) {
                redPawns[i] = null;
            }
        }
    }

    public TextElement getPlayerName() {
        return playerName;
    }
    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
        addElement(playerName);
    }

    public Arrow[] getArrows() {return arrows;}
    public void setArrows(Arrow[] arrows) {
        this.arrows = arrows;
        for (int i = 0; i < arrows.length; i++) {
            addElement(arrows[i]);
        }
    }


    private void setupCallbacks() {

        //System.out.println((holeController.verifPawnMove(board, color, colPawn, rowPawn, finRow, finCol));
        onPutInContainer( (element, gridDest, rowDest, colDest) -> {
            if(isBlueMissing()) {
                if (model.getPlayers().get(0).equals("PlayerRed")){
                    computePartyResult(0);
                }
                else{
                    computePartyResult(1);
                }
            } else if (isRedMissing()) {
                if (model.getPlayers().get(0).equals("PlayerBlue")){
                    computePartyResult(0);
                }
                else{
                    computePartyResult(1);
                }
            }
        });



    }

    public void computePartyResult(int idWinner) {
        if (idWinner == HoleStageModel.Equality)model.stopGame();
        else {
            // set the winner
            model.setIdWinner(idWinner);
            // stop the game
            model.stopGame();
        }

    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new HoleStageFactory(this);
    }
}
