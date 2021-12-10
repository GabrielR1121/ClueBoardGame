package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import javax.swing.JOptionPane;

//Client side.
//Methods in this class will be exclusively for the player and nobody else.  
public class Client implements Runnable {
	public final int SCREEN_WIDTH = 842;
	public final int SCREEN_HEIGHT = 872;
	public final int UNIT_SIZE = 32;
	private Socket cliente;
	private static DataOutputStream out;
	private DataInputStream in;
	private int puerto = 2027;
	private String host = "localhost";
	// private String host = "147.182.234.14";

	Random rand = new Random();
	volatile static int x;
	volatile static int y;
	volatile static String color;

	public static int lastX;
	public static int lastY;

	public static int amountofPlayers = 0;

	public static ArrayList<String> playerColor = new ArrayList<String>();
	public volatile static boolean isPlayerTurn;
	public volatile static int currTurn;
	public volatile static boolean turnEnded = false;

	public static boolean isGameRunning = false;

	public static String outMsg = "";
	public String inMsg;
	public String character;
	private volatile static int playerTurn = 0;
	public volatile static int colorIdx = -1;
	public volatile static String[] availableColors;
	public volatile static int startCoordsX;
	public volatile static int startCoordsY;

	public static ArrayList<Boolean> playerAssumptions = new ArrayList<>() {

		{
			for (int i = 0; i < 21; i++)
				add(false);

		}

	};

	public Client() {
		try {

			System.out.println("Client created");

			cliente = new Socket(host, puerto);

			this.in = new DataInputStream(cliente.getInputStream());

			this.out = new DataOutputStream(cliente.getOutputStream());

		} catch (Exception e) {
			System.out.println("Error in Client file creating client constructor. Error message: " + e.getMessage());
		}

	}

	@Override
	public void run() {
		try {

			// We should receive availableColors, turn, and isPlayerTurn
			String inMsg = in.readUTF();

			String[] strMsg = inMsg.split(";");

			availableColors = strMsg[0].split(",");

			currTurn = Integer.parseInt(strMsg[1]); // Will be used as an index for the coordinates.

			isPlayerTurn = Boolean.parseBoolean(strMsg[2]);

			// int colorIdx = rand.nextInt(availableColors.length);
			while (colorIdx == -1) {
			}

			// System.out.println("Color index: " + colorIdx);

			// We should send the index of the amount of players, and the colorIdx to
			// join.
			// This is for the first player, the rest send only the color chosen.

			// String outMsg = "";

			// Only for the first player
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
			// Message to be received: Amount of players, and the starting position of each
			// player.
			inMsg = in.readUTF();

			// The first thing being sent is the amount of players, then the positions.
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

			// removed an if
			amountofPlayers = Integer.parseInt(positions[0]);

			positions = Arrays.copyOfRange(positions, 1, positions.length);
			color = positions[0].trim();
			x = Integer.parseInt(positions[1]);
			y = Integer.parseInt(positions[2]);

			// Placeholders
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

			// Setting the initial values for the colors, and the starting positions of
			// each.
			String inMsg = in.readUTF();

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

			for (int i = 0; i < amountofPlayers; i++) {
				playerColor.set(i, cP[i]);
				Build.playerX.set(i, Integer.parseInt(xP[i].trim()));
				Build.playerY.set(i, Integer.parseInt(yP[i].trim()));

			}

		} catch (Exception e) {
			System.out.println("Error in coords: " + e.getMessage());
		}

		try {

			lastX = x;
			lastY = y;
			startCoordsX = x;
			startCoordsY = y;

			while (true) {
				// If the game is not running this will start the build for everyone with
				// required values. These must not contain 0.
				if (!isGameRunning && !Build.playerX.contains(0)) {
					isGameRunning = true;
					new Build();

				} // Forces while loop to online run when the values of x and y change.
				else {
					if (isPlayerTurn) {

						if ((lastX != x || lastY != y) || (Rumor.rumorCharacterIdx != -1 && Rumor.rumorWeaponIdx != -1
								&& Rumor.rumorRoomIdx != -1)) {

							isPlayerTurn = ((Build.diceRoll != 0) ? true : false);
							lastX = x;
							lastY = y;
							Build.playerX.set(currTurn, x);
							Build.playerY.set(currTurn, y);

							///////////////
							sendOutMsg();
							//////////////

							if ((Rumor.rumorCharacterIdx != -1 && Rumor.rumorWeaponIdx != -1
									&& Rumor.rumorRoomIdx != -1)) {

								String strMsg[];
								do {
									String inMsg = in.readUTF();

									strMsg = inMsg.split(";");
									System.out.println("Received str msg " + Arrays.toString(strMsg));
								} while (Boolean.parseBoolean(strMsg[8]));

								Rumor.showDisputedCard(Integer.parseInt(strMsg[7]));
								// System.out.println("Card Disputed: " + Build.cardDeckMap.get());
								// changeXYValues(inMsg);
								Rumor.reset();
								Build.diceRoll = 0;
								// NEED TO SAVE DISPUTED CARD!!!!!!!!!

								// Make clear rumor method.
								// Make code to view disputed card.

								// try to place this in a method because code is repeating.

								////////////////////
								sendOutMsg();
								/////////////////

							}

						} else if (Rumor.isEliminated) {
							Build.diceRoll = 0;
							isPlayerTurn = false;

							x = startCoordsX;
							y = startCoordsY;
							Build.playerX.set(currTurn, x);
							Build.playerY.set(currTurn, y);

							////////
							sendOutMsg();
							////////

						}

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

	// We receive x y and index to change value at
	public static void changeXYValues(String inMsg) {

		inMsg = inMsg.replace('[', ' ').trim();
		inMsg = inMsg.replace(']', ' ').trim();
		String[] strMsg = inMsg.split(";");

		if (Integer.parseInt(strMsg[4]) != -1 && Integer.parseInt(strMsg[5]) != -1
				&& Integer.parseInt(strMsg[6]) != -1) {
			try {
				// Check if index are correct
				if (Boolean.parseBoolean(strMsg[8]) && Integer.parseInt(strMsg[9]) == currTurn) {
					Rumor.rumorCharacterIdx = Integer.parseInt(strMsg[4]);
					Rumor.rumorWeaponIdx = Integer.parseInt(strMsg[5]);
					Rumor.rumorRoomIdx = Integer.parseInt(strMsg[6]);
					System.out.println("It is my turn to dispute");

					Rumor.disputeRumor();

					// Use index of -2 as default index and -1 if the player doesn't have cards to
					// dispute
					while (Rumor.cardDisputed == -2) {
					}

					// This cant go into sendDM cause we are ending turns and switching variables
					// specifically for this case.
					String outMsg = "";
					outMsg += x + ";";
					outMsg += y + ";";
					outMsg += false + ";";

					outMsg += Rumor.rumorCharacterIdx + ";";
					outMsg += Rumor.rumorWeaponIdx + ";";
					outMsg += Rumor.rumorRoomIdx + ";";
					outMsg += Rumor.cardDisputed + ";";

					Rumor.reset();

					outMsg += false + ";";
					outMsg += -2 + ";";

					// If anything ever goes wrong en el codigo the error might be here.
					outMsg += false;

					System.out.println("OUT MESSAGE FOR DISPUTE: " + outMsg);
					out.writeUTF(outMsg);
					out.flush();

				}

			} catch (Exception e) {
				System.out.println("Error Client Trying to dispute rumor: " + e.getMessage());
			}

		} else {
			Build.playerX.set(Integer.parseInt(strMsg[2]), Integer.parseInt(strMsg[0]));
			Build.playerY.set(Integer.parseInt(strMsg[2]), Integer.parseInt(strMsg[1]));

			if (Boolean.parseBoolean(strMsg[3]) == true) {

				playerTurn = Integer.parseInt(strMsg[2]) + 1;

				if (playerTurn == amountofPlayers)
					playerTurn = 0;
			} // if

			if (Boolean.parseBoolean(strMsg[3]) && playerTurn == currTurn) {
				Build.newDiceRoll();
				isPlayerTurn = true;
			} // if

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

	public static void sendOutMsg() throws IOException {
		outMsg = "";

		outMsg += x + ";";
		outMsg += y + ";";
		outMsg += ((Build.diceRoll != 0) ? false : true) + ";";// Estamos haciendo el trabajo de
		// turnEnded().
		outMsg += Rumor.rumorCharacterIdx + ";";
		outMsg += Rumor.rumorWeaponIdx + ";";
		outMsg += Rumor.rumorRoomIdx + ";";
		// Equivalent of testVar V
		outMsg += Rumor.cardDisputed + ";";

		outMsg += ((Rumor.rumorCharacterIdx != -1) ? true : false) + ";";

		outMsg += ((currTurn == amountofPlayers - 1) ? 0 : (currTurn + 1)) + ";";
		outMsg += Rumor.playerWon + ";";

		System.out.println("out: test " + playerColor.get(currTurn) + " " + outMsg);
		out.writeUTF(outMsg);
		out.flush();

	}

}
