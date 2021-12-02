package Clue;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

import javax.swing.*;

public class MainMenu extends JPanel {

	static final int SCREEN_WIDTH = 842;
	static final int SCREEN_HEIGHT = 872;
	JFrame frame = new JFrame();

	// Gotta work on moving the Color enum alone.

	// Starts for every player to either start a game or join one.
	// This main menu must contain every color available for the user to choose.
	// Eliminates the chosen colors for the next client...

	// Waits for every player to choose a color and join the game,
	// then every player gets the same gameboard with every location for
	// their chosen color.
	public MainMenu() {

		addGameButtons();

		this.setPreferredSize(new Dimension(SCREEN_WIDTH/3, SCREEN_HEIGHT/7));

		this.setFocusable(true);
		frame.setLocationRelativeTo(null);
		frame.add(this);
		frame.setTitle("Clue");
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);

	}//constructor

	private void addGameButtons() {

		JButton startGameBTN = new JButton("Start Game");

		JLabel enterText = new JLabel("Entre la cantidad de jugadores (2-6): ");

		JTextField text = new JTextField(5);
		
		JButton submitBtn = new JButton("Submit");
		
		submitBtn.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				Client.amountofPlayers = Integer.parseInt(text.getText());

				enterText.setVisible(false);

				text.setVisible(false);

				submitBtn.setVisible(false);

				addColorButtons();

			}

		});

		JButton joinGameBTN = new JButton("Join Game");

		startGameBTN.setSize(350, 350);

		startGameBTN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Client client = new Client();
				Thread thread = new Thread(client);
				thread.start();

				startGameBTN.setVisible(false);
				joinGameBTN.setVisible(false);

				//We need to gather the amount of players for the first player
				enterText.setVisible(true);
				text.setVisible(true);
				submitBtn.setVisible(true);

			}

		});


		joinGameBTN.setSize(350, 350);

		joinGameBTN.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Client client = new Client();
				Thread thread = new Thread(client);
				thread.start();
				startGameBTN.setVisible(false);
				joinGameBTN.setVisible(false);
				addColorButtons();

			}

		});

		this.add(startGameBTN);
		this.add(joinGameBTN);
		enterText.setVisible(false);
		this.add(enterText);
		text.setVisible(false);
		this.add(text);
		submitBtn.setVisible(false);
		this.add(submitBtn);


	}

	private void addColorButtons() {

		JButton greenColor = new JButton("Green");
		greenColor.setBackground(Colors.Green.getColor());

		JButton plumColor = new JButton("Plum");
		plumColor.setBackground(Colors.Plum.getColor());

		JButton scarlettColor = new JButton("Scarlett");
		scarlettColor.setBackground(Colors.Scarlett.getColor());

		JButton orchidColor = new JButton("Orchid");
		orchidColor.setBackground(Colors.Orchid.getColor());

		JButton peacockColor = new JButton("Peacock");
		peacockColor.setBackground(Colors.Peacock.getColor());

		JButton mustardColor = new JButton("Mustard");
		mustardColor.setBackground(Colors.Mustard.getColor());


		greenColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Green");
				frame.dispose();

			}

		});

		plumColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Plum");
				frame.dispose();

			}

		});

		scarlettColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Scarlett");
				frame.dispose();

			}

		});

		orchidColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Orchid");
				frame.dispose();

			}

		});

		mustardColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Mustard");
				frame.dispose();

			}

		});

		peacockColor.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Client.colorIdx = Arrays.binarySearch(Client.availableColors, "Peacock");
				frame.dispose();

			}

		});



		for (int i = 0; i < Client.availableColors.length; i++) {


			switch (Client.availableColors[i]) {
			case "Green": {
				this.add(greenColor);
				break;
			}
			case "Plum":{
				this.add(plumColor);
				break;

			}
			case "Orchid": {
				this.add(orchidColor);
				break;
			}
			case "Scarlett": {
				this.add(scarlettColor);
				break;
			}
			case "Peacock": {
				this.add(peacockColor);
				break;
			}
			case "Mustard": {
				this.add(mustardColor);
				break;
			}


			default:
				throw new IllegalArgumentException("Unexpected value: " + Client.availableColors[i]);
			}


		}


	}



}