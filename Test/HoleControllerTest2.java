import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.view.View;
import control.HoleController;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HoleControllerTest2 {

    private HoleStageModel stage;
    private HoleBoard board;
    private HoleController holeController; // Updated to HoleController
    private Model mockModel;
    private View mockView;

    @BeforeEach
    void setUp() {
        stage = mock(HoleStageModel.class);
        board = mock(HoleBoard.class);
        mockModel = mock(Model.class);
        mockView = mock(View.class);
        holeController = mock(HoleController.class);
    }

    @Test
    void testMoveIsOk_withInfantrymanMoveOk() {
        Pawn infantryman = mock(Pawn.class);
        when(infantryman.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(infantryman.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(infantryman.getContainer()).thenReturn(mock(ContainerElement.class));

        Pawn[] bluePawns = new Pawn[]{infantryman};
        when(stage.getBluePawns()).thenReturn(bluePawns);
        when(stage.getRedPawns()).thenReturn(new Pawn[1]);

        when(holeController.verifPawnMove(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        assertTrue(holeController.moveIsOk(stage, 1, 1, board));

        verify(stage, never()).computePartyResult(anyInt());

    }

    @Test
    void testMoveIsOk_withHorsemanMoveOk() {
        Pawn horseman = mock(Pawn.class);
        when(horseman.getRole()).thenReturn(Pawn.HORSEMAN);
        when(horseman.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(horseman.getContainer()).thenReturn(mock(ContainerElement.class));

        Pawn[] bluePawns = new Pawn[]{horseman};
        when(stage.getBluePawns()).thenReturn(bluePawns);
        when(stage.getRedPawns()).thenReturn(new Pawn[1]);

        int[][] validCells = {{2, 2}};
        when(board.getValidCell(any(), anyInt(), anyInt())).thenReturn(validCells);
        when(holeController.verifMoveCavalier(any(), anyInt(), anyInt(), anyInt(), anyInt(), any(), anyInt())).thenReturn(true);

        assertTrue(holeController.moveIsOk(stage, 1, 1, board));
        verify(holeController, times(1)).moveIsOk(any(), anyInt(), anyInt(), any());

        verify(stage, never()).computePartyResult(anyInt());
    }

    @Test
    void testMoveIsOk_noValidMoves() {
        Pawn infantryman = mock(Pawn.class);
        when(infantryman.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(infantryman.getColor()).thenReturn(Pawn.PAWN_BLUE);
        when(infantryman.getContainer()).thenReturn(mock(ContainerElement.class));

        Pawn[] bluePawns = new Pawn[]{infantryman};
        when(stage.getBluePawns()).thenReturn(bluePawns);
        when(stage.getRedPawns()).thenReturn(new Pawn[1]);

        when(holeController.verifPawnMove(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);

        assertFalse(holeController.moveIsOk(stage, 1, 1, board));

        verify(stage, times(1)).computePartyResult(anyInt());
    }

    @Test
    void testMoveIsOk_withRedInfantrymanMoveOk() {
        Pawn infantryman = mock(Pawn.class);
        when(infantryman.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(infantryman.getColor()).thenReturn(Pawn.PAWN_RED);
        when(infantryman.getContainer()).thenReturn(mock(ContainerElement.class));

        Pawn[] redPawns = new Pawn[]{infantryman};
        when(stage.getRedPawns()).thenReturn(redPawns);
        when(stage.getBluePawns()).thenReturn(new Pawn[1]);

        when(holeController.verifPawnMove(any(), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);

        assertTrue(holeController.moveIsOk(stage, 1, 1, board));

        verify(stage, never()).computePartyResult(anyInt());
    }

    @Test
    void testMoveIsOk_withRedHorsemanMoveOk() {
        Pawn horseman = mock(Pawn.class);
        when(horseman.getRole()).thenReturn(Pawn.HORSEMAN);
        when(horseman.getColor()).thenReturn(Pawn.PAWN_RED);
        when(horseman.getContainer()).thenReturn(mock(ContainerElement.class));

        Pawn[] redPawns = new Pawn[]{horseman};
        when(stage.getRedPawns()).thenReturn(redPawns);
        when(stage.getBluePawns()).thenReturn(new Pawn[1]);

        int[][] validCells = {{2, 2}};
        when(board.getValidCell(any(), anyInt(), anyInt())).thenReturn(validCells);
        when(holeController.verifMoveCavalier(any(), anyInt(), anyInt(), anyInt(), anyInt(), any(), anyInt())).thenReturn(true);

        assertTrue(holeController.moveIsOk(stage, 1, 1, board));

        verify(stage, never()).computePartyResult(anyInt());
    }


}
