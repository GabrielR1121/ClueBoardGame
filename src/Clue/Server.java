package Clue;

import javax.swing.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.net.ServerSocket;
import java.util.List;

public class Server {

    private final int port = 2027;
    // Could change if pop-up added to ask amount of players.
    private final int numberOfConnections = 6;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private boolean isPlayerTurn = false;

    public static ArrayList<String> availableColors = new ArrayList<String>() {

        {
            add("Mustard");
            add("Orchid");
            add("Green");
            add("Plum");
            add("Peacock");
            add("Scarlett");

        }

    };
    public static StringBuilder sb = new StringBuilder();

    // Not in UML yet
    public static int turn;

    // Amount of players to connect
    int amountofPlayer = 4;

    // Find out where they go.
    // int playersX[] = new int[amountofPlayer];
    // int playersY[] = new int[amountofPlayer];

    // static int x;
    // static int y;

    int amountCards = 21;
    Random rand = new Random();

    // try to auto generate.
    private ArrayList<Integer> secretFolder = new ArrayList<Integer>();
    List<List<Integer>> playerCards = new ArrayList<List<Integer>>();

    private ArrayList<Integer> CardDeck = new ArrayList<Integer>();

    private boolean isEliminated = false;

    ServerSocket server;

    public static void main(String[] args) {
        Server servidor = new Server();
        servidor.startServer();
    }

    // Server constructor
    // Initalizes the panel for the jFrome with the determined Width and Height.
    // Adds the action Listeners
    // Starts the server
    Server() {

        // Fills and Refills the Deck
        fillCardDeck();

        // Selects 3 cards at random from Character, Weapon and Room and sets them aside
        secretFolderDistribute();

        // Shuffles the remainning
        Collections.shuffle(CardDeck);

        distributeCards();

        ThreadServer.main();
    }

    // Opens the sockets and waits for a connection from the client.
    // Sets the program to running and allows the timer thread to start
    // Eventually a pop up with the color will be displayed here.
    public void startServer() {

        System.out.println("Starting server... \n-------------");

        try {

            server = new ServerSocket(port, numberOfConnections);

            while (true) {

                System.out.println("Awaiting connection...");
                Socket client = server.accept();
                users.add(client);

                // Connection went through
                // System.out.println(users.toString());

                Runnable run = new ThreadServer(client, users, turn++);
                Thread hilo = new Thread(run);
                hilo.start();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Starts the Final Rumor
    public void finalRumor() {

    }

    public void fillCardDeck() {
        CardDeck.clear();
        for (int i = 0; i < amountCards; i++)
            CardDeck.add(i);
    }

    public void secretFolderDistribute() {
        secretFolder.clear();

        int character = rand.nextInt(6);
        secretFolder.add(character);

        int room = rand.nextInt(14 - 6 + 1) + 6;
        secretFolder.add(room);

        int weapon = rand.nextInt(20 - 15 + 1) + 15;
        secretFolder.add(weapon);

        CardDeck.remove(weapon);
        CardDeck.remove(room);
        CardDeck.remove(character);
        amountCards -= 3;
        // System.out.println("Secret Card: " + secretFolder.toString());

    }

    public void distributeCards() {

        playerCards.clear();
        int distribute = ((amountCards) / amountofPlayer);
        int rem = amountCards % amountofPlayer;

        int start = 0;
        int end = distribute;

        for (int i = 1; i <= amountofPlayer; i++) {

            int extra = (i <= rem) ? 1 : 0;

            playerCards.add(CardDeck.subList(start, end));

            start = end;
            end += distribute + extra;

        }

        // System.out.println(list.toString());

    }

    // // Checks to see if players are not on top of each other.
    // public void checkPlayer(int xCoord, int yCoord) {

    // if(checkDuplicateCoordinate(playerX, index) &&
    // checkDuplicateCoordinate(playerY, 0)
    // System.out.println("ON TOP");

    // }

    // private static boolean checkDuplicateCoordinate(int[] playerArray, int
    // coordinateIdx) {
    // for (int i = 0; i < playerArray.length; i++) {
    // if (coordinateIdx != i) {
    // if (playerArray[coordinateIdx] == playerArray[i])
    // return true;
    // }
    // return false;
    // }

    // }

    // Checks to see if player has remaining moves.
    public void checkMoves(int roll) {

    }

    // Displays GameOver screen when a player wins or all players get eliminated.
    // Will display winning cards and player who one if any.
    public void gameOver() {

    }

}