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
    protected Player adversary;
    protected int colDest;
    protected int rowDest;
    protected Pawn pawn;

    protected HoleController holeController;

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public HoleDecider(Model model, Controller control) {
        super(model, control);
    }

    public HoleDecider(Model model, Controller control, Player currentPlayer, Player adversary, HoleController holeController) {
        super(model, control);
        this.currentPlayer = currentPlayer;
        this.adversary = adversary;
        this.holeController = holeController;
    }


    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        HoleStageModel stage = (HoleStageModel) model.getGameStage();
        HoleBoard board = stage.getBoard(); // get the board
        int color;
        if (model.getIdPlayer() == 0) {
            color = Pawn.PAWN_BLUE;
        } else {
            color = Pawn.PAWN_RED;
        }

        List<Pawn> pawns = stage.getPawns(currentPlayer);
        List<Pawn> pawns_adverse = stage.getPawns(adversary);
        ActionList actions = new ActionList();


        if (currentPlayer.getComputerType() == 1) {
            //1ère IA (position stratégique et minimisation des risques):
            //Initialisation :
            //Créer une liste de tous les mouvements valides pour le joueur actuel.
            ArrayList<ArrayList<PointPosition>> possibleMove = new ArrayList<ArrayList<PointPosition>>();

            for (Pawn p : pawns) {
                if (p.getRole() == Pawn.INFANTRYMAN) {
                    possibleMove.add(new ArrayList<PointPosition>());
                    if (currentPlayer.getName().equals("PlayerBlue") || currentPlayer.getName().equals("computer1")) {
                        //Test mouvement possible en fonction du joueur
                        if (analyseCorrectMove(p.getCol(), p.getRow(), p.getCol(), p.getRow() + 1)) {
                            possibleMove.get(possibleMove.size() - 1).add(new PointPosition(p.getRow() + 1, p.getCol()));
                        }
                    } else {
                        //Test mouvement possible en fonction du joueur
                        if (analyseCorrectMove(p.getCol(), p.getRow(), p.getCol(), p.getRow() - 1)) {
                            possibleMove.get(possibleMove.size() - 1).add(new PointPosition(p.getRow() - 1, p.getCol()));
                        }
                    }
                } else if (p.getRole() == Pawn.HORSEMAN){
                    possibleMove.add(new ArrayList<PointPosition>());
                    int[][] temp;
                    if (stage.getBoardArrows1()[p.getRow()][p.getCol()] != null || stage.getBoardArrows2()[p.getRow()][p.getCol()] != null){
                        temp = board.getValidCell(model, stage.getBoardArrows1()[p.getRow()][p.getCol()], stage.getBoardArrows2()[p.getRow()][p.getCol()], p.getRow(), p.getCol());
                    } else {
                        temp = board.getValidCell(model, p.getRow(), p.getCol());
                    }
                    for (int i = 0; i < temp.length; i++) {
                        if (analyseCorrectMove(p.getCol(), p.getRow(), temp[i][1], temp[i][0])) {
                            possibleMove.get(possibleMove.size() - 1).add(new PointPosition(temp[i][0], temp[i][1]));
                        }
                    }
                }
            }

            //Initialiser une liste de positions stratégiques sur le plateau (par exemple, les cases centrales).
            PointPosition[] strategicPositions = new PointPosition[]{
                    new PointPosition(2, 2), new PointPosition(3, 2), new PointPosition(4, 2), new PointPosition(5, 2),
                    new PointPosition(2, 3), new PointPosition(5, 3),
                    new PointPosition(2, 4), new PointPosition(5, 4),
                    new PointPosition(2, 5), new PointPosition(3, 5), new PointPosition(4, 5), new PointPosition(5, 5)
            };

            PointPosition[] strategicPositionsPlus = new PointPosition[] {
                    new PointPosition(3, 3), new PointPosition(4, 3), new PointPosition(3, 4), new PointPosition(4, 4)
            };


            // Évaluation des mouvements :
            //  ◦ Pour chaque mouvement valide, évaluer la position finale du pion.
            //  ◦ Attribuer un score à chaque mouvement basé sur plusieurs critères :
            //    ▪ Contrôle des cases centrales : Les mouvements qui placent un pion sur une case centrale obtiennent un score plus élevé.
            //    ▪ Proximité des pions adverses : Les mouvements qui rapprochent un pion d'un pion adverse obtiennent un score plus élevé, favorisant les futures opportunités de capture.
            //    ▪ Éloignement des bords : Les mouvements qui placent un pion loin des bords du plateau peuvent être favorisés pour éviter les pièges.
            //    ▪ Sécurité : Les mouvements qui placent un pion dans une position moins susceptible d'être capturé au tour suivant obtiennent un score plus élevé.
            for (int i = 0; i < possibleMove.size(); i++) {
                for (int j = 0; j < possibleMove.get(i).size(); j++) {
                    int score = 0;
                    PointPosition p = possibleMove.get(i).get(j);
                    //Contrôle des cases centrales
                    for (PointPosition sp : strategicPositions) {
                        if (p.equals(sp)) {
                            score += 10;
                        }
                    }
                    if (isOnStrategicPositionPlus(pawns.get(i).getCol(), pawns.get(i).getRow())) {
                        score -= 5;
                    }
                    for (PointPosition sp : strategicPositionsPlus) {
                        if (p.equals(sp)) {
                            score += 10;
                        }
                    }
                    //Proximité des pions adverses
                    for (Pawn pa : pawns_adverse) {
                        if (p.getDistance(pa) < 2) {
                            score += 15;
                        }
                    }
                    //Éloignement des bords
                    if (pawns.get(i).getRole()==0 && (p.getCol() == 0 || p.getCol() == 7 || p.getRow() == 0 || p.getRow() == 7)) {
                        score -= 8;
                    }
                    if (pawns.get(i).getRow() == 0 || pawns.get(i).getRow() == 7) {
                        if (pawns.get(i).getRole()==1) {
                            score += 5;
                        }
                        score += 5;
                    }
                    if (possibleMove.size()<10 && pawns.get(i).getRole()==1) {
                        score += 10;
                    }
                    //Sécurité
                    if ((p.getRow()==0 && p.getCol()==0) || (p.getRow()==0 && p.getCol()==7) || (p.getRow()==7 && p.getCol()==0) || (p.getRow()==7 && p.getCol()==7)){
                        score += 5;
                    }
                    possibleMove.get(i).get(j).setScore(score);
                }
            }

            //Sélection du mouvement :
            //◦ Choisir le mouvement avec le score le plus élevé parmi les mouvements évalués.
            //◦ Exécuter le mouvement sélectionné.
            int max = -10000;
            int k = 0;
            int l = 0;
            ArrayList<Integer> pawnIndex = new ArrayList<Integer>();
            ArrayList<PointPosition> bestMove = new ArrayList<PointPosition>();
            for (int i = 0; i < possibleMove.size(); i++) {
                for (int j = 0; j < possibleMove.get(i).size(); j++) {
                    if (possibleMove.get(i).get(j).getScore() > max) {
                        bestMove.clear();
                        pawnIndex.clear();
                        max = possibleMove.get(i).get(j).getScore();
                        pawnIndex.add(i);
                        bestMove.add(possibleMove.get(i).get(j));
                        k = i;
                        l = j;
                    } else if (possibleMove.get(i).get(j).getScore() == max) {
                        pawnIndex.add(i);
                        bestMove.add(possibleMove.get(i).get(j));
                    }
                }
            }
            /*
            for (int i = 0; i < bestMove.size(); i++) {
                System.out.println("Score : " + bestMove.get(i).getScore() + " Col : " + bestMove.get(i).getCol() + " Row : " + bestMove.get(i).getRow());
            }
            for (int i = 0; i < pawnIndex.size(); i++) {
                System.out.println(pawns.get(pawnIndex.get(i)).getCol() + " " + pawns.get(pawnIndex.get(i)).getRow());
            }
            */
            if (bestMove.size() >= 1){
                l = loto.nextInt(bestMove.size());  // if there are several best moves, choose one randomly
            }

            rowDest = bestMove.get(l).getRow();
            colDest = bestMove.get(l).getCol();
            pawn = pawns.get(pawnIndex.get(l));
            System.out.println("Colonne d'arrivée = "+colDest+", Ligne d'arrivée = "+rowDest+". Pion de départ : Col = "+pawn.getCol()+", Row = "+pawn.getRow());
            actions = ActionFactory.generatePutInContainer(model, pawn, "holeboard", rowDest, colDest);
            actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        } else if (currentPlayer.getComputerType()==2){

        }
        return actions;
    }

    private boolean analyseCorrectMove(int colPawn, int rowPawn, int finCol, int finRow) {
        HoleStageModel gameStage = (HoleStageModel) model.getGameStage();
        HoleBoard board = gameStage.getBoard();

        // check coords validity
        if ((finRow < 0) || (finRow >= 8) || (finCol < 0) || (finCol >= 8)) return false;

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
            if (!holeController.verifPawnMove(board, color, colPawn, rowPawn, finRow, finCol)) return false;
        } else {
            if (!holeController.verifMoveCavalier(board, colPawn, rowPawn, finRow, finCol, gameStage, color)) return false;
        }

        return true;
    }

    public int getColDest() {
        return colDest;
    }

    public int getRowDest() {
        return rowDest;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public boolean isOnStrategicPositionPlus(int col, int row) {
        PointPosition p = new PointPosition(row, col);
        PointPosition[] strategicPositionsPlus = new PointPosition[] {
                new PointPosition(3, 3), new PointPosition(4, 3), new PointPosition(3, 4), new PointPosition(4, 4)
        };
        for (PointPosition sp : strategicPositionsPlus) {
            if (p.equals(sp)) {
                return true;
            }
        }
        return false;
    }
}
