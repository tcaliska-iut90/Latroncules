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
        HoleStageModel stage = (HoleStageModel)model.getGameStage();
        HoleBoard board = stage.getBoard(); // get the board
        Pawn pawn; // the pawn that is moved
        int rowDest; // the dest. row in board
        int colDest; // the dest. col in board


        //1ère IA (position stratégique et minimisation des risques) :
        //Initialisation :
        //Créer une liste de tous les mouvements valides pour le joueur actuel.
        List<Pawn> pawns = stage.getPawns(currentPlayer);
        ActionList actions = new ActionList();

        //Initialiser une liste de positions stratégiques sur le plateau (par exemple, les cases centrales).
        PointPosition[] strategicPositions = new PointPosition[] {
                new PointPosition(2,2), new PointPosition(3,2), new PointPosition(4,2), new PointPosition(5,2),
                new PointPosition(2,3), new PointPosition(3,3), new PointPosition(4,3), new PointPosition(5,3),
                new PointPosition(2,4), new PointPosition(3,4), new PointPosition(4,4), new PointPosition(5,4),
                new PointPosition(2,5), new PointPosition(3,5), new PointPosition(4,5), new PointPosition(5,5)
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

        return actions;
    }


}
