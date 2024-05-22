package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.PointPosition;
import boardifier.model.action.ActionList;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HoleDecider extends Decider {

    protected Player currentPlayer;

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public HoleDecider(Model model, Controller control) {
        super(model, control);
    }

    public HoleDecider(Model model, Controller control, Player currentPlayer) {
        super(model, control);
        this.currentPlayer = currentPlayer;
    }


    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        HoleStageModel stage = (HoleStageModel) model.getGameStage();
        HoleBoard board = stage.getBoard(); // get the board
        Pawn pawn; // the pawn that is moved
        int rowDest; // the dest. row in board
        int colDest; // the dest. col in board
        List<Pawn> pawns = stage.getPawns(currentPlayer);
        ActionList actions = new ActionList();


        if (currentPlayer.getComputerType() == 1) {
            //1ère IA (position stratégique et minimisation des risques) :
            //Initialisation :
            //Créer une liste de tous les mouvements valides pour le joueur actuel.
            ArrayList<ArrayList<PointPosition>> possibleMove = new ArrayList<ArrayList<PointPosition>>();
            for (Pawn p : pawns) {
                if (p.getRole() == Pawn.INFANTRYMAN) {
                    possibleMove.add(new ArrayList<PointPosition>());
                    if (analyseCorrectMove(p.getCol(), p.getRow(), p.getCol(), p.getRow() + 1)) {
                        possibleMove.get(possibleMove.size() - 1).add(new PointPosition(p.getCol(), p.getRow() + 1));
                    }
                } else {
                    int[][] temp;
                    if (stage.getBoardArrows1()[p.getRow()][p.getCol()] != null) {
                        temp = board.getValidCell(model, stage.getBoardArrows1()[p.getRow()][p.getCol()], stage.getBoardArrows2()[p.getRow()][p.getCol()], p.getRow(), p.getCol());
                    } else {
                        temp = board.getValidCell(model, p.getRow(), p.getCol());
                    }
                    for (int i = 0; i < temp.length; i++) {
                        possibleMove.get(possibleMove.size() - 1).add(new PointPosition(temp[i][1], temp[i][0]));
                    }

                }
            }

            //Initialiser une liste de positions stratégiques sur le plateau (par exemple, les cases centrales).
            PointPosition[] strategicPositions = new PointPosition[]{
                    new PointPosition(2, 2), new PointPosition(3, 2), new PointPosition(4, 2), new PointPosition(5, 2),
                    new PointPosition(2, 3), new PointPosition(3, 3), new PointPosition(4, 3), new PointPosition(5, 3),
                    new PointPosition(2, 4), new PointPosition(3, 4), new PointPosition(4, 4), new PointPosition(5, 4),
                    new PointPosition(2, 5), new PointPosition(3, 5), new PointPosition(4, 5), new PointPosition(5, 5)
            };


            // Évaluation des mouvements :
            //  ◦ Pour chaque mouvement valide, évaluer la position finale du pion.
            //  ◦ Attribuer un score à chaque mouvement basé sur plusieurs critères :
            //    ▪ Contrôle des cases centrales : Les mouvements qui placent un pion sur une case centrale obtiennent un score plus élevé.
            //    ▪ Proximité des pions adverses : Les mouvements qui rapprochent un pion d'un pion adverse obtiennent un score plus élevé, favorisant les futures opportunités de capture.
            //    ▪ Éloignement des bords : Les mouvements qui placent un pion loin des bords du plateau peuvent être favorisés pour éviter les pièges.
            //    ▪ Sécurité : Les mouvements qui placent un pion dans une position moins susceptible d'être capturé au tour suivant obtiennent un score plus élevé.

            //Sélection du mouvement :
            //◦ Choisir le mouvement avec le score le plus élevé parmi les mouvements évalués.
            //◦ Exécuter le mouvement sélectionné.


            //ActionList actions = ActionFactory.generatePutInContainer( model, pawn, "holeboard", rowDest, colDest);
            //actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        } else if (currentPlayer.getComputerType()==2){

        }
        return actions;
    }

    private boolean analyseCorrectMove(int colPawn, int rowPawn, int finCol, int finRow) {
        HoleStageModel gameStage = (HoleStageModel) model.getGameStage();
        HoleBoard board = gameStage.getBoard();

        // check coords validity
        if ((finRow < 0) || (finRow > 8) || (finCol < 0) || (finCol > 8)) return false;

        // check if the pawn is the good color
        int color;
        if (model.getIdPlayer() == 0) {
            color = Pawn.PAWN_BLUE;
        } else {
            color = Pawn.PAWN_RED;
        }

        Pawn p = (Pawn) board.getElement(rowPawn, colPawn);
        try {
            if (p.getColor() != color) return false;
        } catch (NullPointerException e) {
            System.out.println("Aucun pion n'est présent sur cette case");
            return false;
        }


        //Vérifier le type du pion
        if (p.getRole() == Pawn.INFANTRYMAN) {
            if (!verifPawnMove(board, color, colPawn, rowPawn, finRow, finCol)) return false;
        } else {
            if (!verifMoveCavalier(board, colPawn, rowPawn, finRow, finCol, gameStage)) return false;
        }

        return true;
    }

    private boolean verifPawnMove(HoleBoard board, int color, int colPawn, int rowPawn, int finRow, int finCol) {
        //Test mouvement possible en fonction de la couleur
        if ((color == Pawn.PAWN_BLUE && (colPawn != finCol || rowPawn + 1 != finRow)) || (color == Pawn.PAWN_RED && (colPawn != finCol || rowPawn - 1 != finRow))) {
            System.out.println("Un pion peut aller que tout droit");
            return false;

        }
        //Test pion devant le pion joueur
        if (board.getElement(finRow, finCol) != null) {
            System.out.println("Un pion se trouve devant ce pion");
            return false;
        }
        //Test mouvement impossible
        if (finCol > 0 && finCol < 7) {
            Pawn p1 = (Pawn) board.getElement(finRow, finCol - 1);
            Pawn p2 = (Pawn) board.getElement(finRow, finCol + 1);
            if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }

        return true;
    }

    protected boolean verifMoveCavalier(HoleBoard board, int colPawn, int rowPawn, int finRow, int finCol, HoleStageModel holeStageModelmodel) {
        int[][] temp;
        boolean valueFound = false;

        if (holeStageModelmodel.getBoardArrows1()[rowPawn][colPawn] != null) {
            temp = board.getValidCell(model, holeStageModelmodel.getBoardArrows1()[rowPawn][colPawn], holeStageModelmodel.getBoardArrows2()[rowPawn][colPawn], rowPawn, colPawn);
        } else {
            temp = board.getValidCell(model, rowPawn, colPawn);
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i][0] == finRow && temp[i][1] == finCol) {
                valueFound = true;
                break;
            }
        }
        if (!valueFound) {
            System.out.println("Mouvement impossible sur cette case");
            return false;
        }

        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i][0] + ", " + temp[i][1]);
        }
        return true;
    }
}
