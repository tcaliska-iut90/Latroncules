import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import control.HoleController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HoleConsole {

    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws IOException {

        Logger.setLevel(Logger.LOGGER_TRACE);
        Logger.setVerbosity(Logger.VERBOSE_HIGH);
        int mode = 2;
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        }
        Model model = new Model();
        if (mode == 0) {
            System.out.println("Qui commence ?");
            System.out.println("(1)PlayerRed");
            System.out.println("(2)PlayerBlue");
            String line = bufferedReader.readLine();
            if (Integer.parseInt(line) == 1){
                model.addHumanPlayer("PlayerRed");
                model.addHumanPlayer("PlayerBlue");
            }else {
                model.addHumanPlayer("PlayerBlue");
                model.addHumanPlayer("PlayerRed");
            }


        }
        else if (mode == 1) {
            model.addHumanPlayer("PlayerBlue");
            model.addComputerPlayer("computer");
        }
        else if (mode == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }

        StageFactory.registerModelAndView("hole", "model.HoleStageModel", "view.HoleStageView");
        View holeView = new View(model);
        HoleController control = new HoleController(model,holeView);
        control.setFirstStageName("hole");
        try {
            control.startGame();
            control.stageLoop();
        }
        catch(GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}