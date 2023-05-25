package cardgame.client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App extends JFrame
{

    private int larghezza = 1366;
    private int altezza = 768;
    private Singleton s = Singleton.getInstance();
    
    public App() {
        setSize(larghezza, altezza);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setContentPane(new TableGame());
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if(s.getCS() != null)
                        s.getCS().risposta("QU" + s.getId());  
                }
                catch(Exception er) {
                    er.printStackTrace();
                }
            }
        });
    }
    public static void main( String[] args )
    {
        new App();
    }
}