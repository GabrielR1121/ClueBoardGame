package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ClientHandeler implements Runnable {
	// Creates the Data input and output streams;
	private DataOutputStream out;
	private DataInputStream in;
	public String inMsg = "";
	public String outMsg = "";

	// Establishes whos turn it is within all the players in the game
	private static int playerTurn;

	public static HashMap<String, Integer[]> characters = new HashMap<String, Integer[]>();

	// Establishes whos turn it is
	public boolean isPlayerTurn = false;

	// Identifier for each player
	public char label;

	/**
	 * Server side.
	 * Methods in this class will be exclusively for the server.
	 * 
	 * This class will establish first communication with the client and send
	 * all the needed information the player needs before starting the game.
	 * 
	 * 
	 * The way this class works is each instance of ClientHandeler will be stored in
	 * an arraylist in order to be used for the communication.
	 */

	/**
	 * As the players connect to the server they will be assigned their own
	 * individual in, out, turn, and label(For identification purposes).
	 * 
	 * @param socket     The socket where all players will be connected to.
	 * @param playerTurn The assigned player turn. This number will not change
	 *                   throughout the code.
	 * @param label      An identifier for each player. Is used as a type of
	 *                   username for the communication.
	 */
	@SuppressWarnings("static-access")
	public ClientHandeler(Socket socket, int playerTurn, char label) {
		try {
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.playerTurn = playerTurn;
			this.label = label;
			Server.clientHandeler.add(this);
			Server.connPlayers++;

		} catch (Exception e) {
			System.out.println("Error in clientHandeler receiving players: " + e.getMessage());
		}
	}

	/**
	 * Client thread.
	 * 
	 * The way this thread works is in a zig-zag pattern where the clienthandeler
	 * sends the first message.
	 * The client receives it and sends in a responce.
	 */
	@Override
	public void run() {
		// Prepares the first outMsg where the server tells the client what are the
		// colors that are availble to play with, also sends the assined turn for this
		// player and if its their turn.
		try {

			for (int i = 0; i < Server.availableColors.size(); i++) {

				if (i < Server.availableColors.size() - 1)
					outMsg += Server.availableColors.get(i) + ",";
				else
					outMsg += Server.availableColors.get(i) + ";";

			}

			outMsg += playerTurn + ";";

			if (playerTurn == 0)
				this.isPlayerTurn = true;

			outMsg += isPlayerTurn + ";";
			out.writeUTF(outMsg);

		} catch (Exception e) {
			System.out.println("Error in ThreadServer file sending first message. Error message: " + e.getMessage());
		}

		try {
			// Will receive the color chosen and the amountofPLayers (for the first Client).
			String inMsg = in.readUTF();

			String[] strMsg = inMsg.split(";");
			// Updates the amount of players to expect in the server and prepares it to be
			// sent to all clients.
			if (playerTurn == 0) {
				Server.amountofPlayers = Integer.parseInt(strMsg[0]);

				// Fills the deck with all the needed cards
				Server.fillCardDeck();

				// Selects 3 cards at random from Character, Weapon and Room and sets them aside
				// for later use.
				Server.secretFolderDistribute();

				// Shuffles the left over cards
				Collections.shuffle(Server.CardDeck);

				// Evenly divides the cards between all players.
				Server.distributeCards();

				// Creates placeholder variables while everyone starts connecting.
				for (int i = 0; i < Server.amountofPlayers; i++) {
					Server.playerColor.add(null);
					Server.playerX.add(0);
					Server.playerY.add(0);
				}

				strMsg = Arrays.copyOfRange(strMsg, 1, strMsg.length);

			}
			// Recieves the color the player chose and asigns them their starting positons
			newPlayer(Server.availableColors.get(Integer.parseInt(strMsg[0])));
			// Stores the color the player chose.
			Server.playerColor.set(playerTurn, Server.availableColors.get(Integer.parseInt(strMsg[0])));

			// After applying all nesecarry changes the color is removed from the list so
			// that when other players join the color will no longer be availble
			Server.availableColors.remove(Integer.parseInt(strMsg[0]));

			// Sends the outMsg to the player with the amountOfPlayers to expect, the color
			// they chose, their assigned starting positions, their distiburted deck and the
			// secret cards set aside.
			out.writeUTF(Server.amountofPlayers + ";" + Server.playerColor.get(playerTurn) + ";"
					+ Server.playerX.get(playerTurn) + ";"
					+ Server.playerY.get(playerTurn) + ";" + Server.playerCards.get(playerTurn) + ";"
					+ Server.secretFolder);

		} catch (Exception e) {
			System.out.println(
					"Error in ThreadServer file receiving amount of players and color player wants. Error message: "
							+ e.getMessage() + "\n" + "Line number: " + e.getStackTrace());
		}

		// Sends every player connected a direct outMsg with everyones starting coords
		// and their color. (if code malfuntions i removed playerTurn)
		sendDM(Server.playerColor + ";" + Server.playerX + ";" + Server.playerY);

		try {
			// Starts the thread loop
			while (true) {
				// Waits for all players to be connected and receives the moving players new X,
				// Y coords and broadcasts it to every other player.

				// The way this communication works is like a type of groupchat where everyone
				// receives my message but there is no need for me to receive my own message
				// again.
				String inMsg = in.readUTF();
				System.out.println("in: " + inMsg);

				String[] strMsg = inMsg.split(";");

				// The outMsg contains the players X (0), Y(1), the current players turn, if the
				// turn
				// has ended(2), the index of the character(3), weapon(4) and room(5)(which will
				// default on
				// -1), the index of the disputed card(6), if a rumor was started(7), who will
				// dispute
				// the rumor among the players(8), and if somebody won the game (9).
				outMsg = strMsg[0] + ";" + strMsg[1] + ";" + Server.currTurn + ";" + strMsg[2] + ";" + strMsg[3] + ";"
						+ strMsg[4] + ";" + strMsg[5] + ";" + strMsg[6] + ";" + strMsg[7] + ";" + strMsg[8] + ";"
						+ strMsg[9];
				System.out.println("SERVER OUT MSG: " + outMsg);

				// Sends the message to every player expect the one sending the messages,
				broadcastMessage(outMsg);

				// Changes turns if the turn ended.
				if (Boolean.parseBoolean(strMsg[2]) == true) {

					Server.currTurn++;
					// Resets the currTurn back to player 1 if a full loop was made.
					if (Server.currTurn == Server.amountofPlayers)
						Server.currTurn = 0;

				}

			} // while
		} catch (Exception e) {
			System.out.println("Server Error: " + e.getMessage());

		}
	}// run

	/**
	 * Receives the outMsg and sends it to everyone playing except the player that
	 * sent it.
	 * They way this works is by using the arraylist of players and sending the
	 * message to everyone who doent habe the same label as the sender.
	 * 
	 * Example: IF there are 3 players labeled A, B and C,. IF it is player B's turn
	 * the message will be sent to players A, and C because their label dosent match
	 * label B
	 * 
	 * @param outMsg The message that will be sent.
	 */
	public void broadcastMessage(String outMsg) {

		for (ClientHandeler clientHandeler : Server.clientHandeler) {
			try {
				if (clientHandeler.label != this.label) {
					Thread.sleep(100);
					clientHandeler.out.writeUTF(outMsg);
					clientHandeler.out.flush();
				}

			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}

	/**
	 * Receives the outMsg and sends a message to ALL players dispite label assiged
	 * to them.
	 * 
	 * @param outMsg The message that will be sent.
	 */
	public void sendDM(String outMsg) {

		for (ClientHandeler clientHandeler : Server.clientHandeler) {

			try {
				if (!Server.playerX.contains(0)) {
					System.out.println("Sending Dm " + outMsg);
					clientHandeler.out.writeUTF(outMsg);
					clientHandeler.out.flush();
					Server.playerX.clear();
					Server.playerY.clear();
					Server.playerColor.clear();
				}

			} catch (Exception e) {
				System.out.println("Error in clientHandeler in Sending Direct Message: " + e.getMessage());
			}
		}

	}

	/**
	 * Receives the color the player chose and assigns the correct starting
	 * positions, from the character hashmap, for that individual color
	 * 
	 * @param color The color the player chose.
	 */
	public void newPlayer(String color) {

		String charArr[];

		switch (color) {
			case "Green":
				charArr = (Arrays.toString(characters.get(color))).split(",");
				getStartingCoordinates(charArr);
				break;
			case "Mustard":
				charArr = (Arrays.toString(characters.get(color))).split(",");
				getStartingCoordinates(charArr);
				break;
			case "Orchid":
				charArr = (Arrays.toString(characters.get(color))).split(",");
				getStartingCoordinates(charArr);
				break;
			case "Peacock":
				charArr = (Arrays.toString(characters.get(color))).split(",");
				getStartingCoordinates(charArr);
				break;
			case "Plum":
				charArr = (Arrays.toString(characters.get(color))).split(",");
				getStartingCoordinates(charArr);
				break;
			case "Scarlett":
				charArr = (Arrays.toString(characters.get(color))).split(",");
				getStartingCoordinates(charArr);
				break;
		}

	}

	/**
	 * Receives the split character coords and stores them in the PlayerX and
	 * PlayerY arralist in order to later be used.
	 * 
	 * @param charArr
	 */
	public static void getStartingCoordinates(String[] charArr) {

		Server.playerX.set(playerTurn, Integer.parseInt((charArr[0].replace('[', ' ')).trim()));
		Server.playerY.set(playerTurn, Integer.parseInt((charArr[1].replace(']', ' ')).trim()));

	}

	/**
	 * Fills the hashmap with every colors starting positions.
	 */
	public static void main() {

		// Adding the starting coordinates for each of the characters.
		characters.put("Green", new Integer[] { 327, 807 });
		characters.put("Mustard", new Integer[] { 775, 263 });
		characters.put("Orchid", new Integer[] { 487, 807 });// to do
		characters.put("Peacock", new Integer[] { 39, 615 });
		characters.put("Plum", new Integer[] { 39, 199 });
		characters.put("Scarlett", new Integer[] { 551, 39 });

	}

}
