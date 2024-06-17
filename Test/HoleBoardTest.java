import boardifier.model.Model;
import boardifier.model.Player;
import model.Arrow;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class HoleBoardTest {

    private HoleBoard holeBoard;
    private Model mockModel;
    private Arrow mockArrow1;
    private Arrow mockArrow2;
    private HoleStageModel mockStageModel;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        mockArrow1 = mock(Arrow.class);
        mockArrow2 = mock(Arrow.class);
        mockPlayer = mock(Player.class);
        mockStageModel = mock(HoleStageModel.class);

        holeBoard = new HoleBoard(0, 0, mockStageModel);
    }

    @Test
    void testGetValidCellWithArrows() {
        when(mockArrow1.getDirection()).thenReturn(0);
        when(mockArrow2.getDirection()).thenReturn(1);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockModel.getCurrentPlayer().getColor()).thenReturn(1);
        int[][] result = holeBoard.getValidCell(mockModel, mockArrow1, mockArrow2, 4, 4);
        int[][] expected = {{3, 4}, {5, 4}, {4, 3}, {4, 5}};
        assertArrayEquals(expected, result);

        when(mockArrow1.getDirection()).thenReturn(2);
        when(mockArrow2.getDirection()).thenReturn(3);
        int[][] result2 = holeBoard.getValidCell(mockModel, mockArrow1, mockArrow2, 4, 4);
        int[][] expected2 = {{3,3}, {5,5}, {3, 5}, {5, 3}};
        assertArrayEquals(expected2, result2);
    }

    @Test
    void testGetValidCellWithoutArrows() {
        // Test pion sans ennemies autour
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
        int[][] result = holeBoard.getValidCell(mockModel, 4, 4);
        int[][] expected = {{3, 4}, {5, 4}, {4, 3}, {4, 5}, {3, 3}, {5, 5}, {3, 5}, {5, 3}};
        assertArrayEquals(expected, result);

        // Test pion présent sur une case autour
        //Un saut de pion est effectué de façon à ce que le pion joueur en 4,4 puissent aller en 4,6 si un pion ennemie est en 4,5
        Pawn pawnEnnemy = mock(Pawn.class);
        when(pawnEnnemy.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockModel.getCurrentPlayer().getColor()).thenReturn(1);
        holeBoard.addElement(pawnEnnemy, 3,3);
        holeBoard.addElement(pawnEnnemy, 5,5);
        holeBoard.addElement(pawnEnnemy, 3,4);
        holeBoard.addElement(pawnEnnemy, 5,4);
        holeBoard.addElement(pawnEnnemy, 4,3);
        holeBoard.addElement(pawnEnnemy, 4,5);
        holeBoard.addElement(pawnEnnemy, 3,5);
        holeBoard.addElement(pawnEnnemy, 5,3);

        int[][] result2 = holeBoard.getValidCell(mockModel, 4, 4);
        int[][] expected2 = {{2, 4}, {6, 4}, {4, 2}, {4, 6}, {2, 2}, {6, 6}, {2, 6}, {6, 2}};
        assertArrayEquals(expected2, result2);
    }

    @Test
    void testBaseTakingPawn() {
        int row = 4;
        int col = 4;
        Pawn PawnEnemy = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, mockStageModel);
        Pawn pawn = new Pawn(0, Pawn.PAWN_RED, mockStageModel);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockModel.getCurrentPlayer().getColor()).thenReturn(1);

        // Simulate enemy pawns around the position
        holeBoard.addElement(pawn, row - 1, col);
        holeBoard.addElement(pawn, row + 1, col);
        holeBoard.addElement(pawn, row, col - 1);
        holeBoard.addElement(pawn, row, col + 1);
        holeBoard.addElement(PawnEnemy, row, col);

        //Test vertical
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row-1, col, Pawn.PAWN_RED, Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row+1, col, Pawn.PAWN_RED,  Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);

        //Test horizontal
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row, col - 1, Pawn.PAWN_RED ,Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row, col + 1, Pawn.PAWN_RED,  Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);

        //Diagonale Major
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row- 1, col - 1, Pawn.PAWN_RED,  Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row + 1, col + 1, Pawn.PAWN_RED,  Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);

        //Diagonale Minor
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row - 1, col + 1, Pawn.PAWN_RED,  Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, row, col);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row + 1, col -1, Pawn.PAWN_RED,  Pawn.PAWN_BLUE);

        // Verify if deletePawnsTaking was called correctly
        verify(mockStageModel, times(8)).removeBluePawns(PawnEnemy);
    }

    @Test
    void testCoinTakingPawn() {

        Pawn PawnEnemy = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, mockStageModel);
        Pawn pawn = new Pawn(0, Pawn.PAWN_RED, mockStageModel);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
        when(mockModel.getCurrentPlayer().getColor()).thenReturn(1);


        //Test coin Supérieur Gauche
        holeBoard.addElement(PawnEnemy, 0, 0);
        holeBoard.addElement(pawn, 1, 0);
        holeBoard.addElement(pawn, 0, 1);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 1, 0, Pawn.PAWN_RED, Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, 0, 0);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 0, 1, Pawn.PAWN_RED,Pawn.PAWN_BLUE);


        //Test coin supérieur droit
        holeBoard.addElement(PawnEnemy, 0, 7);
        holeBoard.addElement(pawn, 0, 6);
        holeBoard.addElement(pawn, 1, 7);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 0, 6, Pawn.PAWN_RED, Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, 0, 7);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel,1, 7, Pawn.PAWN_RED, Pawn.PAWN_BLUE);

        //Test coin inférieur gauche
        holeBoard.addElement(PawnEnemy, 7, 0);
        holeBoard.addElement(pawn, 6, 0);
        holeBoard.addElement(pawn, 7, 1);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 6, 0, Pawn.PAWN_RED, Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, 7, 0);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 7, 1, Pawn.PAWN_RED,Pawn.PAWN_BLUE);

        //Coin Inférieur droit
        holeBoard.addElement(PawnEnemy, 7, 7);
        holeBoard.addElement(pawn, 7, 6);
        holeBoard.addElement(pawn, 6, 7);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 7, 6, Pawn.PAWN_RED, Pawn.PAWN_BLUE);
        holeBoard.addElement(PawnEnemy, 7, 7);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 6, 7, Pawn.PAWN_RED, Pawn.PAWN_BLUE);

        // Verify if deletePawnsTaking was called correctly
        verify(mockStageModel, times(8)).removeBluePawns(PawnEnemy);
    }

    @Test
    void testIsCapturable() {
        Pawn mockPawn = mock(Pawn.class);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_BLUE);

        holeBoard.addElement(mockPawn, 3, 4);
        holeBoard.addElement(mockPawn, 5, 4);
        boolean result = holeBoard.isCapturable(holeBoard, 4, 4, Pawn.PAWN_BLUE);
        assertTrue(result);

        holeBoard.addElement(mockPawn, 4, 3);
        holeBoard.addElement(mockPawn, 4, 5);
        boolean result2 = holeBoard.isCapturable(holeBoard, 4, 4, Pawn.PAWN_BLUE);
        assertTrue(result2);

        holeBoard.addElement(mockPawn, 3, 3);
        holeBoard.addElement(mockPawn, 5, 5);
        boolean result3 = holeBoard.isCapturable(holeBoard, 4, 4, Pawn.PAWN_BLUE);
        assertTrue(result3);

        holeBoard.addElement(mockPawn, 3, 5);
        holeBoard.addElement(mockPawn, 5, 3);
        boolean result4 = holeBoard.isCapturable(holeBoard, 4, 4, Pawn.PAWN_BLUE);
        assertTrue(result4);
    }

    @Test
    void testIsCapturableCoin(){
        Pawn mockPawn = mock(Pawn.class);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_BLUE);

        holeBoard.addElement(mockPawn, 1, 0);
        holeBoard.addElement(mockPawn, 0, 1);
        boolean result = holeBoard.isCapturable(holeBoard, 0, 0, Pawn.PAWN_BLUE);
        assertTrue(result);

        holeBoard.addElement(mockPawn, 7, 1);
        holeBoard.addElement(mockPawn, 6, 0);
        boolean result2 = holeBoard.isCapturable(holeBoard, 7, 0, Pawn.PAWN_BLUE);
        assertTrue(result2);

        holeBoard.addElement(mockPawn, 0, 6);
        holeBoard.addElement(mockPawn, 1, 7);
        boolean result3 = holeBoard.isCapturable(holeBoard, 0, 7, Pawn.PAWN_BLUE);
        assertTrue(result3);

        holeBoard.addElement(mockPawn, 7, 6);
        holeBoard.addElement(mockPawn, 6, 7);
        boolean result4 = holeBoard.isCapturable(holeBoard, 7, 7, Pawn.PAWN_BLUE);
        assertTrue(result4);
    }

    @Test
    void testCheckPiece() {
        Pawn mockPawn = mock(Pawn.class);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_RED);

        holeBoard.addElement(mockPawn, 3, 3);

        boolean result = holeBoard.checkPiece(holeBoard, 3, 3, Pawn.PAWN_RED);
        assertTrue(result);
    }

    @Test
    public void testCheckIsCapturableWithoutCoupInterditVertical() {
        Pawn pawnBlue = mock(Pawn.class);
        Pawn pawnRed = mock(Pawn.class);

        holeBoard.addElement(pawnBlue, 7,7);

        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditVertical(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditVertical(1,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditVertical(7,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        when(pawnBlue.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(pawnRed.getColor()).thenReturn(Pawn.PAWN_RED);

        holeBoard.addElement(pawnBlue, 1,3);
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditVertical(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.addElement(pawnRed, 2,3);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditVertical(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);


        holeBoard.addElement(pawnBlue, 5,3);
        holeBoard.addElement(pawnRed, 4,3);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditVertical(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
    }

    @Test
    public void testCheckIsCapturableWithoutCoupInterditHorizontal() {
        Pawn pawnBlue = mock(Pawn.class);
        Pawn pawnRed = mock(Pawn.class);

        holeBoard.addElement(pawnBlue, 7,7);

        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditHorizontal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditHorizontal(3,1, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditHorizontal(3,7, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        when(pawnBlue.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(pawnRed.getColor()).thenReturn(Pawn.PAWN_RED);

        holeBoard.addElement(pawnBlue, 3,1);
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditHorizontal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.addElement(pawnRed, 3,2);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditHorizontal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);


        holeBoard.addElement(pawnBlue, 3,5);
        holeBoard.addElement(pawnRed, 3,4);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditHorizontal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
    }

    @Test
    public void testCheckIsCapturableWithoutCoupInterditMajorDiagonal() {
        Pawn pawnBlue = mock(Pawn.class);
        Pawn pawnRed = mock(Pawn.class);

        holeBoard.addElement(pawnBlue, 7,7);

        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(3,1, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(3,7, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(1,1, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(7,7, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        when(pawnBlue.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(pawnRed.getColor()).thenReturn(Pawn.PAWN_RED);

        holeBoard.addElement(pawnBlue, 1,1);
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.addElement(pawnRed, 2,2);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);


        holeBoard.addElement(pawnBlue, 5,5);
        holeBoard.addElement(pawnRed, 4,4);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditMajorDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
    }

    @Test
    public void testCheckIsCapturableWithoutCoupInterditMinorrDiagonal() {
        Pawn pawnBlue = mock(Pawn.class);
        Pawn pawnRed = mock(Pawn.class);

        holeBoard.addElement(pawnBlue, 7,7);

        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(3,1, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(3,7, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(1,1, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(7,7, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        when(pawnBlue.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(pawnRed.getColor()).thenReturn(Pawn.PAWN_RED);

        holeBoard.addElement(pawnBlue, 1,5);
        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.addElement(pawnRed, 2,4);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);


        holeBoard.addElement(pawnBlue, 5,1);
        holeBoard.addElement(pawnRed, 4,2);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterditMinorrDiagonal(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));
    }


    @Test
    public void testCheckIsCapturableWithoutCoupInterdit(){
        Pawn pawnBlue = mock(Pawn.class);
        Pawn pawnRed = mock(Pawn.class);
        when(pawnBlue.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(pawnRed.getColor()).thenReturn(Pawn.PAWN_RED);

        assertFalse(holeBoard.CheckIsCapturableWithoutCoupInterdit(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.addElement(pawnRed, 2,2);
        holeBoard.addElement(pawnBlue, 1,1);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterdit(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);
        holeBoard.addElement(pawnRed, 2,3);
        holeBoard.addElement(pawnBlue, 1, 3);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterdit(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));


        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);
        holeBoard.addElement(pawnRed, 2,4);
        holeBoard.addElement(pawnBlue, 1,5);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterdit(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));


        holeBoard.removeElement(pawnBlue);
        holeBoard.removeElement(pawnRed);
        holeBoard.addElement(pawnRed, 3,2);
        holeBoard.addElement(pawnBlue, 3,1);
        assertTrue(holeBoard.CheckIsCapturableWithoutCoupInterdit(3,3, Pawn.PAWN_BLUE, Pawn.PAWN_RED));

    }
}
