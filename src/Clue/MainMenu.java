package Clue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class MainMenu extends JPanel {

    // Gotta work on moving the Color enum alone.

    // Starts for every player to either start a game or join one.
    // This main menu must contain every color available for the user to choose.
    // Eliminates the chosen colors for the next client...

    // Waits for every player to choose a color and join the game,
    // then every player gets the same gameboard with every location for
    // their chosen color.
    public MainMenu() {

        // SIDE NOTE: WHEN CHOOSING A COLOR, WE MUST NOT REMOVE THE ELEMENT TO KEEP THE
        // INDEX
        // WE CAN ALSO SET THE VALUE TO NULL.

        // Players chooses colors. Player then chooses startGame or joinGame
        // Waits for the rest of the players.
        // Esto era el while(colorIdx != -1){
        // break;
        // }

        // En client se elimino colorIdx como local y paso a ser global.
        // Lo otro que se hizo en client era para evitar el re-write,
        // se elimino el ultimo outMsg que era lo que estaba rew-writing.

        // Por ultimo, en el constructor de Build esta el mismo codigo que
        // contienen las ultimas lineas de esta clase..

        JFrame frame = new JFrame();

        JButton startGameBTN = new JButton("Start Game");

        JButton joinGameBTN = new JButton("Join Game");

        JButton greenColor = new JButton("Green");
        greenColor.setBackground(new Color(0, 125, 0));

        JButton plumColor = new JButton("Plum");
        plumColor.setBackground(new Color(142, 69, 133));

        JButton scarlettColor = new JButton("Scarlett");
        scarlettColor.setBackground(new Color(255, 36, 0));

        JButton orchidColor = new JButton("Orchid");
        orchidColor.setBackground(new Color(218, 112, 214));

        JButton peacockColor = new JButton("Peacock");
        peacockColor.setBackground(new Color(51, 161, 201));

        JButton mustardColor = new JButton("Mustard");
        mustardColor.setBackground(new Color(255, 204, 102));

        startGameBTN.setSize(350, 350);

        startGameBTN.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                Client client = new Client();
                Thread thread = new Thread(client);
                thread.start();
                startGameBTN.setVisible(false);
                joinGameBTN.setVisible(false);

                // Luego de presionar los botones de los colores, espera a que entre el resto de
                // jugadores
                //

            }

        });

        joinGameBTN.setSize(350, 350);

        joinGameBTN.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                Client client = new Client();
                Thread thread = new Thread(client);
                thread.start();

            }

        });

        greenColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Client.colorIdx = 0;
                greenColor.setVisible(false);
                plumColor.setVisible(false);
                orchidColor.setVisible(false);
                mustardColor.setVisible(false);
                greenColor.setVisible(false);
                scarlettColor.setVisible(false);
                peacockColor.setVisible(false);

            }

        });

        plumColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Client.colorIdx = 4;
                greenColor.setVisible(false);
                plumColor.setVisible(false);
                orchidColor.setVisible(false);
                mustardColor.setVisible(false);
                greenColor.setVisible(false);
                scarlettColor.setVisible(false);
                peacockColor.setVisible(false);

            }

        });

        scarlettColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Client.colorIdx = 5;
                greenColor.setVisible(false);
                plumColor.setVisible(false);
                orchidColor.setVisible(false);
                mustardColor.setVisible(false);
                greenColor.setVisible(false);
                scarlettColor.setVisible(false);
                peacockColor.setVisible(false);

            }

        });

        orchidColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Client.colorIdx = 2;
                greenColor.setVisible(false);
                plumColor.setVisible(false);
                orchidColor.setVisible(false);
                mustardColor.setVisible(false);
                greenColor.setVisible(false);
                scarlettColor.setVisible(false);
                peacockColor.setVisible(false);

            }

        });

        mustardColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Client.colorIdx = 1;
                greenColor.setVisible(false);
                plumColor.setVisible(false);
                orchidColor.setVisible(false);
                mustardColor.setVisible(false);
                greenColor.setVisible(false);
                scarlettColor.setVisible(false);
                peacockColor.setVisible(false);

            }

        });

        peacockColor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // Client.colorIdx = 3;
                greenColor.setVisible(false);
                plumColor.setVisible(false);
                orchidColor.setVisible(false);
                mustardColor.setVisible(false);
                greenColor.setVisible(false);
                scarlettColor.setVisible(false);
                peacockColor.setVisible(false);

            }

        });

        this.add(greenColor);
        this.add(plumColor);
        this.add(scarlettColor);
        this.add(orchidColor);
        this.add(mustardColor);
        this.add(peacockColor);
        this.add(startGameBTN);
        this.add(joinGameBTN);

        this.setPreferredSize(new Dimension(500, 500));
        this.setFocusable(true);

        System.out.println("Staring gameframe...");
        frame.add(this);
        frame.setTitle("Clue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

}