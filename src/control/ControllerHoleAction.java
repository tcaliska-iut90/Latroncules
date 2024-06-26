package control;

import boardifier.control.Controller;
import boardifier.control.ControllerAction;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.HoleView;

/**
 * A basic action controller that only manages menu actions
 * Action events are mostly generated when there are user interactions with widgets like
 * buttons, checkboxes, menus, ...
 */
public class ControllerHoleAction extends ControllerAction implements EventHandler<ActionEvent> {

    // to avoid lots of casts, create an attribute that matches the instance type.
    private HoleView holeView;
    private boolean start = true;

    public ControllerHoleAction(Model model, View view, Controller control) {
        super(model, view, control);
        // take the view parameter ot define a local view attribute with the real instance type, i.e. BasicView.
        holeView = (HoleView) view;

        // set handlers dedicated to menu items
        setMenuHandlers();

        // If needed, set the general handler for widgets that may be included within the scene.
        // In this case, the current gamestage view must be retrieved and casted to the right type
        // in order to have an access to the widgets, and finally use setOnAction(this).
        // For example, assuming the current gamestage view is an instance of MyGameStageView, which
        // creates a Button myButton :
        // ((MyGameStageView)view.getCurrentGameStageView()).getMyButton().setOnAction(this).

    }

    private void setMenuHandlers() {

        // set event handler on the MenuStart item
        holeView.getMenuStart().setOnAction(e -> {
            if (start) {
                holeView.setup();
                setButtonHandler();
                setMenuHandlers();
                start = false;
            }else {
                startGame();
            }
        });
        // set event handler on the MenuIntro item
        holeView.getMenuIntro().setOnAction(e -> {
            control.stopGame();
            holeView.resetView();
            start = true;
            model.getPlayers().clear();
        });
        // set event handler on the MenuQuit item
        holeView.getMenuQuit().setOnAction(e -> {
            System.exit(0);
        });

        holeView.getMenuHelp().setOnAction(e -> {
            control.stopGame();
            holeView.resetView();
            start = true;
            model.getPlayers().clear();
            holeView.help();
        });

    }

    private void setButtonHandler(){
        holeView.getButtonJvJ().setOnAction(e ->{
            holeView.setupChoice();
            setButtonChoiceHandler();
            setMenuHandlers();
        });

        holeView.getButtonJvC().setOnAction(e -> {
            holeView.setupAIChoice(true);
            setAIChoiceHandler(true);
            setMenuHandlers();
        });

        holeView.getButtonCvC().setOnAction(e -> {
            holeView.setupAIChoice(false);
            setAIChoiceHandler(false);
            setMenuHandlers();
        });
    }

    private void setButtonChoiceHandler(){
        holeView.getButtonBluePlayer().setOnAction(e ->{
            model.addHumanPlayer("PlayerBlue");
            model.addHumanPlayer("PlayerRed");
            startGame();
        });
        holeView.getButtonRedPlayer().setOnAction(e -> {
            model.addHumanPlayer("PlayerRed");
            model.addHumanPlayer("PlayerBlue");
            startGame();
        });
    }

    private void setAIChoiceHandler(boolean isJvC){
        if (isJvC) {
            holeView.getButtonJvIA1().setOnAction(e -> {
                model.addHumanPlayer("PlayerBlue");
                model.addComputerPlayer("computer", 1);
                startGame();
            });
            holeView.getButtonJvIA2().setOnAction(e -> {
                model.addHumanPlayer("PlayerBlue");
                model.addComputerPlayer("computer", 2);
                startGame();
            });
        } else {
            holeView.getButtonIA1vIA1().setOnAction(e -> {
                model.addComputerPlayer("computer1", 1);
                model.addComputerPlayer("computer2", 1);
                startGame();
            });
            holeView.getButtonIA1vIA2().setOnAction(e -> {
                model.addComputerPlayer("computer1", 1);
                model.addComputerPlayer("computer2", 2);
                startGame();
            });
            holeView.getButtonIA2vIA2().setOnAction(e -> {
                model.addComputerPlayer("computer1", 2);
                model.addComputerPlayer("computer2", 2);
                startGame();
            });
        }
    }

    private void startGame(){
        try {
            control.startGame();
        }
        catch(GameException err) {
            System.err.println(err.getMessage());
            System.exit(1);
        }
    }

    /**
     * The general handler for action events.
     * this handler should be used if the code to process a particular action event is too long
     * to fit in an arrow function (like with menu items above). In this case, this handler must be
     * associated to a widget w, by calling w.setOnAction(this) (see constructor).
     *
     * @param event An action event generated by a widget of the scene.
     */
    public void handle(ActionEvent event) {

        if (!model.isCaptureActionEvent()) return;
    }
}
