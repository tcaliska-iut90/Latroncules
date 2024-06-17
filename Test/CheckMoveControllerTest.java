import boardifier.control.Logger;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.RootPane;
import boardifier.view.View;
import control.CheckMoveController;
import javafx.stage.Stage;
import model.Arrow;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import view.HoleView;

import java.io.BufferedReader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckMoveControllerTest {

    @Mock
    private Model mockModel;
    @Mock
    View mockView;
    @Mock
    private Player mockPlayer;
    @Mock
    private HoleStageModel mockStageModel;
    @Mock
    private HoleBoard mockBoard;

    @Mock
    CheckMoveController controller;
    @Mock
    Pawn mockPawn;

    Model model;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockModel = mock(Model.class);
        mockView = mock(View.class);
        mockPawn = mock(Pawn.class);
        mockBoard = mock(HoleBoard.class);
        mockStageModel = mock(HoleStageModel.class);
        controller = new CheckMoveController(mockModel);

        when(mockModel.getGameStage()).thenReturn(mockStageModel);
        when(mockStageModel.getBoard()).thenReturn(mockBoard);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
    }

    @Test
    public void testVerifPawnMove() {
        // Test coup possible pion bleu
        boolean result = verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 0, 7, 6, 0);
        assertFalse(result);

        // Test coup possible pion rouge
        boolean result2 = verifPawnMove(mockBoard, Pawn.PAWN_RED, 0, 0, 1, 0);
        assertFalse(result2);


        // Test coup interdit
        when(mockBoard.getElement(1, 1)).thenReturn(null);
        when(mockBoard.getElement(1, 0)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        when(mockBoard.getElement(1, 2)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        boolean result4 = verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 1, 0, 1, 1);
        assertFalse(result4);

        // Test coup possible
        when(mockBoard.getElement(1, 0)).thenReturn(null);
        when(mockBoard.getElement(1, 1)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        boolean result5 = verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 0, 0, 1, 0);
        assertTrue(result5);
    }

    @Test
    public void testVerifMoveCavalier() {


        int[][] validCells = {{2, 1}, {3, 3}};
        Arrow[][] a = new Arrow[8][8];

        // Test mouvement impossible sur une case non fléchés
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        boolean result = controller.verifMoveCavalier(mockBoard, Pawn.PAWN_BLUE,1, 1, 2, 2, mockStageModel);
        assertFalse(result);

        // Test mouvement possible sur une case non fléchés
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        when(mockBoard.getElement(2, 1)).thenReturn(null);
        //when(testCoupInterdit(anyInt(), anyInt(), any(HoleBoard.class),anyInt())).thenReturn(true);
        boolean result2 = controller.verifMoveCavalier(mockBoard,Pawn.PAWN_BLUE, 1, 1, 2, 1, mockStageModel );
        assertTrue(result2);

        // Test cases fléchés
        Arrow mockArrow = mock(Arrow.class);
        Arrow[][] arrows1 = {{mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow},
                {mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow}};
        when(mockStageModel.getBoardArrows1()).thenReturn(arrows1);
        when(mockStageModel.getBoardArrows2()).thenReturn(arrows1);
        when(mockBoard.getValidCell(any(Model.class), eq(mockArrow), eq(mockArrow), anyInt(), anyInt())).thenReturn(validCells);
        boolean result3 = controller.verifMoveCavalier(mockBoard,Pawn.PAWN_BLUE, 1, 1, 2, 1, mockStageModel);
        assertTrue(result3);

        // Test mouvement pas possible sur une case non fléchés et un pion sur la case d'arrivée
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        when(mockBoard.getElement(2, 1)).thenReturn(mockPawn);
        boolean result4 = controller.verifMoveCavalier(mockBoard, Pawn.PAWN_BLUE,1, 1, 2, 1, mockStageModel);
        assertFalse(result4);

        // Test cases fléchés et un pion sur la case d'arrivée
        when(mockStageModel.getBoardArrows1()).thenReturn(arrows1);
        when(mockStageModel.getBoardArrows2()).thenReturn(arrows1);
        when(mockBoard.getValidCell(any(Model.class), eq(mockArrow), eq(mockArrow), anyInt(), anyInt())).thenReturn(validCells);
        boolean result5 = controller.verifMoveCavalier(mockBoard, Pawn.PAWN_BLUE,1, 1, 2, 1, mockStageModel);
        assertFalse(result5);
    }

    @Test
    public void testChangeInfantrymanBlueToHorseman() {
        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_BLUE);

        boolean result = controller.changeInfantrymanToHorseman(mockPawn, 7);
        assertTrue(result);

        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_BLUE);

        boolean result2 = controller.changeInfantrymanToHorseman(mockPawn, 5);
        assertFalse(result2);
    }

    @Test
    public void testChangeInfantrymanRedToHorseman(){
        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);

        boolean result = controller.changeInfantrymanToHorseman(mockPawn, 0);
        assertTrue(result);

        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);

        boolean result2 = controller.changeInfantrymanToHorseman(mockPawn, 5);
        assertFalse(result2);
    }



    @Test
    void testCheckCoupInterditHorizontal() {
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);
        when(mockBoard.getElement(anyInt(), anyInt())).thenReturn(mockPawn);
        assertFalse(checkCoupInterditHorizontal(3, 3, mockBoard, Pawn.PAWN_BLUE));


        when(mockBoard.isCapturable(any(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        assertTrue(checkCoupInterditHorizontal(3, 3, mockBoard, Pawn.PAWN_BLUE));
    }

    @Test
    void testCheckCoupInterditVertical() {
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);
        when(mockBoard.getElement(anyInt(), anyInt())).thenReturn(mockPawn);
        assertFalse(checkCoupInterditVertical(3, 3, mockBoard, Pawn.PAWN_BLUE));

        when(mockBoard.isCapturable(any(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        assertTrue(checkCoupInterditVertical(3, 3, mockBoard, Pawn.PAWN_BLUE));
    }

    @Test
    void testCheckCoupInterditDiagonal() {
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);
        when(mockBoard.getElement(anyInt(), anyInt())).thenReturn(mockPawn);
        assertFalse(checkCoupInterditDiagonal(3, 3, mockBoard, Pawn.PAWN_BLUE));

        when(mockBoard.isCapturable(any(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        assertTrue(checkCoupInterditDiagonal(3, 3, mockBoard, Pawn.PAWN_BLUE));
    }

    @Test
    void testCoupInterditCoinSupérieurGauche() {
        when(mockBoard.getElement(0, 1)).thenReturn(mockPawn);
        when(mockBoard.getElement(1, 0)).thenReturn(mockPawn);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);


        assertFalse( checkCoinSupérieurGauche(0, 0, mockBoard, Pawn.PAWN_BLUE), "Coin supérieur gauche en étant capturable doit être interdit");

        when(mockBoard.getElement(0, 1)).thenReturn(null);
        assertTrue(checkCoinSupérieurGauche(0, 0, mockBoard, Pawn.PAWN_BLUE), "Coin supérieur gauche avec seulement un pion adverse autour est autorisé");
    }

    @Test
    void testCoupInterditCoinSupérieurDroit() {
        when(mockBoard.getElement(0, 6)).thenReturn(mockPawn);
        when(mockBoard.getElement(1, 7)).thenReturn(mockPawn);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);


        assertFalse(testCoupInterdit(7, 0, mockBoard, Pawn.PAWN_BLUE), "Coin supérieur droit en étant capturable doit être interdit");

        when(mockBoard.getElement(0, 6)).thenReturn(null);
        assertTrue(checkCoinInférieurDroit(7, 0, mockBoard, Pawn.PAWN_BLUE), "Coin supérieur droit avec seulement un pion adverse autour est autorisé");
    }

    @Test
    void testCoupInterditCoinInférieurGauche() {
        when(mockBoard.getElement(6, 0)).thenReturn(mockPawn);
        when(mockBoard.getElement(7, 1)).thenReturn(mockPawn);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);

        assertFalse( testCoupInterdit(0, 7, mockBoard, Pawn.PAWN_BLUE), "Coin inférieur gauche en étant capturable doit être interdit");

        when(mockBoard.getElement(6, 0)).thenReturn(null);
        assertTrue(checkCoinInférieurGauche(0, 7, mockBoard, Pawn.PAWN_BLUE), "Coin inférieur gauche avec seulement un pion adverse autour est autorisé");
    }

    @Test
    void testCoupInterditCoinInférieurDroit() {
        when(mockBoard.getElement(6, 7)).thenReturn(mockPawn);
        when(mockBoard.getElement(7, 6)).thenReturn(mockPawn);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);

        assertFalse(testCoupInterdit(7, 7, mockBoard, Pawn.PAWN_BLUE), "Coin inférieur droit en étant capturable doit être interdit");

        when(mockBoard.getElement(6, 7)).thenReturn(null);
        assertTrue(checkCoinInférieurDroit(0, 7, mockBoard, Pawn.PAWN_BLUE), "Coin inférieur droit avec seulement un pion adverse autour est autorisé");
    }


























    private boolean testCoupInterdit(int finCol, int finRow, HoleBoard board, int color){

        if ((finRow == 0 || finRow == 7) && (finCol == 0 || finCol == 7)){
            return checkCoupInterditCoin(finCol, finRow, board, color);
        }
        if (finRow > 0 && finRow < 7 && !checkCoupInterditVertical(finCol, finRow, board, color)) return false;

        if (finCol > 0 && finCol < 7 && !checkCoupInterditHorizontal(finCol, finRow, board, color))return false;

        if ((finCol > 0 && finCol < 7) && (finRow > 0 && finRow < 7) && !checkCoupInterditDiagonal(finCol, finRow, board, color))return false;

        if (finRow > 0 && finRow < 7 && finCol > 0 && finCol < 7 && !checkCoupInterditVertical(finCol, finRow, board, color)) return false;

        return true;
    }

    private Boolean checkCoupInterditHorizontal(int finCol, int finRow, HoleBoard board, int color){

        Pawn p1 = (Pawn) board.getElement(finRow, finCol - 1);
        Pawn p2 =(Pawn) board.getElement(finRow, finCol + 1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow, finCol - 1, color) || !board.isCapturable(board, finRow, finCol + 1, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoupInterditVertical(int finCol, int finRow, HoleBoard board, int color){
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol );
        Pawn p2 =(Pawn) board.getElement(finRow + 1, finCol);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow + 1, finCol, color) || !board.isCapturable(board, finRow - 1, finCol, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoupInterditDiagonal(int finCol, int finRow, HoleBoard board, int color){
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol-1 );
        Pawn p2 =(Pawn) board.getElement(finRow + 1, finCol+1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow + 1, finCol +1, color) || !board.isCapturable(board, finRow - 1, finCol - 1, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }

        p1 = (Pawn) board.getElement(finRow - 1, finCol+1 );
        p2 =(Pawn) board.getElement(finRow + 1, finCol-1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow - 1, finCol +1, color) || !board.isCapturable(board, finRow + 1, finCol - 1, color)) {
                System.out.println("Impossible, coup interdit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoupInterditCoin(int finCol, int finRow, HoleBoard board, int color) {
        // Vérification pour le coin supérieur gauche (0, 0)
        if (finCol == 0 && finRow == 0) {
            return checkCoinSupérieurGauche(finCol, finRow, board, color);
        }
        // Vérification pour le coin supérieur droit (0, 7)
        if (finCol == 7 && finRow == 0) {
            return checkCoinSupérieurDroit(finCol, finRow, board, color);
        }
        // Vérification pour le coin inférieur gauche (7, 0)
        if (finCol == 0 && finRow == 7) {
            return checkCoinInférieurGauche(finCol, finRow, board, color);
        }
        // Vérification pour le coin inférieur droit (7, 7)
        if (finCol == 7 && finRow == 7) {
            return checkCoinInférieurDroit(finCol, finRow, board, color);
        }
        return true;
    }

    private boolean checkCoinSupérieurGauche(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow, finCol + 1);
        Pawn p2 = (Pawn) board.getElement(finRow + 1, finCol);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow, finCol + 1, color) || !board.isCapturable(board, finRow + 1, finCol, color)) {
                System.out.println("Impossible, coup interdit au coin supérieur gauche");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoinSupérieurDroit(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow, finCol - 1);
        Pawn p2 = (Pawn) board.getElement(finRow + 1, finCol);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow, finCol - 1, color) || !board.isCapturable(board, finRow + 1, finCol, color)) {
                System.out.println("Impossible, coup interdit au coin supérieur droit");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoinInférieurGauche(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol);
        Pawn p2 = (Pawn) board.getElement(finRow, finCol + 1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow - 1, finCol, color) || !board.isCapturable(board, finRow, finCol + 1, color)) {
                System.out.println("Impossible, coup interdit au coin inférieur gauche");
                return false;
            }
        }
        return true;
    }

    private boolean checkCoinInférieurDroit(int finCol, int finRow, HoleBoard board, int color) {
        Pawn p1 = (Pawn) board.getElement(finRow - 1, finCol);
        Pawn p2 = (Pawn) board.getElement(finRow, finCol - 1);
        if ((p1 != null && p1.getColor() != color) && (p2 != null && p2.getColor() != color)) {
            if (!board.isCapturable(board, finRow - 1, finCol, color) || !board.isCapturable(board, finRow, finCol - 1, color)) {
                System.out.println("Impossible, coup interdit au coin inférieur droit");
                return false;
            }
        }
        return true;
    }


    private boolean verifPawnMove(HoleBoard board, int color, int colPawn, int rowPawn, int finRow, int finCol) {
        //Test mouvement possible en fonction de la couleur
        if ((color == Pawn.PAWN_BLUE && (colPawn != finCol || rowPawn + 1 != finRow)) || (color == Pawn.PAWN_RED && (colPawn != finCol || rowPawn - 1 != finRow))) {
            return false;
        }

        return testCoupInterdit(finCol, finRow, board, color);
    }
}
