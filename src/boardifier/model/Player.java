package boardifier.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public final static int HUMAN = 1;
    public final static int COMPUTER = 2;

    private int computerType;

    protected int type;
    protected String name;
    /**
     * The list of keys currently pressed but not yet realeased
     */
    protected List<String> keyPressed;

    private Player(int type, String name) {
         this.type = type;
         this.name = name;
         this.keyPressed = new ArrayList<>();
    }

    private Player(int type, String name, int computerType) {
        this.type = type;
        this.name = name;
        this.computerType = computerType;
        this.keyPressed = new ArrayList<>();
    }

    public void reset() {
        keyPressed.clear();
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastKeyPressed() {
        if (keyPressed.isEmpty()) return "";
        return keyPressed.get(keyPressed.size()-1);
    }
    public void addKeyPressed(String lastKey) {
        if (!this.keyPressed.contains(lastKey)) {
            this.keyPressed.add(lastKey);
        }
    }
    public void removeKeyPressed(String lastKey) {
        this.keyPressed.remove(lastKey);
    }
    public boolean isKeyPressed(String key) {
        return keyPressed.contains(key);
    }

    public static Player createHumanPlayer(String name) {
        return new Player(HUMAN, name);
    }
    public static Player createComputerPlayer(String name) {
        return new Player(COMPUTER, name);
    }
    public static Player createComputerPlayer(String name, int computerType) {
        return new Player(COMPUTER, name, computerType);
    }


    public int getComputerType() {
        return computerType;
    }

    public int getColor() {
        if (this.name.equals("PlayerBlue") || this.name.equals("computer1")) {
            return 0;
        } else {
            return 1;
        }
    }

}
