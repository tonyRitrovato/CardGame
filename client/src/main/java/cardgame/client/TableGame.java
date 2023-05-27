package cardgame.client;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TableGame extends JPanel {

    private Singleton s = Singleton.getInstance();

    private JPanel titleBar = new JPanel();
    public JPanel cardsOnTable = new JPanel();
    public JPanel deck = new JPanel();
    private JPanel consolePane = new JPanel();
    private JTextArea console = new JTextArea();
    private boolean hasQuit = false; 
    
    public TableGame() { 
        s.setTableGame(this);
        setBackground(new Color(50,205,50));
        setLayout(new BorderLayout());

        deck.setOpaque(false);
        cardsOnTable.setOpaque(false);
        titleBar.setOpaque(false);


        JLabel title = new JLabel("Scopa Semplice!");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);

        JButton quit = new JButton("abbandona");
        quit.addActionListener((ActionEvent e) -> {
            try {
                if(s.getCS() != null)
                    s.getCS().risposta("QU" + s.getId());  
            }
            catch(Exception er) {
                er.printStackTrace();
            }
            hasQuit = true;
        });
        quit.setBackground(Color.DARK_GRAY);
        quit.setForeground(Color.WHITE);
        
        JButton searchMatch = new JButton("cerca una partita");
        searchMatch.setBackground(Color.DARK_GRAY);
        searchMatch.setForeground(Color.WHITE);
        searchMatch.addActionListener((ActionEvent e) -> {
            new MatchConnection();
            titleBar.remove(searchMatch);
            titleBar.validate();
            titleBar.repaint();
            titleBar.add(quit);
        });
        Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 20);
        titleBar.setBorder(margin);
        titleBar.add(title);
        titleBar.add(searchMatch);

        console.setEditable(false);
        console.setPreferredSize(new Dimension(250, 500));
        console.setBackground(Color.DARK_GRAY);
        Border border = BorderFactory.createLineBorder(Color.WHITE, 5);
        console.setBorder(border);
        console.setForeground(Color.WHITE);
        console.setFont(new Font("Arial", Font.BOLD, 16));
        consolePane.setOpaque(false);
        consolePane.setBorder(margin);
        consolePane.add(console);

        Border deckMargin = BorderFactory.createEmptyBorder(0,20 , 20, 20);
        deck.setBorder(deckMargin);
        

        add(titleBar, BorderLayout.NORTH);
        add(cardsOnTable, BorderLayout.CENTER);
        add(deck, BorderLayout.SOUTH);
        add(consolePane, BorderLayout.EAST);

        titleBar.revalidate();
    }

    public void waitCards() {
        Timer t = new Timer(3000, null);
        t.addActionListener((ActionEvent e) -> {
            try {
                String cards = s.getCS().risposta("DS" + s.getId());
                SwingUtilities.invokeLater(() -> console.setText("in Attesa di altri giocatori"));
                if(hasQuit == true) {
                    t.stop();
                    SwingUtilities.invokeLater(() -> console.setText("partita Abbandonata"));
                    new MatchConnection();
                    hasQuit = false;
                }
                if (cards.compareTo("WA") != 0) {
                    t.stop();
                    SwingUtilities.invokeLater(() -> console.setText(console.getText() + '\n' + "Partita al completo!  \n \n distribuzione carte"));
                    cards = cards.substring(2, cards.length());
                    ArrayList<String> deckString = new ArrayList<String>();
                    for(int i = 1; i < 11; i++) {
                        String s = cards.substring(0, 2);
                        deckString.add(s);
                        cards = cards.substring(2, cards.length());
                    }
                    for (String s : deckString) {
                        Card c = new Card(s.substring(1, 2), s.substring(0, 1));
                        deck.add(c);
                    }
                    deck.revalidate();
                    deck.repaint();
                    sendCard();
                }
                titleBar.revalidate();
            } catch (Exception er) {
                er.printStackTrace();
            }
        });
        t.start();
    }

    private void sendCard() {
        Timer t = new Timer(1000, null);
        s.setTimer(t);
        t.addActionListener((ActionEvent e) -> {
            try {
                String turn = s.getCS().risposta("TU" + s.getId());
                switch(turn) {
                    case "PI" -> {
                        JOptionPane.showMessageDialog(null, "qualcuno ha abbandonato la partita, cercane un'altra", "Avviso", JOptionPane.ERROR_MESSAGE);
                        deck.removeAll();
                        deck.revalidate();
                        cardsOnTable.removeAll();
                        cardsOnTable.revalidate();
                        console.setText("");
                        new MatchConnection();
                        t.stop();
                    }

                    case "TN" -> {
                        s.setTurn(false);
                        String tableCard = s.getCS().risposta("ST" + s.getId());
                        tableCard = tableCard.substring(2, tableCard.length());
                        cardsOnTable.removeAll();
                        if(tableCard.length() > 0) {
                            for(int i = 0; i < tableCard.length(); i += 2) {
                                Card c = new Card(tableCard.substring(i+1, i+2), tableCard.substring(i, i+1));
                                c.setEnabled(false);
                                c.removeMouseListener();
                                cardsOnTable.add(c);
                                cardsOnTable.revalidate();
                                cardsOnTable.repaint();
                            }
                        }
                        else {
                            cardsOnTable.removeAll();
                            cardsOnTable.repaint();
                        }
                    }

                    case "TX" -> {
                        s.setTurn(true);
                        console.setText(console.getText() + '\n' + "\n è il tuo turno!");
                        t.stop();
                        /*In questo punto richiedi il feed delle carte in tavola e lo gestisci */
                        cardsOnTable.removeAll();
                        cardsOnTable.repaint();
                        String tableCard = s.getCS().risposta("ST" + s.getId());
                        tableCard = tableCard.substring(2, tableCard.length());
                        cardsOnTable.removeAll();
                        if(tableCard.length() > 0) {
                            for(int i = 0; i < tableCard.length(); i += 2) {
                                Card c = new Card(tableCard.substring(i+1, i+2), tableCard.substring(i, i+1));
                                c.setEnabled(false);
                                c.removeMouseListener();
                                cardsOnTable.add(c);
                                cardsOnTable.revalidate();
                                cardsOnTable.repaint();
                            }
                        }
                        else {
                            cardsOnTable.removeAll();
                            cardsOnTable.repaint();
                        }
                        win();
                       if(s.getWin()) {
                        t.stop();
                        System.out.println("timer fermo");
                       }
                    }
                }
            }
            catch(Exception er) {
                er.printStackTrace();
            }
        });
        t.start();

        /*  */
        }

    private void win() {
        s.setWin(false);
        System.out.println(deck.getComponentCount());
        if(deck.getComponentCount() <= 1) {
            System.out.println("Siamo dentro!");
            Timer tScore = new Timer(3000, null);
            tScore.addActionListener((ActionEvent e) -> {
                try {
                    String score = s.getCS().risposta("VP" + s.getId());
                    String reply = score.substring(0, 2);
                    switch (reply) {
                        case "PV" -> {
                            JOptionPane.showMessageDialog(null, "partita vinta!" +
                            '\n' + "punteggio: " + score.substring(2, score.length()), "fine partita", JOptionPane.INFORMATION_MESSAGE);
                            tScore.stop();
                            s.setWin(true);
                            deck.removeAll();
                            deck.revalidate();
                            console.setText("");
                            cardsOnTable.removeAll();
                            cardsOnTable.revalidate();
                            s.geTimer().stop();
                            s.getCS().risposta("QU" + s.getId()); 
                            new MatchConnection();
                        }
        
                        case "PP" -> {
                            JOptionPane.showMessageDialog(null, "partita persa!" +
                            '\n' + "punteggio: " + score.substring(2, score.length()), "fine partita", JOptionPane.INFORMATION_MESSAGE);
                            tScore.stop();
                            s.setWin(true);
                            deck.removeAll();
                            deck.revalidate();
                            console.setText("");
                            cardsOnTable.removeAll();
                            cardsOnTable.revalidate();
                            s.geTimer().stop();
                            s.getCS().risposta("QU" + s.getId()); 
                            new MatchConnection();
                        }
        
                        case "IC" -> { s.setWin(false); }
                        }
                    }
                    catch(Exception er) {
                        er.printStackTrace();
                    }
                });
                tScore.start();
            }
        }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, new Color(0,100,0), 0, h, new Color(255, 255, 255, 0));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }


}


