package Clue;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

/**
 * Creates and opens the frame for the Main Menu with all its component.
 */
public class MainMenu extends JPanel {

	static final int SCREEN_WIDTH = 842;
	static final int SCREEN_HEIGHT = 872;
	JFrame frame = new JFrame();

	// Waits for every player to choose a color and join the game,
	// then every player gets the same gameboard with every location for
	// their chosen color.
	public MainMenu() {

		this.setLayout(new GridBagLayout());
		// Adds the game buttons
		addGameButtons();
		this.setFocusable(true);

		// ImageIcon
		ImageIcon logo = new ImageIcon(
				"C:\\Users\\grgar\\OneDrive\\The backup folder\\School\\UPRB folder\\Fourth Year\\Semester 1\\Data Communication\\CLUE\\ClueBoardGame\\Assets\\GameBoard\\iconImage.png");
		frame.setIconImage(logo.getImage());
		frame.add(this);
		frame.setTitle("Clue");
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}// constructor

	public static void main(String[] args) {
		new MainMenu();
	}

	// Adds Start button for the first player, and adds the Join button for the rest
	// of the players.
	private void addGameButtons() {

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		GridBagConstraints c01 = new GridBagConstraints();
		c01.gridx = 0;
		c01.gridy = 1;

		GridBagConstraints c11 = new GridBagConstraints();
		c11.gridx = 1;
		c11.gridy = 1;

		GridBagConstraints c02 = new GridBagConstraints();
		c02.gridx = 0;
		c02.gridy = 2;

		GridBagConstraints c03 = new GridBagConstraints();
		c03.gridx = 0;
		c03.gridy = 3;

		// Input text from user
		JLabel enterText = new JLabel("Enter the amount of players (2-6): ");
		JTextField text = new JTextField(5);

		// Copyright
		JLabel cR = new JLabel("This is not intended for commercial purposes.");
		cR.setFont(new Font("Serif", Font.PLAIN, 8));

		// Author's Note
		JLabel authorsNote = new JLabel(
				"Created by Gabriel Rodriguez, Claudia Monge, and Christian Ramos. December 2021.");
		authorsNote.setFont(new Font("Serif", Font.PLAIN, 10));

		// Menu Image
		ImageIcon icon = new ImageIcon("C:\\Users\\djavi\\Desktop\\menu.png");
		JLabel img = new JLabel();
		img.setIcon(icon);

		// Once client opens MainMenu, starts the client thread.
		Client client = new Client();
		Thread thread = new Thread(client);
		thread.start();

		// Submit Button
		JButton submitBtn = new JButton("Submit");
		submitBtn.setFont(new Font("Serif", Font.BOLD, 15));

		submitBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Client.amountofPlayers = Integer.parseInt(text.getText());

				enterText.setVisible(false);

				text.setVisible(false);

				submitBtn.setVisible(false);

				addColorButtons();

			}

		});

		// Start Button
		JButton startGameBTN = new JButton("Start Game");
		startGameBTN.setFont(new Font("Serif", Font.BOLD, 15));
		startGameBTN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				img.setVisible(false);

				authorsNote.setVisible(false);

				cR.setVisible(false);

				startGameBTN.setVisible(false);

				// Gather the amount of players for the first player.
				enterText.setVisible(true);
				text.setVisible(true);
				submitBtn.setVisible(true);

			}

		});

		// Join Button
		JButton joinGameBTN = new JButton("Join Game");
		joinGameBTN.setFont(new Font("Serif", Font.BOLD, 15));

		joinGameBTN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				img.setVisible(false);

				authorsNote.setVisible(false);

				cR.setVisible(false);

				joinGameBTN.setVisible(false);

				addColorButtons();

			}

		});

		this.add(img, c);

		// Show only the start button for the first client,
		// the rest can only join the game.
		if (Client.currTurn == 0)
			this.add(startGameBTN, c01);
		else
			this.add(joinGameBTN, c01);

		this.add(authorsNote, c02);

		this.add(cR, c03);

		enterText.setVisible(false);
		this.add(enterText, c);

		text.setVisible(false);
		this.add(text, c01);

		submitBtn.setVisible(false);
		this.add(submitBtn, c11);

	}

	// Adds buttons to choose color
	private void addColorButtons() {

		JButton greenColor = new JButton("Green");
		greenColor.setFont(new Font("Serif", Font.BOLD, 20));
		greenColor.setBackground(Colors.Green.getColor());

		JButton plumColor = new JButton("Plum");
		plumColor.setFont(new Font("Serif", Font.BOLD, 20));
		plumColor.setBackground(Colors.Plum.getColor());

		JButton scarlettColor = new JButton("Scarlett");
		scarlettColor.setFont(new Font("Serif", Font.BOLD, 20));
		scarlettColor.setBackground(Colors.Scarlett.getColor());

		JButton orchidColor = new JButton("Orchid");
		orchidColor.setFont(new Font("Serif", Font.BOLD, 20));
		orchidColor.setBackground(Colors.Orchid.getColor());

		JButton peacockColor = new JButton("Peacock");
		peacockColor.setFont(new Font("Serif", Font.BOLD, 20));
		peacockColor.setBackground(Colors.Peacock.getColor());

		JButton mustardColor = new JButton("Mustard");
		mustardColor.setFont(new Font("Serif", Font.BOLD, 20));
		mustardColor.setBackground(Colors.Mustard.getColor());

		// If a color is chosen, drops the frame and waits for every player to join.
		greenColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Green");
				frame.dispose();

			}

		});

		// If a color is chosen, drops the frame and waits for every player to join.
		plumColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Plum");
				frame.dispose();

			}

		});

		// If a color is chosen, drops the frame and waits for every player to join.
		scarlettColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Scarlett");
				frame.dispose();

			}

		});

		// If a color is chosen, drops the frame and waits for every player to join.
		orchidColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Orchid");
				frame.dispose();

			}

		});

		// If a color is chosen, drops the frame and waits for every player to join.
		mustardColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Mustard");
				frame.dispose();

			}

		});

		// If a color is chosen, drops the frame and waits for every player to join.
		peacockColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Peacock");
				frame.dispose();

			}

		});

		// Adds to the panel only the available colors
		for (int i = 0; i < Client.availableColors.length; i++) {

			switch (Client.availableColors[i]) {
				case "Green":
					this.add(greenColor);
					break;

				case "Plum":
					this.add(plumColor);
					break;

				case "Orchid":
					this.add(orchidColor);
					break;

				case "Scarlett":
					this.add(scarlettColor);
					break;

				case "Peacock":
					this.add(peacockColor);
					break;

				case "Mustard":
					this.add(mustardColor);
					break;

				default:
					throw new IllegalArgumentException("Unexpected value: " + Client.availableColors[i]);
			}// switch

		} // for

	}// addColorButtons

}// MainMenu