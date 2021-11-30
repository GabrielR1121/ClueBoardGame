package clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ClientHandeler implements Runnable {

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private static int playerTurn; 

	public String inMsg;
	public String outMsg;

	//	public static ArrayList<Integer> playerX = new ArrayList<Integer>();
	//	public static ArrayList<Integer> playerY = new ArrayList<Integer>();
	//	public static ArrayList<String> playerColor = new ArrayList<String>();
	public static HashMap<String, Integer[]> characters = new HashMap<String, Integer[]>();

	public boolean isPlayerTurn = false;
	public char label;
	public boolean gameStarted = false;

	public int amountOfPlayers = 0;
	public int color;
	public int x;
	public int y;

	//private int currTurn = 0;

	// public int x;
	// public int y;
	// public String character;

	@SuppressWarnings("static-access")
	public ClientHandeler(Socket socket, int playerTurn, char label) {
		try {
			this.socket = socket;
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.playerTurn = playerTurn;
			this.label = label;
			Server.clientHandeler.add(this);
			Server.connPlayers++;

		} catch (Exception e) {

		}
	}

	@Override
	public void run() {

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

			// System.out.println("first messaage: " + outMsg);

			// use mthod to send
//			System.out.println("Sent the available colors");
			out.writeUTF(outMsg);

		} catch (Exception e) {
			System.out.println("Error in ThreadServer file sending first message. Error message: " + e.getMessage());
		}

		try {
			// Will receive the color chosen and the amountofPLayers (for the first Client).
			// System.out.println("-------");
			String inMsg = in.readUTF();

			// System.out.println("received amount of player and color for player");
			String[] strMsg = inMsg.split(";");

			// System.out.println(Arrays.toString(strMsg));

			// System.out.println("Array:" + Arrays.toString(strMsg));
			// System.out.println("Available colors: " + Server.availableColors);

			if (playerTurn == 0) {
				Server.amountofPlayers = Integer.parseInt(strMsg[0]);
				for (int i = 0; i < Server.amountofPlayers; i++) {
					Server.playerColor.add(null);
					Server.playerX.add(0);
					Server.playerY.add(0);
				}

				strMsg = Arrays.copyOfRange(strMsg, 1, strMsg.length);
				// Server.sb.append(amountofPlayers + ";");
			}

			newPlayer(Server.availableColors.get(Integer.parseInt(strMsg[0])));

			Server.playerColor.set(playerTurn, Server.availableColors.get(Integer.parseInt(strMsg[0])));
			Server.availableColors.remove(Integer.parseInt(strMsg[0]));

			// System.out.println("starting pos: " + Server.sb.toString());

			// System.out.println(Server.sb.toString());

			// The message being sent contains the amount of players, and the starting
			// position of each player.
			// System.out.println(
			// "Colors and X Y" + playerColor.get(turn) + ";" + playerX.get(turn) + ";" +
			// playerY.get(turn));		

//			System.out.println("Player's :" + Server.playerColor + ";" +Server.playerX +";" +Server.playerY);

			// use method to send
			out.writeUTF(Server.amountofPlayers + ";" + Server.playerColor.get(playerTurn) + ";" + Server.playerX.get(playerTurn) + ";"
					+ Server.playerY.get(playerTurn));

//			System.out.println("Sent out player coords");
			// System.out.println(Server.availableColors.toString());

		} catch (Exception e) {
			System.out.println(
					"Error in ThreadServer file receiving amount of players and color player wants. Error message: "
							+ e.getMessage() + "\n" + "Line number: " + e.getStackTrace());
		}

		// System.out.println("about to enter the while loop");

		sendDM(Server.playerColor + ";" + Server.playerX + ";" + Server.playerY + ";" + playerTurn);

		try {
			while (true) {


				// System.out.println("Players online: " +Server.connPlayers);

				// System.out.println("in while loop");
				// inMsg = in.readUTF();
				String inMsg = in.readUTF();
				System.out.println("in: " + inMsg);

				String[] strMsg = inMsg.split(";");

				// System.out.println("In: " + Arrays.toString(strMsg));
				//				System.out.println("StrMSG: " + strMsg[2]);



				// System.out.println("about to send message");
				//				outMsg = strMsg[0] + ";" + strMsg[1] + ";" + ((Boolean.parseBoolean(strMsg[2])) ? true : false);
				outMsg = strMsg[0] + ";" + strMsg[1] + ";" + Server.currTurn + ";" + strMsg[2];

				
				broadcastMessage(outMsg);

				//Changes turns
				if (Boolean.parseBoolean(strMsg[2]) == true) {

					Server.currTurn ++;
					if (Server.currTurn == Server.amountofPlayers)
						Server.currTurn = 0;
					System.out.println("SERVER CHANGING TURNS" + "TURN: " + Server.currTurn);


				}
				//				Server.playerX.set(Server.currTurn, Integer.parseInt(strMsg[0]));
				//				Server.playerY.set(Server.currTurn, Integer.parseInt(strMsg[1]));

				// out.writeUTF(outMsg);

				// use send method





			}//while
		} catch (Exception e) {

		}
	}//run

	public void broadcastMessage(String outMsg) {
		// System.out.println("here");
		// System.out.println(Server.clientHandeler);
		for (ClientHandeler clientHandeler : Server.clientHandeler) {
			try {
				if (clientHandeler.label != this.label) {
					Thread.sleep(100);
					System.out.println("Broadcasting msg: " + outMsg + " to " + clientHandeler.label);
					clientHandeler.out.writeUTF(outMsg);
					clientHandeler.out.flush();
				}

			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}

	}

	public void sendDM(String outMsg) {

		for (ClientHandeler clientHandeler : Server.clientHandeler) {

			try {
				if (!Server.playerX.contains(0)) {
					System.out.println("Sending Dm " + outMsg);
					clientHandeler.out.writeUTF(outMsg);
					clientHandeler.out.flush();
					/////////////////////////
					Server.playerX.clear();
					Server.playerY.clear();
					Server.playerColor.clear();
					/////////////////////////
				}

			} catch (Exception e) {

			}
		}


	}

	public void newPlayer(String color) {

		// System.out.println("EN NEW PLAYER..." + color);
		String charArr[];

		// System.out.println(color);
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

	public static void getStartingCoordinates(String[] charArr) {

		// System.out.println("STARTING COORDS..." + Arrays.toString(charArr));
		// System.out.println(Arrays.toString(charArr));

		Server.playerX.set(playerTurn, Integer.parseInt((charArr[0].replace('[', ' ')).trim()));
		Server.playerY.set(playerTurn, Integer.parseInt((charArr[1].replace(']', ' ')).trim()));
		// System.out.println("new player");

	}

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
