package cardgame.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MatchConnection extends JDialog {

    private Singleton s = Singleton.getInstance();
    private int larghezza = 400;
    private int altezza = 250;
    
    public MatchConnection() {
        setPreferredSize(new Dimension(larghezza, altezza));
        setLocation(440, 150);
        setTitle("Connessione");
        setAlwaysOnTop(true);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JTextField ipField = new JTextField();
        ipField.setPreferredSize(new Dimension(100, 25));
        JTextField portField = new JTextField();
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(100, 25));
        portField.setPreferredSize(new Dimension(100, 25));
        c.gridy = 0;
        add(new JLabel("inserisci l' indirizzo ip del server"),c);
        c.gridy = 1;
        add(ipField,c);
        c.gridy = 2;
        add(new JLabel("inserisci la porta di ascolto del server"),c);
        c.gridy = 3;
        add(portField, c);
        c.gridy = 4;
        add(new JLabel("inserisci il nome utente da visualizzare in partita"), c);
        c.gridy = 5;
        add(usernameField, c);
        c.gridy = 6;
        JButton submit = new JButton("Procedi");
        submit.addActionListener((ActionEvent e) -> {
            if(portField.getText().compareTo("") == 0 |
                ipField.getText().compareTo("") == 0 |
                usernameField.getText().compareTo("") == 0) {
                JOptionPane.showMessageDialog(null, "completa tutti i campi!", "Avviso", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ConnessioneAServer cs = new ConnessioneAServer(ipField.getText(), Integer.parseInt(portField.getText()));
            //ConnessioneAServer cs = new ConnessioneAServer("localhost", 777);
            s.setCS(cs);
            try {
                String reply = cs.risposta("AC" + usernameField.getText());
                String answer = reply.substring(0, 2);
                switch(answer) {
                    case "OK" -> {
                        String id = reply.substring(2, reply.length());
                        s.setId(id);
                        JOptionPane.showMessageDialog(null, "Partita trovata", "Avviso", JOptionPane.INFORMATION_MESSAGE);
                        this.dispose(); 
                        s.getTableGame().waitCards();
                    }

                    case "MX" -> {
                        JOptionPane.showMessageDialog(null, "partita piena", "Avviso", JOptionPane.WARNING_MESSAGE);
                    }

                    case "EU" -> {
                        JOptionPane.showMessageDialog(null, "nome utente già utilizzato", "Avviso", JOptionPane.ERROR_MESSAGE);
                    }
                }
                    
            }
            catch(Exception er) {
                JOptionPane.showMessageDialog(null, "connessione al server fallita!", "Avviso", JOptionPane.ERROR_MESSAGE);
            }
        });
        c.gridx = 4;
        add(submit, c);
        pack();
        setVisible(true);
    }
}
