package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;
import boardifier.view.ConsoleColor;

/**
 * HoleStageFactory must create the game elements that are defined in HoleStageModel
 * WARNING: it just creates the game element and NOT their look, which is done in HoleStageView.
 *
 * If there must be a precise position in the display for the look of a game element, then this element must be created
 * with that position in the virtual space and MUST NOT be placed in a container element. Indeed, for such
 * elements, the position in their virtual space will match the position on the display. For example, in the following,
 * the black pot is placed in 18,0. When displayed on screen, the top-left character of the black pot will be effectively
 * placed at column 18 and row 0.
 *
 * Otherwise, game elements must be put in a container and it will be the look of the container that will manage
 * the position of element looks on the display. For example, pawns are put in a ContainerElement. Thus, their virtual space is
 * in fact the virtual space of the container and their location in that space in managed by boardifier, depending of the
 * look of the container.
 *
 */
public class HoleStageFactory extends StageElementsFactory {
    private HoleStageModel stageModel;

    //false pour le jeu normal
    public static boolean testVict = true;


    public HoleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (HoleStageModel) gameStageModel;
    }


    @Override
    public void setup() {

        // create the text that displays the player name and put it in 0,0 in the virtual space
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(0,0);
        stageModel.setPlayerName(text);

        // create the board, in 0,1 in the virtual space
        HoleBoard board = new HoleBoard(0, 1, stageModel);
        // assign the board to the game stage model
        stageModel.setBoard(board);




        /* create the pawns
            NB: their coordinates are by default 0,0 but since they are put
            within the pots, their real coordinates will be computed by the view
         */






        Arrow[] arrows = new Arrow[4];
        for (int i = 0; i < arrows.length; i++) {
            arrows[i] = i == 0 ? new Arrow(Arrow.VERTICAL, stageModel) : i == 1 ? new Arrow(Arrow.HORIZONTAL, stageModel) : i == 2 ? new Arrow(Arrow.MAJOR_DIAGONAL, stageModel) : new Arrow(Arrow.MINOR_DIAGONAL, stageModel);
        }
        stageModel.setArrows(arrows);



        if(testVict){
            Pawn[] bluePawns = new Pawn[2];
            bluePawns[0] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_BLUE, stageModel);
            bluePawns[1] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, stageModel);
            // assign the blue pawns to the game stage model
            stageModel.setBluePawns(bluePawns);

            Pawn[] redPawns = new Pawn[2];
            redPawns[0] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_RED, stageModel);
            redPawns[1] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, stageModel);
            stageModel.setRedPawns(redPawns);



            board.addElement(bluePawns[0], 6, 6);
            board.addElement(bluePawns[1], 6, 5);


            board.addElement(redPawns[0], 1, 4);
            board.addElement(redPawns[1], 1, 3);


        }else {
            Pawn[] bluePawns = new Pawn[16];
            for(int i=0;i<16;i++) {
                if (i%2 == 0)
                    bluePawns[i] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_BLUE, stageModel);
                else
                    bluePawns[i] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, stageModel);
            }
            // assign the blue pawns to the game stage model
            stageModel.setBluePawns(bluePawns);

            Pawn[] redPawns = new Pawn[16];
            for(int i=0;i<16;i++) {
                if (i%2 == 0)
                    redPawns[i] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_RED, stageModel);
                else
                    redPawns[i] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, stageModel);

            }
            stageModel.setRedPawns(redPawns);

            for (int i = 0; i < bluePawns.length; i++) {
                if (i<8){
                    board.addElement(redPawns[i], 6, i);
                    board.addElement(bluePawns[i], 0, i);
                }
                else {
                    board.addElement(redPawns[i], 7, 15-i);
                    board.addElement(bluePawns[i], 1, 15-i);
                }
            }
        }



        Arrow[][] boardArrow1 = new Arrow[8][8];
        Arrow[][] boardArrow2 = new Arrow[8][8];
        for (int i = 0; i < 16; i+=2) {
            if (i <8) {
                boardArrow1[0][i] = arrows[2];
                boardArrow2[0][i] = arrows[3];
                boardArrow1[6][i] = arrows[2];
                boardArrow2[6][i] = arrows[3];
                if (i<4) {
                    boardArrow1[2][i] = arrows[3];
                    boardArrow2[2][i] = arrows[1];
                    boardArrow1[4][i] = arrows[3];
                    boardArrow2[4][i] = arrows[0];
                }
                else {
                    boardArrow1[2][i] = arrows[1];
                    boardArrow2[2][i] = arrows[2];
                    boardArrow1[4][i] = arrows[0];
                    boardArrow2[4][i] = arrows[2];
                }
            }
            else {
                boardArrow1[1][i-7] = arrows[2];
                boardArrow2[1][i-7] = arrows[3];
                boardArrow1[7][i-7] = arrows[2];
                boardArrow2[7][i-7] = arrows[3];

                if (i<12){
                    boardArrow1[3][i-7] = arrows[0];
                    boardArrow2[3][i-7] = arrows[2];
                    boardArrow1[5][i-7] = arrows[1];
                    boardArrow2[5][i-7] = arrows[2];
                }
                else {
                    boardArrow1[3][i-7] = arrows[0];
                    boardArrow2[3][i-7] = arrows[3];
                    boardArrow1[5][i-7] = arrows[1];
                    boardArrow2[5][i-7] = arrows[3];
                }
            }
        }
        stageModel.setBoardArrows1(boardArrow1);
        stageModel.setBoardArrows2(boardArrow2);

        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                System.out.println(board.getElement(i, j)+ " " + i + " " + j);
            }
        }

        /* Test Affichage
        for (int i = 0; i < boardArrow1.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < boardArrow1[i].length; j++) {
                if (boardArrow1[i][j] != null)
                    System.out.print(boardArrow1[i][j].getDirection() + " " + boardArrow2[i][j].getDirection() + " |");
                else
                    System.out.print("|     |");
            }
            System.out.println();
        }*/


    }



}
