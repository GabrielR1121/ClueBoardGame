package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Client side.
 * Methods in this class will be exclusively for the player and nobody else.
 * 
 * This class will establish communication with the server and retrieve
 * all the nesecary information the player needs before starting the game.
 */
public class Client implements Runnable {
	// Width and Height of the gameboard
	public final int SCREEN_WIDTH = 842;
	public final int SCREEN_HEIGHT = 872;

	// Establishes the movement of all pieces (each player can move 32 at a time)
	public final int UNIT_SIZE = 32;

	// Creates the player socket
	private Socket player;

	// Creates the Data input and output streams;
	private static DataOutputStream out;
	private DataInputStream in;
	public static String outMsg = "";
	public String inMsg;

	// Established the port and host that will be used.
	private int puerto = 2027;
	private String host = "localhost";
	// private String host = "147.182.234.14";

	// Variables need to creat all players.
	static Random rand = new Random();
	volatile static int x;
	volatile static int y;
	volatile static String color;

	public static int lastX;
	public static int lastY;

	// Saves the players starting coords for when the game ends.
	public volatile static int startCoordsX;
	public volatile static int startCoordsY;

	public volatile static boolean isPlayerTurn;
	public volatile static int currTurn;
	public volatile static boolean turnEnded = false;

	// The amount of players the game will expect before starting
	public static int amountofPlayers = 0;

	// All player colors in order of arrival.
	public static ArrayList<String> playerColor = new ArrayList<String>();

	// Checks to see if the game has started.
	public static boolean isGameRunning = false;

	// Determines who is the next turn so all players know whos tuen it is.
	private volatile static int playerTurn = 0;

	// Received the input from main menu for the color the player picked.
	public volatile static int colorIdx = -1;

	// Receives the available colors from server and shows it to the client.
	public volatile static String[] availableColors;

	// Fills players assumptions with false and when player check list is used this
	// will be updated.
	public static ArrayList<Boolean> playerAssumptions = new ArrayList<>() {

		{
			for (int i = 0; i < 21; i++)
				add(false);

		}

	};

	/**
	 * Creates a new client and connects them to the server.
	 */
	public Client() {
		try {

			System.out.println("Client created");

			player = new Socket(host, puerto);

			this.in = new DataInputStream(player.getInputStream());

			out = new DataOutputStream(player.getOutputStream());

		} catch (Exception e) {
			System.out.println("Error in Client file creating client constructor. Error message: " + e.getMessage());
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
		try {

			// Receives the available colors, the players turn on the cycle and if its the
			// players turn from clienteHandeler.
			String inMsg = in.readUTF();

			String[] strMsg = inMsg.split(";");

			availableColors = strMsg[0].split(",");

			currTurn = Integer.parseInt(strMsg[1]);

			isPlayerTurn = Boolean.parseBoolean(strMsg[2]);

			// The program stops until the player picks their color in main menu.
			while (colorIdx == -1) {
			}

			// Prepares the out message for the clientehandeler.
			// If the you are the first one to connect the server you will send the amount
			// of player that are playing and the color index.
			// else only the color index.
			if (currTurn == 0)
				outMsg += amountofPlayers + ";" + colorIdx;
			else
				outMsg += colorIdx + ";";

			out.writeUTF(outMsg);
		} catch (Exception e) {
			System.out.println(
					"Error in Client file receiving and sending of first message. Error message: " + e.getMessage());
		}

		try {
			String inMsg = "";
			// Message to be received: Amount of players, and the starting position of the
			// player, the player color, the players cards, and the secret cards.
			inMsg = in.readUTF();

			String[] positions = inMsg.split(";");

			// Receives the distributed cards from server.
			positions[4] = positions[4].replace("[", "");
			positions[4] = positions[4].replace("]", "");

			String[] cP = positions[4].split(",");

			for (int i = 0; i < cP.length; i++)
				Build.playerCards.add(Integer.parseInt(cP[i].trim()));

			// Receives the secret cards from server.
			positions[5] = positions[5].replace("[", "");
			positions[5] = positions[5].replace("]", "");

			String[] sC = positions[5].split(",");

			for (int i = 0; i < sC.length; i++)
				Build.secretCards.add(Integer.parseInt(sC[i].trim()));

			// Sets the amount of players for everyone.
			amountofPlayers = Integer.parseInt(positions[0]);

			// Removes the amount of players from the received list so it is easier to work
			// with later on.
			positions = Arrays.copyOfRange(positions, 1, positions.length);
			color = positions[0].trim();
			x = Integer.parseInt(positions[1]);
			y = Integer.parseInt(positions[2]);

			// Fills arraylists with default values while waiting for all player colors and
			// coords.
			for (int i = 0; i < amountofPlayers; i++) {

				playerColor.add(null);
				Build.playerX.add(0);
				Build.playerY.add(0);

			}

			playerColor.set(currTurn, color);
			Build.playerX.set(currTurn, x);
			Build.playerY.set(currTurn, y);

		} catch (Exception e) {
			System.out.println(
					"Error in Client file receiving and distributing colors and coords for players. Error message: "
							+ e.getMessage());
		}
		try {

			// Receives all player X and Y Coords and their colors.
			String inMsg = in.readUTF();

			// Dismantles the message
			inMsg = inMsg.replace('[', ' ').trim();
			inMsg = inMsg.replace(']', ' ').trim();
			String[] strMsg = inMsg.split(";");

			strMsg[0] = strMsg[0].replace('[', ' ').trim();
			strMsg[0] = strMsg[0].replace(']', ' ').trim();
			String[] cP = strMsg[0].split(",");

			strMsg[1] = strMsg[1].replace('[', ' ').trim();
			strMsg[1] = strMsg[1].replace(']', ' ').trim();
			String[] xP = strMsg[1].split(",");

			strMsg[2] = strMsg[2].replace('[', ' ').trim();
			strMsg[2] = strMsg[2].replace(']', ' ').trim();
			String[] yP = strMsg[2].split(",");

			// Stores all X, Y Coords and colors in their respective arraylist in the
			// correct order.
			for (int i = 0; i < amountofPlayers; i++) {
				playerColor.set(i, cP[i]);
				Build.playerX.set(i, Integer.parseInt(xP[i].trim()));
				Build.playerY.set(i, Integer.parseInt(yP[i].trim()));
			}

		} catch (Exception e) {
			System.out.println("Error in coords: " + e.getMessage());
		}

		try {
			// Prepares variable before thread goes in a loop.
			lastX = x;
			lastY = y;
			startCoordsX = x;
			startCoordsY = y;

			// Starts the thread loop.
			while (true) {
				// If the game is not running this will start the build for everyone with
				// required values. These must not contain 0.
				if (!isGameRunning && !Build.playerX.contains(0)) {
					isGameRunning = true;
					new Build();

				} else {
					// Checks if its the players turn.
					if (isPlayerTurn) {
						// If the player moved or a rumor started this if will run
						if ((lastX != x || lastY != y) || (Rumor.rumorCharacterIdx != -1 && Rumor.rumorWeaponIdx != -1
								&& Rumor.rumorRoomIdx != -1)) {
							// Changes the player turn if their dice roll reached 0;
							isPlayerTurn = ((Build.diceRoll != 0) ? true : false);

							// Updates lastX and lastY
							lastX = x;
							lastY = y;

							// Updates the arraylist with its new x, y values
							Build.playerX.set(currTurn, x);
							Build.playerY.set(currTurn, y);

							// Sends message to clientHandeler.
							sendOutMsg();

							// If a rumor started sets th eplayer into a waiting state until the rumor is
							// disputed
							if ((Rumor.rumorCharacterIdx != -1 && Rumor.rumorWeaponIdx != -1
									&& Rumor.rumorRoomIdx != -1)) {

								String strMsg[];
								do {

									// Receives the disputed card and a boolean tealling it the start rumor is
									// over..
									String inMsg = in.readUTF();

									strMsg = inMsg.split(";");
									System.out.println("Received str msg " + Arrays.toString(strMsg));
								} while (Boolean.parseBoolean(strMsg[8]));

								// Displays the disputed card sent by the othe player.
								Rumor.showDisputedCard(Integer.parseInt(strMsg[7]));

								// Resets the Rumor class in order to be used again.
								Rumor.reset();

								Build.diceRoll = 0;

								// Sends message to clientHandeler.
								sendOutMsg();

							}
							// If the player is eliminated they will be sent back to their starting
							// positions and the only thing they are allowed to do is dispute rumors.
						} else if (Rumor.isEliminated) {
							Build.diceRoll = 0;
							isPlayerTurn = false;

							x = startCoordsX;
							y = startCoordsY;
							Build.playerX.set(currTurn, x);
							Build.playerY.set(currTurn, y);

							sendOutMsg();
						}
						// If its not your turn all the player will do is receive the updated coords
						// from the player that is moving
					} else {

						String inMsg = in.readUTF();
						System.out.println("in: " + playerColor.get(currTurn) + " " + inMsg);
						changeXYValues(inMsg);

					} // else

				} // else

			} // while

		} catch (Exception e) {
			System.out.println("Error in Client file sending constant x and y coordinates. Error message: "
					+ e.getMessage() + "\n" + "Line number: " + e.getStackTrace()[0].getLineNumber());

		} // catch

	}// run

	/**
	 * Dismantles the message sent from clientHandeler and applies the proper
	 * changes
	 * 
	 * @param inMsg Message sent from clienteHandelet
	 */
	public static void changeXYValues(String inMsg) {

		inMsg = inMsg.replace('[', ' ').trim();
		inMsg = inMsg.replace(']', ' ').trim();
		String[] strMsg = inMsg.split(";");

		// Checks if a rumor has started
		if (Integer.parseInt(strMsg[4]) != -1 && Integer.parseInt(strMsg[5]) != -1
				&& Integer.parseInt(strMsg[6]) != -1) {
			try {
				// checks to see which player gets to dispute the rumor.
				if (Boolean.parseBoolean(strMsg[8]) && Integer.parseInt(strMsg[9]) == currTurn) {
					Rumor.rumorCharacterIdx = Integer.parseInt(strMsg[4]);
					Rumor.rumorWeaponIdx = Integer.parseInt(strMsg[5]);
					Rumor.rumorRoomIdx = Integer.parseInt(strMsg[6]);

					Rumor.disputeRumor();

					// Use index of -2 as default index and -1 if the player doesn't have cards to
					// dispute
					while (Rumor.cardDisputed == -2) {
					}

					// Prepares to send the disputed card to the other player.
					String outMsg = "";
					outMsg += x + ";";
					outMsg += y + ";";
					outMsg += false + ";";

					outMsg += Rumor.rumorCharacterIdx + ";";
					outMsg += Rumor.rumorWeaponIdx + ";";
					outMsg += Rumor.rumorRoomIdx + ";";
					outMsg += Rumor.cardDisputed + ";";

					// Resets rumor for later use
					Rumor.reset();

					outMsg += false + ";";
					outMsg += -2 + ";";

					outMsg += false;

					System.out.println("OUT MESSAGE FOR DISPUTE: " + outMsg);
					out.writeUTF(outMsg);
					out.flush();

				}

			} catch (Exception e) {
				System.out.println("Error Client Trying to dispute rumor: " + e.getMessage());
			}

		} else {
			// Updates the x, y coords of the moving player.
			Build.playerX.set(Integer.parseInt(strMsg[2]), Integer.parseInt(strMsg[0]));
			Build.playerY.set(Integer.parseInt(strMsg[2]), Integer.parseInt(strMsg[1]));

			// Checks to see if the turn ended
			if (Boolean.parseBoolean(strMsg[3]) == true) {
				// If the turn ended increamnet player turn
				playerTurn = Integer.parseInt(strMsg[2]) + 1;

				// If the increament passed the amount of players reset playerTurn to 0.
				if (playerTurn == amountofPlayers)
					playerTurn = 0;
			} // if

			// if the turn ended and player turn is equal to the players assined turn then
			// start the turn
			if (Boolean.parseBoolean(strMsg[3]) && playerTurn == currTurn) {
				Build.newDiceRoll();
				isPlayerTurn = true;
			} // if

			// If the game ended reset everyone to their startinf positions and show the
			// winning cards.
			if (Boolean.parseBoolean(strMsg[10])) {
				// System.out.println("I LOST");
				Rumor.isEliminated = true;
				x = startCoordsX;
				y = startCoordsY;
				Build.playerX.set(currTurn, x);
				Build.playerY.set(currTurn, y);
				Rumor.checkSecretFolder();
			}

		}

	}// changeXY

	/**
	 * Prepares the out message to be sent to the clientHandeler.
	 * 
	 * @throws IOException
	 */
	public static void sendOutMsg() throws IOException {
		outMsg = "";

		outMsg += x + ";";
		outMsg += y + ";";
		outMsg += ((Build.diceRoll != 0) ? false : true) + ";";
		outMsg += Rumor.rumorCharacterIdx + ";";
		outMsg += Rumor.rumorWeaponIdx + ";";
		outMsg += Rumor.rumorRoomIdx + ";";
		outMsg += Rumor.cardDisputed + ";";

		outMsg += ((Rumor.rumorCharacterIdx != -1) ? true : false) + ";";

		outMsg += rand.nextInt(amountofPlayers) + ";";
		outMsg += Rumor.playerWon + ";";

		System.out.println("out: test " + playerColor.get(currTurn) + " " + outMsg);
		out.writeUTF(outMsg);
		out.flush();

	}

}
