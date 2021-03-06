package Clue;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Build extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 842;
	static final int SCREEN_HEIGHT = 872;
	static final int UNIT_SIZE = 32;
	int align = 6;
	char direction = 'p';
	public static String color;
	Random rand = new Random();
	JFrame frame = new JFrame();
	public static int diceRoll = 0;
	static String roomName = "";

	public static CopyOnWriteArrayList<Integer> playerX = new CopyOnWriteArrayList<Integer>();
	public static CopyOnWriteArrayList<Integer> playerY = new CopyOnWriteArrayList<Integer>();
	public static ArrayList<Integer> playerCards = new ArrayList<Integer>();
	public static CopyOnWriteArrayList<Integer> secretCards = new CopyOnWriteArrayList<Integer>();
	public static HashMap<Integer, ArrayList<HashMap<Integer, String>>> roomCoord = new HashMap<Integer, ArrayList<HashMap<Integer, String>>>();
	public static JButton[] cardButtons = new JButton[21];

	// will be used for startPlayerTurn()
	public static int mutablePlayerTurn = 0;

	// Permited moves Hashmap
	HashMap<Integer, ArrayList<Integer>> permitedCoordinates = new HashMap<Integer, ArrayList<Integer>>();

	// Characters hashmap
	public static HashMap<String, Integer[]> characters = new HashMap<String, Integer[]>();

	// Doors positions hashmap
	HashMap<Integer, ArrayList<Integer>> doors = new HashMap<Integer, ArrayList<Integer>>();

	// Has the string info of all of the cards on the cardDeck array.
	public static HashMap<Integer, String> cardDeckMap = new HashMap<Integer, String>();

	// Used to paint the players color at the top of the play screen.
	public volatile static Color playerColor;

	public volatile static Boolean gameEnded = false;

	public static SoundThread gameSound = new SoundThread(".//Assets//Sound//gameScreen.wav");
	public static SoundThread winSound = new SoundThread(".//Assets//Sound//playerWin.wav");
	public static SoundThread looseSound = new SoundThread(".//Assets//Sound//playerLoose.wav");

	// Constructor
	public Build() {

		System.out.println("Starting build...");
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());

		System.out.println("Staring gameframe...");
		// ImageIcon
		ImageIcon logo = new ImageIcon(".\\Assets\\GameBoard\\iconImage.png");
		frame.setIconImage(logo.getImage());
		frame.add(this);
		frame.setTitle("Clue");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);

		main();
		gameSound.playSoundEffect();

	}

	/** Main entry point. */
	public void main() {

		// Adding the starting coordinates for each of the characters.
		characters.put("Green", new Integer[] { 327, 807 });
		characters.put("Mustard", new Integer[] { 775, 263 });
		characters.put("Orchid", new Integer[] { 487, 807 });// to do
		characters.put("Peacock", new Integer[] { 39, 615 });
		characters.put("Plum", new Integer[] { 39, 199 });
		characters.put("Scarlett", new Integer[] { 551, 39 });

		// Adding the starting coordinates for each of the doors.
		roomCoord.put(199, new ArrayList<>());
		roomCoord.get(199).add(new HashMap<>() {
			{
				put(647, "Conservatory");
			}
		});

		roomCoord.put(519, new ArrayList<>());
		roomCoord.get(519).add(new HashMap<>() {
			{
				put(423, "DiningRoom");
			}
		});

		roomCoord.put(295, new ArrayList<>());
		roomCoord.get(295).add(new HashMap<>() {
			{
				put(167, "Hall");
			}
		});

		roomCoord.put(647, new ArrayList<>());
		roomCoord.get(647).add(new HashMap<>() {
			{
				put(583, "Kitchen");
			}
		});

		roomCoord.put(263, new ArrayList<>());
		roomCoord.get(263).add(new HashMap<>() {
			{
				put(295, "Library");
			}
		});

		roomCoord.put(583, new ArrayList<>());
		roomCoord.get(583).add(new HashMap<>() {
			{
				put(231, "Lounge");
			}
		});
		roomCoord.put(487, new ArrayList<>());
		roomCoord.get(487).add(new HashMap<>() {
			{
				put(551, "BallRoom");
			}
		});

		roomCoord.put(103, new ArrayList<>());
		roomCoord.get(103).add(new HashMap<>() {
			{
				put(391, "BilliardRoom");
			}
		});

		roomCoord.put(391, new ArrayList<>());
		roomCoord.get(391).add(new HashMap<>() {
			{
				put(263, "DecisionRoom");
			}
		});

		roomCoord.put(231, new ArrayList<>());
		roomCoord.get(231).add(new HashMap<>() {
			{
				put(167, "Study");
			}
		});

		// Hashmap of the info on the cardDeck elements.
		cardDeckMap.put(0, "Green");
		cardDeckMap.put(1, "Mustard");
		cardDeckMap.put(2, "Orchid");
		cardDeckMap.put(3, "Peacock");
		cardDeckMap.put(4, "Plum");
		cardDeckMap.put(5, "Scarlett");
		cardDeckMap.put(6, "BallRoom");
		cardDeckMap.put(7, "BilliardRoom");
		cardDeckMap.put(8, "Conservatory");
		cardDeckMap.put(9, "DiningRoom");
		cardDeckMap.put(10, "Hall");
		cardDeckMap.put(11, "Kitchen");
		cardDeckMap.put(12, "Library");
		cardDeckMap.put(13, "Lounge");
		cardDeckMap.put(14, "Study");
		cardDeckMap.put(15, "Candlestick");
		cardDeckMap.put(16, "Dagger");
		cardDeckMap.put(17, "LeadPipe");
		cardDeckMap.put(18, "Revolver");
		cardDeckMap.put(19, "Rope");
		cardDeckMap.put(20, "Wrench");

		permitedCoordinates.put(263, new ArrayList<Integer>());
		permitedCoordinates.get(263).add(71);
		permitedCoordinates.get(263).add(135);
		permitedCoordinates.get(263).add(103);
		permitedCoordinates.get(263).add(167);
		permitedCoordinates.get(263).add(199);
		permitedCoordinates.get(263).add(231);
		permitedCoordinates.get(263).add(263);
		permitedCoordinates.get(263).add(295);
		permitedCoordinates.get(263).add(327);
		permitedCoordinates.get(263).add(359);
		permitedCoordinates.get(263).add(391);
		permitedCoordinates.get(263).add(423);
		permitedCoordinates.get(263).add(455);
		permitedCoordinates.get(263).add(487);
		permitedCoordinates.get(263).add(519);
		permitedCoordinates.get(263).add(551);
		permitedCoordinates.get(263).add(583);
		permitedCoordinates.get(263).add(615);
		permitedCoordinates.get(263).add(647);
		permitedCoordinates.get(263).add(679);
		permitedCoordinates.get(263).add(711);
		permitedCoordinates.get(263).add(743);
		permitedCoordinates.get(263).add(775);
		/////
		permitedCoordinates.put(295, new ArrayList<Integer>());
		permitedCoordinates.get(295).add(71);
		permitedCoordinates.get(295).add(103);
		permitedCoordinates.get(295).add(135);
		permitedCoordinates.get(295).add(167);
		permitedCoordinates.get(295).add(199);
		permitedCoordinates.get(295).add(231);
		permitedCoordinates.get(295).add(263);
		permitedCoordinates.get(295).add(295);
		permitedCoordinates.get(295).add(327);
		permitedCoordinates.get(295).add(359);
		permitedCoordinates.get(295).add(391);
		permitedCoordinates.get(295).add(423);
		permitedCoordinates.get(295).add(455);
		permitedCoordinates.get(295).add(487);
		permitedCoordinates.get(295).add(519);
		permitedCoordinates.get(295).add(551);
		permitedCoordinates.get(295).add(775);
		///
		permitedCoordinates.put(231, new ArrayList<Integer>());
		permitedCoordinates.get(231).add(199);
		permitedCoordinates.get(231).add(231);
		permitedCoordinates.get(231).add(359);
		permitedCoordinates.get(231).add(391);
		permitedCoordinates.get(231).add(423);
		permitedCoordinates.get(231).add(455);
		permitedCoordinates.get(231).add(487);
		permitedCoordinates.get(231).add(519);
		permitedCoordinates.get(231).add(551);
		permitedCoordinates.get(231).add(583);
		permitedCoordinates.get(231).add(615);
		permitedCoordinates.get(231).add(647);
		permitedCoordinates.get(231).add(679);
		permitedCoordinates.get(231).add(711);
		permitedCoordinates.get(231).add(743);
		permitedCoordinates.get(231).add(167);
		//////
		permitedCoordinates.put(199, new ArrayList<Integer>());
		permitedCoordinates.get(199).add(167);
		permitedCoordinates.get(199).add(199);
		permitedCoordinates.get(199).add(391);
		permitedCoordinates.get(199).add(583);
		permitedCoordinates.get(199).add(615);
		permitedCoordinates.get(199).add(647);
		/////
		permitedCoordinates.put(167, new ArrayList<Integer>());
		permitedCoordinates.get(167).add(167);
		permitedCoordinates.get(167).add(199);
		permitedCoordinates.get(167).add(391);
		permitedCoordinates.get(167).add(583);
		permitedCoordinates.get(167).add(615);
		////////
		permitedCoordinates.put(135, new ArrayList<Integer>());
		permitedCoordinates.get(135).add(167);
		permitedCoordinates.get(135).add(199);
		permitedCoordinates.get(135).add(391);
		permitedCoordinates.get(135).add(583);
		permitedCoordinates.get(135).add(615);
		//
		permitedCoordinates.put(103, new ArrayList<Integer>());
		permitedCoordinates.get(103).add(167);
		permitedCoordinates.get(103).add(199);
		permitedCoordinates.get(103).add(391);
		permitedCoordinates.get(103).add(583);
		permitedCoordinates.get(103).add(615);
		////
		permitedCoordinates.put(71, new ArrayList<Integer>());
		permitedCoordinates.get(71).add(167);
		permitedCoordinates.get(71).add(199);
		permitedCoordinates.get(71).add(391);
		permitedCoordinates.get(71).add(583);
		permitedCoordinates.get(71).add(615);
		//
		permitedCoordinates.put(327, new ArrayList<Integer>());
		permitedCoordinates.get(327).add(263);
		permitedCoordinates.get(327).add(519);
		permitedCoordinates.get(327).add(551);
		permitedCoordinates.get(327).add(775);
		//
		permitedCoordinates.put(359, new ArrayList<Integer>());
		permitedCoordinates.get(359).add(263);
		permitedCoordinates.get(359).add(519);
		permitedCoordinates.get(359).add(551);
		//
		permitedCoordinates.put(391, new ArrayList<Integer>());
		permitedCoordinates.get(391).add(263);
		permitedCoordinates.get(391).add(519);
		permitedCoordinates.get(391).add(551);
		//
		permitedCoordinates.put(423, new ArrayList<Integer>());
		permitedCoordinates.get(423).add(263);
		permitedCoordinates.get(423).add(519);
		permitedCoordinates.get(423).add(551);
		//
		permitedCoordinates.put(455, new ArrayList<Integer>());
		permitedCoordinates.get(455).add(263);
		permitedCoordinates.get(455).add(519);
		permitedCoordinates.get(455).add(551);
		//
		permitedCoordinates.put(487, new ArrayList<Integer>());
		permitedCoordinates.get(487).add(263);
		permitedCoordinates.get(487).add(295);
		permitedCoordinates.get(487).add(327);
		permitedCoordinates.get(487).add(359);
		permitedCoordinates.get(487).add(391);
		permitedCoordinates.get(487).add(423);
		permitedCoordinates.get(487).add(455);
		permitedCoordinates.get(487).add(487);
		permitedCoordinates.get(487).add(519);
		permitedCoordinates.get(487).add(551);
		permitedCoordinates.get(487).add(775);
		/////
		permitedCoordinates.put(519, new ArrayList<Integer>());
		permitedCoordinates.get(519).add(71);
		permitedCoordinates.get(519).add(103);
		permitedCoordinates.get(519).add(135);
		permitedCoordinates.get(519).add(167);
		permitedCoordinates.get(519).add(199);
		permitedCoordinates.get(519).add(231);
		permitedCoordinates.get(519).add(263);
		permitedCoordinates.get(519).add(295);
		permitedCoordinates.get(519).add(327);
		permitedCoordinates.get(519).add(359);
		permitedCoordinates.get(519).add(391);
		permitedCoordinates.get(519).add(423);
		permitedCoordinates.get(519).add(455);
		permitedCoordinates.get(519).add(487);
		permitedCoordinates.get(519).add(519);
		permitedCoordinates.get(519).add(551);
		permitedCoordinates.get(519).add(775);
		///
		permitedCoordinates.put(551, new ArrayList<Integer>());
		permitedCoordinates.get(551).add(71);
		permitedCoordinates.get(551).add(103);
		permitedCoordinates.get(551).add(135);
		permitedCoordinates.get(551).add(167);
		permitedCoordinates.get(551).add(199);
		permitedCoordinates.get(551).add(231);
		permitedCoordinates.get(551).add(263);
		permitedCoordinates.get(551).add(295);
		permitedCoordinates.get(551).add(519);
		permitedCoordinates.get(551).add(551);
		permitedCoordinates.get(551).add(583);
		permitedCoordinates.get(551).add(615);
		permitedCoordinates.get(551).add(647);
		permitedCoordinates.get(551).add(679);
		permitedCoordinates.get(551).add(711);
		permitedCoordinates.get(551).add(743);
		permitedCoordinates.get(551).add(775);

		permitedCoordinates.put(583, new ArrayList<Integer>());
		permitedCoordinates.get(583).add(231);
		permitedCoordinates.get(583).add(263);
		permitedCoordinates.get(583).add(295);
		permitedCoordinates.get(583).add(519);
		permitedCoordinates.get(583).add(551);
		permitedCoordinates.get(583).add(583);
		permitedCoordinates.get(583).add(615);
		permitedCoordinates.get(583).add(647);
		permitedCoordinates.get(583).add(679);
		permitedCoordinates.get(583).add(711);
		permitedCoordinates.get(583).add(743);
		///
		permitedCoordinates.put(615, new ArrayList<Integer>());
		permitedCoordinates.get(615).add(231);
		permitedCoordinates.get(615).add(263);
		permitedCoordinates.get(615).add(295);
		permitedCoordinates.get(615).add(519);
		permitedCoordinates.get(615).add(551);
		permitedCoordinates.get(615).add(583);
		//
		permitedCoordinates.put(647, new ArrayList<Integer>());
		permitedCoordinates.get(647).add(231);
		permitedCoordinates.get(647).add(263);
		permitedCoordinates.get(647).add(295);
		permitedCoordinates.get(647).add(551);
		permitedCoordinates.get(647).add(583);
		//
		permitedCoordinates.put(679, new ArrayList<Integer>());
		permitedCoordinates.get(679).add(231);
		permitedCoordinates.get(679).add(263);
		permitedCoordinates.get(679).add(295);
		permitedCoordinates.get(679).add(551);
		permitedCoordinates.get(679).add(583);
		//
		permitedCoordinates.put(711, new ArrayList<Integer>());
		permitedCoordinates.get(711).add(231);
		permitedCoordinates.get(711).add(263);
		permitedCoordinates.get(711).add(295);
		permitedCoordinates.get(711).add(551);
		permitedCoordinates.get(711).add(583);
		//
		permitedCoordinates.put(743, new ArrayList<Integer>());
		permitedCoordinates.get(743).add(231);
		permitedCoordinates.get(743).add(263);
		permitedCoordinates.get(743).add(295);
		permitedCoordinates.get(743).add(551);
		permitedCoordinates.get(743).add(583);

		for (int i = 0; i < cardButtons.length; i++) {
			cardButtons[i] = new JButton(
					new ImageIcon(
							".\\Assets\\Card_Deck\\" + i + ".png"));
		}
	}

	/**
	 * Initializes the paint for the whole game.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	// Draws all the componets of the game onto the gameframe which include:
	// * GameBoard
	// * Players / Player Movement.
	// * Their respective color
	public void draw(Graphics g) {
		if (!gameEnded) {
			Image img = Toolkit.getDefaultToolkit().getImage(
					".\\Assets\\GameBoard\\ClueGameBoard(updated).jpg");

			g.drawImage(img, 0, 0, null);

			// for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
			// g.drawLine(i * UNIT_SIZE + align, 0, i * UNIT_SIZE + align, SCREEN_HEIGHT);
			// g.drawLine(0, i * UNIT_SIZE + align, SCREEN_WIDTH, i * UNIT_SIZE + align);
			// // Redraws the board each time something happens.
			// repaint();
			// }

			// Gets the starting position for each players and draws it into the window.
			for (int i = 0; i < Client.playerColor.size(); i++) {

				if (Client.playerColor.get(i) != null) {

					switch (Client.playerColor.get(i).trim()) {
						case "Green":
							g.setColor(Colors.Green.getColor());
							break;
						case "Mustard":
							g.setColor(Colors.Mustard.getColor());
							break;
						case "Orchid":
							g.setColor(Colors.Orchid.getColor());
							break;
						case "Peacock":
							g.setColor(Colors.Peacock.getColor());
							break;
						case "Plum":
							g.setColor(Colors.Plum.getColor());
							break;
						case "Scarlett":
							g.setColor(Colors.Scarlett.getColor());
							break;
						default:
							break;
					}// switch()
					if (i == Client.currTurn)
						playerColor = g.getColor();

					g.fillOval(playerX.get(i), playerY.get(i), UNIT_SIZE, UNIT_SIZE);
					repaint();
				} // if()
			} // for()

			startPlayerTurn();
			checkMoves();

			g.setColor(playerColor);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString(Client.playerColor.get(Client.currTurn), (metrics.stringWidth("Player Color: ")) + 40,
					g.getFont().getSize() + align);

			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			FontMetrics metrics1 = getFontMetrics(g.getFont());
			g.drawString("Player Color: ",
					(metrics1.stringWidth("Player Color: ")) - 70, g.getFont().getSize() + align);

			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			FontMetrics metrics2 = getFontMetrics(g.getFont());
			g.drawString("Dice roll: " + diceRoll, (SCREEN_WIDTH - metrics2.stringWidth("Dice Roll: " + diceRoll)) - 35,
					g.getFont().getSize() + align);

			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			FontMetrics metrics3 = getFontMetrics(g.getFont());
			g.drawString("Press C to display checklist",
					(metrics3.stringWidth("Press C on keyboard to display player checklist")) - 350,
					SCREEN_HEIGHT - 35);
			g.setColor(Color.white);
			g.setFont(new Font("Ink Free", Font.BOLD, 20));
			FontMetrics metrics4 = getFontMetrics(g.getFont());
			g.drawString("Press P to display deck of cards.",
					(metrics4.stringWidth("Press P on keyboard to display deck of cards.")) - 350, SCREEN_HEIGHT - 15);

		} else
			gameOver(g);

	} // draw()

	public void gameOver(Graphics g) {
		gameSound.pauseSound();
		this.setBackground(Color.black);
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 100));
		FontMetrics metrics5 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics5.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
		g.setColor(playerColor);
		if (Rumor.playerWon) {
			g.drawString("YOU WON!!", (SCREEN_WIDTH - metrics5.stringWidth("YOU WON!!")) / 2,
					(SCREEN_HEIGHT / 2) + 100);
			winSound.playSound();
		} else {
			g.drawString("YOU LOST", (SCREEN_WIDTH - metrics5.stringWidth("YOU LOST")) / 2, (SCREEN_HEIGHT / 2) + 100);
			looseSound.playSound();
		}

	}

	public void startPlayerTurn() {

		if (mutablePlayerTurn == 0 && Client.isPlayerTurn) {
			newDiceRoll();
			mutablePlayerTurn = 1;
		}
	}

	/**
	 * Verifies if the player has remaining moves.
	 */
	public void checkMoves() {

		// when the dice roll count hits 0 and it's still the player's turn,
		// that mentioned player's turn becomes false and moves onto the next player.
		if (diceRoll == 0 && Client.isPlayerTurn) {
			Client.isPlayerTurn = false;
			mutablePlayerTurn = 0;

		} // if

	}// checkMoves

	// Gives the player a new dice roll if its their turn.
	public static void newDiceRoll() {

		Random rand = new Random();
		if (!Rumor.isEliminated) {
			SoundThread diceSound = new SoundThread(".//Assets//Sound//Dice Roll.wav");
			diceRoll = rand.nextInt(12) + 1;
			diceSound.playSound();
		} else
			diceRoll = 0;

	}

	/**
	 * Checks to see if the player is between the game bounds:
	 * the permited coordinates inside of the board.
	 * 
	 * @param xCoord - x coordinates
	 * @param yCoord - y coordinates
	 * @return true if the coordinates exists
	 *         false if the coordinates don't exist.
	 */
	public Boolean checkBounds(int xCoord, int yCoord) {

		// If the x coordinate is inside of the permited coordinates.
		if (permitedCoordinates.containsKey(xCoord)) {
			// Checks if the y associated with the x is the same one.
			if (permitedCoordinates.get(xCoord).contains(yCoord))
				return true;
		}
		return false;
	}

	/**
	 * Checks if the player is in front of a door based on the coordinates
	 * of the tile in front of the door.
	 * 
	 * @param xCoord - x coordinates
	 * @param yCoord - y coordinates.
	 * @return true if the coordinates exists
	 *         false if the coordinates don't exist.
	 */
	public static Boolean checkRoom(int xCoord, int yCoord) {

		// If the x coordinate is inside of the permited coordinates.
		if (roomCoord.containsKey(xCoord)) {
			if (roomCoord.get(xCoord).size() == 1) {
				// Gets the y coordinate associated with said x.
				if (roomCoord.get(xCoord).get(0).containsKey(yCoord)) {
					// gets the name of the room associates with the x and y values.
					roomName = roomCoord.get(xCoord).get(0).get(yCoord);
					return true;
				}
			}
		}

		return false;

	}

	// Uses the global variable direction in order to move a player N,E,S,W with
	// respect to the Unit_Size.
	// After player moves direction is switched to s in order to prevent constant
	// movement.
	// For now print (X,Y) of the player.
	public void move() {
		if (Client.isPlayerTurn) {
			switch (direction) {

				case 'U':
					if (checkBounds(Client.x, Client.y - UNIT_SIZE)) {

						Client.y = Client.y - UNIT_SIZE;
						--diceRoll;

					}
					break;

				case 'D':
					if (checkBounds(Client.x, Client.y + UNIT_SIZE)) {

						Client.y = Client.y + UNIT_SIZE;
						--diceRoll;

					}
					break;

				case 'L':
					if (checkBounds(Client.x - UNIT_SIZE, Client.y)) {

						Client.x = Client.x - UNIT_SIZE;
						--diceRoll;

					}
					break;

				case 'R':
					if (checkBounds(Client.x + UNIT_SIZE, Client.y)) {
						Client.x = Client.x + UNIT_SIZE;
						--diceRoll;
					}

					break;
			}

			// Move to draw when an idea about how to run only once occurs.
			if (checkRoom(Client.x, Client.y) && diceRoll != 0)
				Rumor.startRumor(roomName);
			direction = 's';

		} else
			direction = 's';
	}

	// activates when an action is preformed in order to run previous methods.
	// Useless method right now
	// Dead code but cant delete.
	public void actionPerformed(ActionEvent e) {
		System.out.println(direction);

	}

	// Checks to see if a key has been pressed and changes direction based on arrow
	// and the player's turn.
	public class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			switch (e.getKeyCode()) {

				case KeyEvent.VK_LEFT:
					direction = 'L';
					move();
					break;

				case KeyEvent.VK_RIGHT:
					direction = 'R';
					move();
					break;

				case KeyEvent.VK_UP:
					direction = 'U';
					move();
					break;

				case KeyEvent.VK_DOWN:
					direction = 'D';
					move();
					break;

				case KeyEvent.VK_C:

					new PlayerChecklist(playerCards, Client.currTurn, Client.playerAssumptions);

					break;
				case KeyEvent.VK_P:

					new PlayerCardsList();

					break;
			}

		}// keyPressed
	}// MyKeyAdapter

}// class
