import boardifier.model.Model;
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

    @BeforeEach
    void setUp() {
        mockModel = mock(Model.class);
        mockArrow1 = mock(Arrow.class);
        mockArrow2 = mock(Arrow.class);
        mockStageModel = mock(HoleStageModel.class);

        holeBoard = new HoleBoard(0, 0, mockStageModel);  // assuming some GameStageModel is used
    }

    @Test
    void testGetValidCellWithArrows() {
        when(mockArrow1.getDirection()).thenReturn(0);  // Vertical arrow
        when(mockArrow2.getDirection()).thenReturn(1);  // Horizontal arrow

        int[][] result = holeBoard.getValidCell(mockModel, mockArrow1, mockArrow2, 4, 4);
        int[][] expected = {{3, 4}, {5, 4}, {4, 3}, {4, 5}};
        assertArrayEquals(expected, result);

        when(mockArrow1.getDirection()).thenReturn(2);  // Vertical arrow
        when(mockArrow2.getDirection()).thenReturn(3);  // Horizontal arrow
        int[][] result2 = holeBoard.getValidCell(mockModel, mockArrow1, mockArrow2, 4, 4);
        int[][] expected2 = {{3,3}, {5,5}, {3, 5}, {5, 3}};
        assertArrayEquals(expected2, result2);
    }

    @Test
    void testGetValidCellWithoutArrows() {
        int[][] result = holeBoard.getValidCell(mockModel, 4, 4);

        int[][] expected = {{3, 4}, {5, 4}, {4, 3}, {4, 5}, {3, 3}, {5, 5}, {3, 5}, {5, 3}};
        assertArrayEquals(expected, result);
    }

    @Test
    void testBaseTakingPawn() {
        int row = 4;
        int col = 4;
        Pawn PawnEnemy = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, mockStageModel);
        Pawn pawn = new Pawn(0, Pawn.PAWN_RED, mockStageModel);
        when(mockModel.getIdPlayer()).thenReturn(1);

        // Simulate enemy pawns around the position
        holeBoard.addElement(pawn, row - 1, col);  // Pawn above
        holeBoard.addElement(pawn, row + 1, col);  // Pawn below
        holeBoard.addElement(pawn, row, col - 1);  // Pawn left
        holeBoard.addElement(pawn, row, col + 1);  // Pawn right
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy

        //Test vertical
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row-1, col, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row+1, col, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy

        //Test horizontal
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row, col - 1, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row, col + 1, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy

        //Diagonale Major
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row- 1, col - 1, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row + 1, col + 1, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy

        //Diagonale Minor
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row - 1, col + 1, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, row, col); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, row + 1, col -1, Pawn.PAWN_RED);

        // Verify if deletePawnsTaking was called correctly
        verify(mockStageModel, times(8)).addRedPawnsTaking(PawnEnemy);
        verify(mockStageModel, times(8)).removeBluePawns(PawnEnemy);
    }

    @Test
    void testCoinTakingPawn() {

        Pawn PawnEnemy = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, mockStageModel);
        Pawn pawn = new Pawn(0, Pawn.PAWN_RED, mockStageModel);
        when(mockModel.getIdPlayer()).thenReturn(1);


        //Test coin Supérieur Gauche
        holeBoard.addElement(PawnEnemy, 0, 0); // pawn Enemy
        holeBoard.addElement(pawn, 1, 0);
        holeBoard.addElement(pawn, 0, 1);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 1, 0, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, 0, 0); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 0, 1, Pawn.PAWN_RED);


        //Test coin supérieur droit
        holeBoard.addElement(PawnEnemy, 0, 7); // pawn Enemy
        holeBoard.addElement(pawn, 0, 6);
        holeBoard.addElement(pawn, 1, 7);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 0, 6, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, 0, 7); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel,1, 7, Pawn.PAWN_RED);

        //Test coin inférieur gauche
        holeBoard.addElement(PawnEnemy, 7, 0); // pawn Enemy
        holeBoard.addElement(pawn, 6, 0);
        holeBoard.addElement(pawn, 7, 1);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 6, 0, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, 7, 0); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 7, 1, Pawn.PAWN_RED);

        //Coin Inférieur droit
        holeBoard.addElement(PawnEnemy, 7, 7); // pawn Enemy
        holeBoard.addElement(pawn, 7, 6);
        holeBoard.addElement(pawn, 6, 7);
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 7, 6, Pawn.PAWN_RED);
        holeBoard.addElement(PawnEnemy, 7, 7); // pawn Enemy
        holeBoard.takingPawn(mockStageModel, holeBoard, mockModel, 6, 7, Pawn.PAWN_RED);

        // Verify if deletePawnsTaking was called correctly
        verify(mockStageModel, times(8)).addRedPawnsTaking(PawnEnemy);
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
}
