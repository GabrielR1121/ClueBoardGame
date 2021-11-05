package Clue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JPanel;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.net.ServerSocket;

public class Server extends JPanel implements ActionListener {

    private final int port = 2027;
    // Could change if pop-up added to ask amount of players.
    private final int numberOfConnections = 6;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private boolean isPlayerTurn = false;
    private int changeTurn = 0;

    // Not in UML yet
    private int turn;
    static final int SCREEN_WIDTH = 842;
    static final int SCREEN_HEIGHT = 872;
    static final int UNIT_SIZE = 32;
    int align = 6;
    char direction = 'p';
    static final int DELAY = 75;
    int amountofPlayer = 3;
    int playersX[] = new int[amountofPlayer];
    int playersY[] = new int[amountofPlayer];
    Timer timer;

    boolean running = false;

    // try to auto generate.
    private ArrayList<Integer> P1 = new ArrayList<Integer>();
    private ArrayList<Integer> P2 = new ArrayList<Integer>();
    private ArrayList<Integer> P3 = new ArrayList<Integer>();

    private enum colors {
    };

    private int secretFolder[] = new int[3];

    private ArrayList<Integer> CardDeck = new ArrayList<Integer>();

    private boolean isEliminated = false;

    ServerSocket server;

    // Server constructor
    // Initalizes the panel for the jFrome with the determined Width and Height.
    // Adds the action Listeners
    // Starts the server
    Server() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        for (int i = 0; i < 24; i++)
            CardDeck.add(i);

        Collections.shuffle(CardDeck);

        distributeCards();

        startServer();
    }

    // Opens the sockets and waits for a connection from the client.
    // Sets the program to running and allows the timer thread to start
    // Eventually a pop up with the color will be displayed here.
    public void startServer() {
        try {

            server = new ServerSocket(port, numberOfConnections);

            // Test methods need clean up
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();

            System.out.println("Pick your color");

            while (true) {
                break;
                // Socket client = server.accept();

                // users.add(client);

                // turn = changeTurn++;

                // Runnable run = new HiloServidor(cliente,usuarios,xo,G);
                // Thread hilo = new Thread(run);
                // hilo.start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initializes the paint for the whole game
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // Draws all the componets of the game onto the gameframe which include:
    // * GameBoard
    // * Players / Player Movement.
    public void draw(Graphics g) {

        Image img = Toolkit.getDefaultToolkit().getImage(
                "C:\\Users\\grgar\\OneDrive\\The backup folder\\School\\UPRB folder\\Fourth Year\\Semester 1\\Data Communication\\C\\f0bf3e514726248e0d0b8544e9527566.jpg");

        g.drawImage(img, 0, 0, null);

        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE + align, 0, i * UNIT_SIZE + align, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE + align, SCREEN_WIDTH, i * UNIT_SIZE + align);
        }

        // for (int i = 0; i < amountofPlayer; i++) {
        // g.setColor(colors[i]);
        // g.fillOval(playersX[i] + align + 1, playersY[i] + align, UNIT_SIZE,
        // UNIT_SIZE);

        // }

        // Redraws the board each time something happens.
        repaint();

    }

    // Starts the Final Rumor
    public void finalRumor() {

    }

    // Uses the global variable direction in order to move a player N,E,S,W with
    // respect to the Unit_Size.
    // After player moves direction is switched to s in order to prevent cosntant
    // movement.
    // For now print (X,Y) of the player.
    public void move() {

        switch (direction) {

        case 'U':
            for (int i = 0; i < amountofPlayer; i++) {
                playersY[i] = playersY[i] - UNIT_SIZE;
                System.out.println(
                        "Player " + (i + 1) + " X: " + (playersX[i] + align + 1) + " Y: " + (playersY[i] + align + 1));
            }

            direction = 's';

            break;

        case 'D':
            for (int i = 0; i < amountofPlayer; i++) {
                playersY[i] = playersY[i] + UNIT_SIZE;
                System.out.println(
                        "Player " + (i + 1) + " X: " + (playersX[i] + align + 1) + " Y: " + (playersY[i] + align + 1));
            }
            direction = 's';
            break;

        case 'L':
            for (int i = 0; i < amountofPlayer; i++) {
                playersX[i] = playersX[i] - UNIT_SIZE;
                System.out.println(
                        "Player " + (i + 1) + " X: " + (playersX[i] + align + 1) + " Y: " + (playersY[i] + align + 1));
            }
            direction = 's';
            break;

        case 'R':
            for (int i = 0; i < amountofPlayer; i++) {
                playersX[i] = playersX[i] + UNIT_SIZE;
                System.out.println(
                        "Player " + (i + 1) + " X: " + (playersX[i] + align + 1) + " Y: " + (playersY[i] + align + 1));
            }
            direction = 's';
            break;
        }
    }

    //
    public void distributeCards() {
        int distribute = (23 - 2) / amountofPlayer;
        try {
            for (int i = 1; i <= amountofPlayer; i++)
                for (int j = 0; j < distribute; j++) {
                    if (i == 1)
                        P1.add(CardDeck.get(j));
                    else if (i == 2)
                        P2.add(CardDeck.get(j));
                    else if (i == 3)
                        P3.add(CardDeck.get(j));

                    CardDeck.remove(j);
                }
        } catch (Exception e) {
        }

        System.out.println(P1.toString());
        System.out.println(P2.toString());
        System.out.println(P3.toString());

    }

    // Sets all players in their respective start positions.
    public void newPlayer() {

    }

    // Checks to see if players are not on top of each other.
    public void checkPlayer() {

    }

    // Checks to see if players are within the game bounds
    public void checkBounds() {

    }

    // Checks to see if players are entering a room through the door.
    public void checkRoom() {

    }

    // Checks to see if player has remaining moves.
    public void checkMoves(int roll) {

    }

    // Displays GameOver screen when a player wins or all players get eliminated.
    // Will display winning cards and player who one if any.
    public void gameOver() {

    }

    // activates when an action is preformed in order to run previus methods.
    public void actionPerformed(ActionEvent e) {
        if (running && isPlayerTurn) {
            move();
            checkPlayer();
            checkBounds();
            checkRoom();
        }
        repaint();
    }

    // Checks to see if a key has been pressed and changes direction based on arrow
    // keys.
    public class MyKeyAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:
                direction = 'L';
                break;

            case KeyEvent.VK_RIGHT:
                direction = 'R';
                break;

            case KeyEvent.VK_UP:
                direction = 'U';
                break;

            case KeyEvent.VK_DOWN:
                direction = 'D';
                break;
            }
        }
    }
}