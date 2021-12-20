package Clue;

import java.net.Socket;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.net.ServerSocket;
import java.util.List;

// Class for the server. Implements threads, and Java Sockets. 
public class Server {

    private final int port = 2027;
    private final int numberOfConnections = 6;
    public static ArrayList<Socket> users = new ArrayList<Socket>();
    public volatile static int amountofPlayers = 0;
    public static int connPlayers = 0;
    public volatile static int currTurn = 0;
    public static CopyOnWriteArrayList<Integer> playerX = new CopyOnWriteArrayList<Integer>();
    public static CopyOnWriteArrayList<Integer> playerY = new CopyOnWriteArrayList<Integer>();
    public static CopyOnWriteArrayList<String> playerColor = new CopyOnWriteArrayList<String>();

    public char label = 'A';

    // ArrayList for the Colors in order. Each client receives the updated ArrayList.
    @SuppressWarnings("serial")
    public static ArrayList<String> availableColors = new ArrayList<String>() {

        {
            add("Green");
            add("Mustard");
            add("Orchid");
            add("Peacock");
            add("Plum");
            add("Scarlett");

        }

    };

    //ArrayList for all the clients.
    public static ArrayList<ClientHandeler> clientHandeler = new ArrayList<>();

    public static int turn = 0;
    static int amountCards = 21;
    static Random rand = new Random();
    public static ArrayList<Integer> secretFolder = new ArrayList<Integer>();
    public static List<List<Integer>> playerCards = new ArrayList<List<Integer>>();
    public static ArrayList<Integer> CardDeck = new ArrayList<Integer>();

    ServerSocket server;

    public static void main(String[] args) {
        Server servidor = new Server();
        servidor.startServer();

    }

    // Server constructor
    // Initalizes the panel for the JFrame with the determined Width and Height.
    // Adds the action Listeners
    // Starts the server
    Server() {

        ClientHandeler.main();
    }

    // Opens the socket and waits for a connection from the client.
    // Sets the program to running. 
    public void startServer() {

        System.out.println("Starting server... \n-------------");

        try {

            server = new ServerSocket(port, numberOfConnections);

            while (true) {

                System.out.println("Awaiting connection...");
                Socket client = server.accept();
                ClientHandeler clienthandeler = new ClientHandeler(client, turn++, label++);

                Thread thread = new Thread(clienthandeler);
                thread.start();

            } // while

        } catch (Exception e) {
            e.printStackTrace();
        }

    }// startServer

    //Fills the card deck with the amount of cards.
    public static void fillCardDeck() {
        CardDeck.clear();
        for (int i = 0; i < amountCards; i++)
            CardDeck.add(i);
    }

    // Distributes the three cards needed for the secret folder. Containing one of each
    // category (Characters, Weapons, Rooms).
    public static void secretFolderDistribute() {
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

    }

    //Distributes the cards across every player in the game.
    public static void distributeCards() {

        playerCards.clear();
        int distribute = ((amountCards) / amountofPlayers);
        int rem = amountCards % amountofPlayers;

        int start = 0;
        int end = distribute;

        /* To distribute cards correctly,
        * the remainder of amountCards % amountofPlayers is used.
        * This happens because there will always be 18 cards left to distribute
        * since there is three cards removed to fill the secret folder.
        */
        for (int i = 1; i <= amountofPlayers; i++) {

            int extra = (i <= rem) ? 1 : 0;

            playerCards.add(CardDeck.subList(start, end));

            start = end;
            end += distribute + extra;

        }

    }// distributeCards


}// class