package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HoleDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public HoleDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        HoleStageModel stage = (HoleStageModel)model.getGameStage();
        HoleBoard board = stage.getBoard(); // get the board
        GameElement pawn; // the pawn that is moved
        int rowDest; // the dest. row in board
        int colDest; // the dest. col in board

        /*
        1ère IA (position stratégique et minimisation des risques) :

        Initialisation :
        ◦ Créer une liste de tous les mouvements valides pour le joueur actuel.
        ◦ Initialiser une liste de positions stratégiques sur le plateau (par exemple, les cases centrales).

                Évaluation des mouvements :
        ◦ Pour chaque mouvement valide, évaluer la position finale du pion.
        ◦ Attribuer un score à chaque mouvement basé sur plusieurs critères :
            ▪ Contrôle des cases centrales : Les mouvements qui placent un pion sur une case centrale obtiennent un score plus élevé.
            ▪ Proximité des pions adverses : Les mouvements qui rapprochent un pion d'un pion adverse obtiennent un score plus élevé, favorisant les futures opportunités de capture.
            ▪ Éloignement des bords : Les mouvements qui placent un pion loin des bords du plateau peuvent être favorisés pour éviter les pièges.
            ▪ Sécurité : Les mouvements qui placent un pion dans une position moins susceptible d'être capturé au tour suivant obtiennent un score plus élevé.

        Sélection du mouvement :
        ◦ Choisir le mouvement avec le score le plus élevé parmi les mouvements évalués.
        ◦ Exécuter le mouvement sélectionné.
        */

        // get the list of all pawns that can be moved
        List<GameElement> pawns = board.getElements(model.getIdPlayer());
        // get the list of all possible moves for each pawn
        ActionList actions = ActionFactory.generatePutInContainer(model, pawns.get(0), "holeboard", 0, 0);
        // get the list of all possible moves for each pawn
        for (GameElement p : pawns) {
            // get the list of all possible moves for the pawn
            List<Point> moves = board.getValidMoves(p);
            // for each possible move, add an action to the list of actions
            for (Point move : moves) {
                actions.add(ActionFactory.generatePutInContainer(model, p, "holeboard", move.y, move.x));
            }
        }

        ActionList actions = ActionFactory.generatePutInContainer( model, pawn, "holeboard", rowDest, colDest);
        //actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.

        return actions;
    }
}
