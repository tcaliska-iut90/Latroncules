package model;

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


    public HoleStageModel(String name, Model model) {
        super(name, model);
    }


    public boolean isBlueMissing(){
        for (Pawn bluePawn : bluePawns) {
            if (bluePawn != null) {
                return false;
            }
        }
        return true;
    }

    public boolean isRedMissing(){
        for (Pawn redPawn : redPawns) {
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
        for (int i = 0; i < HoleRedPawnPot.getNbCols(); i++) {
            if (HoleRedPawnPot.getElement(0, i) == null){
                HoleRedPawnPot.addElement(p, 0, i);
                break;
            }
        }
    }
    /*
    Permet de retirer le pion passé en paramètre du tableau des pions bleue
     */
    public void removeBluePawns(Pawn p){
        for (int i = 0; i < bluePawns.length; i++) {
            if (bluePawns[i] == p) {
                bluePawns[i] = null;
                p.removeFromStage();
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
        for (int i = 0; i < HoleBluePawnPot.getNbCols(); i++) {
            if (HoleBluePawnPot.getElement(0, i)== null){
                HoleBluePawnPot.addElement(p, 0, i);
                break;
            }
        }
    }
    /*
    Permet de retirer le pion passé en paramètre du tableau des pions rouges
     */
    public void removeRedPawns(Pawn p){
        for (int i = 0; i < redPawns.length; i++) {
            if (redPawns[i] == p) {
                redPawns[i] = null;
                p.removeFromStage();
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
        onPutInContainer( (element, gridDest, rowDest, colDest) -> {
           /* // just check when pawns are put in 3x3 board
            if (gridDest != board) return;
            Pawn p = (Pawn) element;
            if (p.getColor() == 0) {
                bluePawnsToPlay--;
            }
            else {
                redPawnsToPlay--;
            }
            if ((bluePawnsToPlay == 0) && (redPawnsToPlay == 0)) {
                computePartyResult();
            }*/
        });
    }

    private void computePartyResult() {
        /*
        int idWinner = -1;
        // get the empty cell, which should be in 2D at [0,0], [0,2], [1,1], [2,0] or [2,2]
        // i.e. or in 1D at index 0, 2, 4, 6 or 8
        int i = 0;
        int nbBlack = 0;
        int nbRed = 0;
        int countBlack = 0;
        int countRed = 0;
        Pawn p = null;
        int row, col;
        for (i = 0; i < 64; i+=2) {
            if (board.isEmptyAt(i / 8, i % 8)) break;
        }
        // get the 4 adjacent cells (if they exist) starting by the upper one
        row = (i / 8) - 1;
        col = i % 8;
        for (int j = 0; j < 4; j++) {
            // skip invalid cells
            if ((row >= 0) && (row <= 2) && (col >= 0) && (col <= 2)) {
                p = (Pawn) (board.getElement(row, col));
                if (p.getColor() == Pawn.PAWN_BLUE) {
                    nbBlack++;
                    countBlack += p.getRole();
                } else {
                    nbRed++;
                    countRed += p.getRole();
                }
            }
            // change row & col to set them at the correct value for the next iteration
            if ((j==0) || (j==2)) {
                row++;
                col--;
            }
            else if (j==1) {
                col += 2;
            }
        }

        // decide whose winning
        if (nbBlack < nbRed) {
            idWinner = 0;
        }
        else if (nbBlack > nbRed) {
            idWinner = 1;
        }
        else {
            if (countBlack < countRed) {
                idWinner = 0;
            }
            else if (countBlack > countRed) {
                idWinner = 1;
            }
        }
        System.out.println("nb black: "+nbBlack+", nb red: "+nbRed+", count black: "+countBlack+", count red: "+countRed+", winner is player "+idWinner);
        // set the winner
        model.setIdWinner(idWinner);
        // stop de the game
        model.stopStage();
        */
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new HoleStageFactory(this);
    }
}
