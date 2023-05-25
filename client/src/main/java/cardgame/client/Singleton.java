package cardgame.client;

import javax.swing.Timer;

public class Singleton {

    private static Singleton instance; 
    private String id;
    private ConnessioneAServer cs;
    private TableGame t;
    private boolean turn;
    private Timer timer;
    private boolean win;

    private Singleton() { }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCS(ConnessioneAServer cs) {
        this.cs = cs;
    }

    public ConnessioneAServer getCS() {
        return cs;
    }

    public void setTableGame(TableGame t) {
        this.t = t;
    }

    public TableGame getTableGame() {
        return t;
    }

    public void setTurn(Boolean turn) {
        this.turn = turn;
    }

    public boolean getTurn() {
        return turn;
    }


    public void setTimer(Timer t) {
        timer = t;
    }

    public Timer geTimer() {
        return timer;
    }

    public boolean getWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }
}

