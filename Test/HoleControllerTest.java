import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.Player;

import control.HoleController;
import model.Arrow;
import model.HoleBoard;
import model.HoleStageModel;
import model.Pawn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HoleControllerTest {

    @Mock
    private Model mockModel;
    @Mock
    private Player mockPlayer;
    @Mock
    private HoleStageModel mockStageModel;
    @Mock
    private HoleBoard mockBoard;
    @Mock
    private BufferedReader mockBufferedReader;

    @InjectMocks
    private HoleController holeController;

    @Mock
    private Pawn mockPawn;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        holeController.consoleIn = mockBufferedReader;

        when(mockModel.getGameStage()).thenReturn(mockStageModel);
        when(mockStageModel.getBoard()).thenReturn(mockBoard);
        when(mockModel.getCurrentPlayer()).thenReturn(mockPlayer);
    }


    @Test
    void testVerifPawnMove() {
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
    void testVerifMoveCavalier() {
        int[][] validCells = {{2, 1}, {3, 3}};
        Arrow[][] a = new Arrow[8][8];

        // Test mouvement impossible sur une case non fléchés
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        boolean result = holeController.verifMoveCavalier(mockBoard, 1, 1, 2, 2, mockStageModel);
        assertFalse(result);

        // Test mouvement possible sur une case non fléchés
        when(mockBoard.getValidCell(any(Model.class), anyInt(), anyInt())).thenReturn(validCells);
        when(mockStageModel.getBoardArrows1()).thenReturn(a);
        boolean result2 = holeController.verifMoveCavalier(mockBoard, 1, 1, 2, 1, mockStageModel);
        assertTrue(result2);

        // Test cases fléchés
        Arrow mockArrow = mock(Arrow.class);
        Arrow[][] arrows1 = {{mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow},
                {mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow, mockArrow}};
        when(mockStageModel.getBoardArrows1()).thenReturn(arrows1);
        when(mockStageModel.getBoardArrows2()).thenReturn(arrows1);
        when(mockBoard.getValidCell(any(Model.class), eq(mockArrow), eq(mockArrow), anyInt(), anyInt())).thenReturn(validCells);
        boolean result3 = holeController.verifMoveCavalier(mockBoard, 1, 1, 2, 1, mockStageModel);
        assertTrue(result3);
    }

    @Test
    void testChangeInfantrymanToHorseman() {
        when(mockPawn.getRole()).thenReturn(Pawn.INFANTRYMAN);
        when(mockPawn.getColor()).thenReturn(Pawn.PAWN_BLUE);
        holeController.changeInfantrymanToHorseman(mockPawn, 7);
        verify(mockPawn, times(1)).setRole(Pawn.HORSEMAN);
    }
}
