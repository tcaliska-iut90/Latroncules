import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import control.HoleController;
import model.Arrow;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.io.BufferedReader;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HoleControllerTest {

    @Mock
    private Model mockModel;
    @Mock
    private View mockView;
    @Mock
    private Player mockPlayer;
    @Mock
    private HoleStageModel mockStageModel;
    @Mock
    private HoleBoard mockBoard;
    @Mock
    private BufferedReader mockBufferedReader;
    @Mock
    private HoleController holeController;

    @Mock
    private Pawn mockPawn;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        holeController = new HoleController(mockModel, mockView);

        holeController.consoleIn = mockBufferedReader;


        when(mockModel.getGameStage()).thenReturn(mockStageModel);
        when(mockStageModel.getBoard()).thenReturn(mockBoard);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
    }

    @Disabled
    public void testStageLoop(){
        when(mockModel.isEndStage()).thenReturn(false,true);
        when(mockPawn.getRole()).thenReturn(Player.COMPUTER);
        doNothing().when(holeController).playTurn();
        doNothing().when(holeController).update();

        verify(holeController, times(2)).update();
        verify(holeController, times(1)).playTurn();
        verify(holeController, times(1)).endOfTurn();
    }


    @Test
    public void testPlayTurnComputer(){
        when(mockPlayer.getType()).thenReturn(Player.COMPUTER);
        String s = holeController.playTurn();
        assertEquals("Computer", s);
    }

    @Disabled
    public void testPlayTurnHuman() throws IOException {
        when(mockPlayer.getType()).thenReturn(Player.HUMAN);
        when(mockBufferedReader.readLine()).thenReturn("A1B2");
        when(holeController.analyseAndPlay("A1B2")).thenReturn(true);
        String s = holeController.playTurn();
        assertEquals("Human", s);
    }

    @Test
    public void testVerifPawnMove() {
        // Test coup possible pion bleu
        boolean result = holeController.verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 0, 7, 6, 0);
        assertFalse(result);

        // Test coup possible pion rouge
        boolean result2 = holeController.verifPawnMove(mockBoard, Pawn.PAWN_RED, 0, 0, 1, 0);
        assertFalse(result2);

        // Test aucun pion devant le pion joueur
        when(mockBoard.getElement(1, 1)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        boolean result3 = holeController.verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 1, 0, 1, 1);
        assertFalse(result3);

        // Test coup interdit
        when(mockBoard.getElement(1, 1)).thenReturn(null);
        when(mockBoard.getElement(1, 0)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        when(mockBoard.getElement(1, 2)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        boolean result4 = holeController.verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 1, 0, 1, 1);
        assertFalse(result4);

        // Test coup possible
        when(mockBoard.getElement(1, 0)).thenReturn(null);
        when(mockBoard.getElement(1, 1)).thenReturn(new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, mockStageModel));
        boolean result5 = holeController.verifPawnMove(mockBoard, Pawn.PAWN_BLUE, 0, 0, 1, 0);
        assertTrue(result5);
    }

    @Test
    public void testVerifMoveCavalier() {
        int[][] validCells = {{2, 1}, {3, 3}};
        Arrow[][] a = new Arrow[8][8];

        // Test mouvement impossible sur une case non fléchés
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        boolean result = holeController.verifMoveCavalier(mockBoard, 1, 1, 2, 2, mockStageModel, Pawn.PAWN_BLUE);
        assertFalse(result);

        // Test mouvement possible sur une case non fléchés
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        boolean result2 = holeController.verifMoveCavalier(mockBoard, 1, 1, 2, 1, mockStageModel, Pawn.PAWN_BLUE);
        assertTrue(result2);

        // Test cases fléchés
        Arrow mockArrow = mock(Arrow.class);
        Arrow[][] arrows1 = {{mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow},
                {mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow}};
        when(mockStageModel.getBoardArrows1()).thenReturn(arrows1);
        when(mockStageModel.getBoardArrows2()).thenReturn(arrows1);
        when(mockBoard.getValidCell(any(Model.class), eq(mockArrow), eq(mockArrow), anyInt(), anyInt())).thenReturn(validCells);
        boolean result3 = holeController.verifMoveCavalier(mockBoard, 1, 1, 2, 1, mockStageModel, Pawn.PAWN_BLUE);
        assertTrue(result3);
    }

    @Test
    public void testChangeInfantrymanBlueToHorseman() {
        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_BLUE);
        holeController.changeInfantrymanToHorseman(mockPawn, 7);
        verify(mockPawn, times(1)).setRole(Pawn.HORSEMAN);
    }

    @Test
    public void testChangeInfantrymanRedToHorseman(){
        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);
        holeController.changeInfantrymanToHorseman(mockPawn, 0);
        verify(mockPawn, times(1)).setRole(Pawn.HORSEMAN);
    }

    @Test
    public void testCoordonnePawnAnalyseAndPlay(){
        boolean result = holeController.analyseAndPlay("a1B2");
        assertFalse(result);

        boolean result2 = holeController.analyseAndPlay("Z1B2");
        assertFalse(result2);

        boolean result3 = holeController.analyseAndPlay("A0B2");
        assertFalse(result3);

        boolean result4 = holeController.analyseAndPlay("A9B2");
        assertFalse(result4);
    }

    @Test
    public void testCoordonneMovePawnAnalyseAndPlay(){
        boolean result = holeController.analyseAndPlay("A1a2");
        assertFalse(result);

        boolean result2 = holeController.analyseAndPlay("B2Z1");
        assertFalse(result2);

        boolean result3 = holeController.analyseAndPlay("B2A0");
        assertFalse(result3);

        boolean result4 = holeController.analyseAndPlay("B2A9");
        assertFalse(result4);
    }

    @Test
    public void testColorAnalyseAndPlay(){
        when(mockModel.getIdPlayer()).thenReturn(0);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);
        boolean result = holeController.analyseAndPlay("A1A2");
        assertFalse(result);

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
    void testCoupInterdit(){
        when(mockBoard.getElement(anyInt(), anyInt())).thenReturn(mockPawn);
        when(checkCoupInterditDiagonal(anyInt(), anyInt(), any(), anyInt())).thenReturn(false);
        when(mockBoard.isCapturable(any(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        assertFalse(testCoupInterdit(3, 3, mockBoard, Pawn.PAWN_BLUE));
    }


    private boolean testCoupInterdit(int finCol, int finRow, HoleBoard board, int color){
        if ((finCol > 0 && finCol < 7) && (finRow > 0 && finRow < 7)){
            if(!checkCoupInterditDiagonal(finCol, finRow, board, color)) return false;
        }

        if (finCol > 0 && finCol < 7){
            if(!checkCoupInterditHorizontal(finCol, finRow, board, color)) return false;}
        if (finRow > 0 && finRow < 7){
            if(!checkCoupInterditVertical(finCol, finRow, board, color)) return false;}
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

}
