package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

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

    //1 pour le test du pion qui change de valeur en bout de plateau
    //2 pour le test où il ne reste plus de pions rouges
    //0 pour le jeu normal
    //3 pour le jeu avec les pions bloqués
    public static int testVict = 4;


    public HoleStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (HoleStageModel) gameStageModel;
    }


    @Override
    public void setup() {

        // create the text that displays the player name and put it in 0,0 in the virtual space
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(400, 700);
        stageModel.setPlayerName(text);

        // create the board, in 0,1 in the virtual space
        HoleBoard board = new HoleBoard(150, 0, stageModel);
        // assign the board to the game stage model
        stageModel.setBoard(board);
        stageModel.setHoleRedPawnPot(new HolePawnPot(0, 30, stageModel));
        stageModel.setHoleBluePawnPot(new HolePawnPot(850, 30, stageModel));



        Arrow[] arrows = new Arrow[4];
        for (int i = 0; i < arrows.length; i++) {
            arrows[i] = i == 0 ? new Arrow(Arrow.VERTICAL, stageModel) : i == 1 ? new Arrow(Arrow.HORIZONTAL, stageModel) : i == 2 ? new Arrow(Arrow.MAJOR_DIAGONAL, stageModel) : new Arrow(Arrow.MINOR_DIAGONAL, stageModel);
        }
        stageModel.setArrows(arrows);
        setupArrows(arrows, board);


        if (testVict == 1) {
            Pawn[] bluePawns = new Pawn[2];
            bluePawns[0] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_BLUE, stageModel);
            bluePawns[1] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, stageModel);
            // assign the blue pawns to the game stage model
            stageModel.setBluePawns(bluePawns);

            Pawn[] redPawns = new Pawn[2];
            redPawns[0] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_RED, stageModel);
            redPawns[1] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, stageModel);
            stageModel.setRedPawns(redPawns);


            board.addElement(bluePawns[0], 7, 2);
            board.addElement(bluePawns[1], 2, 3);
            board.setCellReachable(7,2, false);
            board.setCellReachable(2, 3, false);
            bluePawns[0].setRow(7);
            bluePawns[0].setCol(2);
            bluePawns[1].setRow(2);
            bluePawns[1].setCol(3);

            board.addElement(redPawns[0], 2,2);
            board.addElement(redPawns[1], 4,4);
            board.setCellReachable(2,2, false);
            board.setCellReachable(4, 4, false);
            redPawns[0].setRow(2);
            redPawns[0].setCol(2);
            redPawns[1].setRow(4);
            redPawns[1].setCol(4);


        } else if(testVict == 0) {
            setupPawns(board);
        } else if(testVict == 2) {
            Pawn[] bluePawns = new Pawn[3];
            bluePawns[0] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_BLUE, stageModel);
            bluePawns[1] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_BLUE, stageModel);
            bluePawns[2] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_BLUE, stageModel);
            // assign the blue pawns to the game stage model
            stageModel.setBluePawns(bluePawns);

            Pawn[] redPawns = new Pawn[3];

            redPawns[0] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_RED, stageModel);
            redPawns[1] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_RED, stageModel);
            redPawns[2] = new Pawn(Pawn.HORSEMAN, Pawn.PAWN_RED, stageModel);

            stageModel.setRedPawns(redPawns);


            board.addElement(bluePawns[0], 4, 3);
            board.addElement(bluePawns[1], 6, 3);
            board.addElement(bluePawns[2], 6, 7);
            board.setCellReachable(4,3, false);
            board.setCellReachable(6, 3, false);
            board.setCellReachable(6,7, false);
            bluePawns[0].setRow(4);
            bluePawns[0].setCol(3);
            bluePawns[1].setRow(6);
            bluePawns[1].setCol(3);
            bluePawns[2].setRow(6);
            bluePawns[2].setCol(7);



            board.addElement(redPawns[0], 5, 4);
            board.addElement(redPawns[1], 6, 4);
            board.addElement(redPawns[2], 6, 6);
            board.setCellReachable(5,4, false);
            board.setCellReachable(6, 4, false);
            board.setCellReachable(6,6, false);
            redPawns[0].setRow(5);
            redPawns[0].setCol(4);
            redPawns[1].setRow(6);
            redPawns[1].setCol(4);
            redPawns[2].setRow(6);
            redPawns[2].setCol(6);

        } else if (testVict == 3) {
            Pawn[] bluePawns = new Pawn[1];
            bluePawns[0] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, stageModel);
            // assign the blue pawns to the game stage model
            stageModel.setBluePawns(bluePawns);

            Pawn[] redPawns = new Pawn[1];

            redPawns[0] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, stageModel);
            stageModel.setRedPawns(redPawns);


            board.addElement(bluePawns[0], 3, 5);
            board.addElement(redPawns[0], 5, 5);
            board.setCellReachable(3,3, false);
            board.setCellReachable(5, 5, false);

            bluePawns[0].setRow(3);
            bluePawns[0].setCol(5);
            redPawns[0].setRow(5);
            redPawns[0].setCol(5);
        } else if (testVict == 4) {
            Pawn[] bluePawns = new Pawn[2];
            bluePawns[0] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, stageModel);
            bluePawns[1] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_BLUE, stageModel);

            // assign the blue pawns to the game stage model
            stageModel.setBluePawns(bluePawns);

            Pawn[] redPawns = new Pawn[1];

            redPawns[0] = new Pawn(Pawn.INFANTRYMAN, Pawn.PAWN_RED, stageModel);
            stageModel.setRedPawns(redPawns);

            board.addElement(bluePawns[0], 2, 2);
            board.addElement(bluePawns[1], 3, 4);

            board.addElement(redPawns[0], 3, 3);

            board.setCellReachable(2,2, false);
            board.setCellReachable(3, 4, false);
            board.setCellReachable(3, 3, false);


            bluePawns[0].setRow(2);
            bluePawns[0].setCol(2);
            bluePawns[1].setRow(3);
            bluePawns[1].setCol(4);
            redPawns[0].setRow(3);
            redPawns[0].setCol(3);

            System.out.println(bluePawns[0]);
            System.out.println(bluePawns[1]);
            System.out.println(redPawns[0]);
            System.out.println(board.getElement(3,3));
            System.out.println(board.getElement(3,2));
        }
    }

    private void setupArrows(Arrow[] arrows, HoleBoard board){

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
    }

    private void setupPawns(HoleBoard board){
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
                board.setCellReachable(6, i, false);
                board.setCellReachable(0, i , false);

                redPawns[i].setRow(6);
                redPawns[i].setCol(i);
                bluePawns[i].setRow(0);
                bluePawns[i].setCol(i);
            }
            else {
                board.addElement(redPawns[i], 7, 15-i);
                board.addElement(bluePawns[i], 1, 15-i);
                board.setCellReachable(7, 15-i, false);
                board.setCellReachable(1, 15-i , false);

                redPawns[i].setRow(7);
                redPawns[i].setCol(15 - i);
                bluePawns[i].setRow(1);
                bluePawns[i].setCol(15 - i);
            }
        }
    }





}
