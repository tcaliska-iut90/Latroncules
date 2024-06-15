import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.ControllerHole;
import javafx.application.Application;
import javafx.stage.Stage;
import view.HoleRootPane;
import view.HoleView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Hole extends Application {

    private static final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    private static int mode;

    public static void main(String[] args) {

        Logger.setLevel(Logger.LOGGER_TRACE);
        Logger.setVerbosity(Logger.VERBOSE_HIGH);
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();

        StageFactory.registerModelAndView("hole", "model.HoleStageModel", "view.HoleStageView");
        HoleRootPane rootPane = new HoleRootPane();
        HoleView view = new HoleView(model, stage, rootPane);
        ControllerHole control = new ControllerHole(model,view);
        control.setFirstStageName("hole");
        control.setFirstStageName("hole");

        stage.show();
    }
}