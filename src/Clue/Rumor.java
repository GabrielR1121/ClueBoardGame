/**
* The Rumor.java program is specialized in handling the
* rumors and disputions of the game.
*
* @author  Claudia P. Monge Torres
* @author  Gabriel E. Rodriguez Garcia
* @author  Christian J. Ramos Ortega
* @version 1.0
* @since   12-2021
*/
package Clue;

//Imports.
import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Rumor {

    // Starting index for the character.
    public volatile static int rumorCharacterIdx = -1;
    // Starting index for the weapon.
    public volatile static int rumorWeaponIdx = -1;
    // Starting index for the room.
    public volatile static int rumorRoomIdx = -1;

    // Starting index for the character.
    public volatile static int finalRumorCharacterIdx = -1;
    // Starting index for the weapon.
    public volatile static int finalRumorWeaponIdx = -1;
    // Starting index for the room.
    public volatile static int finalRumorRoomIdx = -1;

    // Starting index for the card disputed.
    public volatile static int cardDisputed = -2;
    // Starting array index for the disputed cards.
    public static int relevantDisputeCards[] = { -1, -1, -1 };

    private static int count = -1;

    // Booleans for verification.
    public volatile static boolean finalRumor = false;
    public volatile static boolean isEliminated = false;
    public volatile static boolean playerWon = false;

    // Frame initiations.
    public static JFrame rumorframe = new JFrame("Rumor");
    public static JFrame disputeFrame = new JFrame("Dispute Rumor");
    public static JFrame finalFrame = new JFrame("Final Rumor");
    public static JFrame resultFrame = new JFrame("Results");
    public static JFrame disputedCardFrame = new JFrame("Disputed Cards");

    /**
     * Starts whether it's the final or regular rumor.
     * 
     * @param room - String of the room the user entered.
     */
    public static void startRumor(String room) {

        // Starts the final rumor
        if (room == "DecisionRoom") {
            finalRumor = true;
            startFinalRumor();
        }

        for (Map.Entry<Integer, String> entry : Build.cardDeckMap.entrySet()) {

            // Starts a regular rumor with the string of the room entered as a "where"
            // index.
            if (entry.getValue() == room) {
                // Gets the index of the room the user entered from cardDeckMap.
                rumorRoomIdx = entry.getKey();
                startRegularRumor(room);
            }

        }

    }

    /**
     * Creates the frame for the final rumor.
     */
    private static void startFinalRumor() {

        finalFrame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
        finalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        finalFrame.setResizable(false);

        // Fills the checkboxes for the rumors.
        rumorCheckBox(finalFrame.getContentPane(), "");

        finalFrame.pack();
        finalFrame.setLocationRelativeTo(null);
        finalFrame.setVisible(true);

    }

    /**
     * Last part of the final rumor. Checks if ALL of the rumored cards
     * are equal to the cards on the secret folder.
     */
    public static void checkSecretFolder() {

        resultFrame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);

        boolean win = false;

        // Check if the player has ALL of the cards on the secret folder.
        if (Build.secretCards.get(0) == finalRumorCharacterIdx &&
                Build.secretCards.get(1) == finalRumorRoomIdx &&
                Build.secretCards.get(2) == finalRumorWeaponIdx) {
            win = true;
        }

        playerEndScreen(resultFrame.getContentPane(), win);

        resultFrame.pack();
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);

    }

    /**
     * Checks if the boolean sent by the user is false or true.
     * If false: Player looses the game.
     * If true: Player wins the game.
     * 
     * @param pane - The content pane of the result frame.
     * @param win  - boolean to check the player status.
     */
    private static void playerEndScreen(Container pane, boolean win) {

        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String text = "";
        if (win == true) {
            text = "WIN";
            playerWon = true;
            Build.gameEnded = true;
        } else {
            text = "Your rumor was incorrect and you have been eliminated. You can still"
                    + " dispute rumors. The correct cards were:";
        }
        // Boolean to stop the eliminated player from still moving.
        isEliminated = true;

        JTextArea endingPosLabel = new JTextArea(text);
        endingPosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(endingPosLabel);
        showCorrectCards(resultFrame.getContentPane());

    }

    /**
     * Shows the cards that were inside of the secret folder.
     * 
     * @param pane- The content pane of the result frame
     */
    public static void showCorrectCards(Container pane) {

        pane.setLayout(new GridLayout(4, 1, 2, 2));

        for (int i = 0; i < Build.secretCards.size(); i++) {

            pane.add(Build.cardButtons[Build.secretCards.get(i)]);

        }

    }

    /**
     * Creates the frame for the regular rumor.
     * 
     * @param room - the string of the room the user entered.
     */
    public static void startRegularRumor(String room) {

        rumorframe.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
        rumorframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        rumorframe.setResizable(false);

        rumorCheckBox(rumorframe.getContentPane(), room);

        rumorframe.pack();
        rumorframe.setLocationRelativeTo(null);
        rumorframe.setVisible(true);

    }

    /**
     * Shows the user the succeeding client's disputed card.
     */
    public static void showDisputedCard(int card) {
        if (card != -1) {
            disputedCardFrame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
            disputedCardFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            disputedCardFrame.setResizable(false);

            Build.cardButtons[card].setVisible(true);
            disputedCardFrame.getContentPane().add(Build.cardButtons[card]);

        } else {
            JLabel rumorLabel = new JLabel("The other player had no cards to show you!");
            rumorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            disputedCardFrame.getContentPane().add(rumorLabel);
        }
        disputedCardFrame.pack();
        disputedCardFrame.setLocationRelativeTo(null);
        disputedCardFrame.setVisible(true);

        disputedCardFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                disputedCardFrame.getContentPane().removeAll();
                disputedCardFrame.dispose();
            }
        });

    }

    /**
     * Adds the cards into check boxes so the user can create a rumor.
     * Gets the index of the selected items for later use
     * 
     * @param pane - For the JFrame.
     * @param room - String of the room name.
     */
    public static void rumorCheckBox(Container pane, String room) {

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        JButton sendRumorBTN = new JButton("Send Rumor");
        JButton cancelRumorBTN = new JButton("Cancel Rumor");
        JComboBox<String> rooms = new JComboBox<String>(); // Created but not added ONLY if its a final rumor.

        if (finalRumor == true) {
            JLabel rumorLabel = new JLabel("THIS IS THE FINAL RUMOR!");
            rumorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            pane.add(rumorLabel);

            // Adding the rooms on check boxes and adding them to the JFrame.
            fillComboBox(rooms, 6, 15);
            rooms.setAlignmentX(Component.CENTER_ALIGNMENT);
            pane.add(rooms);

        } else {
            JLabel rumorLabel = new JLabel(" The supposed killer is and used the "
                    + "following to commit the murder in the: " + room + " ");
            rumorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            pane.add(rumorLabel);
        }

        // Adding characters onto the check boxes and adding them to the JFrame.
        JComboBox<String> characters = new JComboBox<String>();
        fillComboBox(characters, 0, 6);
        characters.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(characters);

        // Adding weapons onto the check boxes and adding them to the JFrame.
        JComboBox<String> weapons = new JComboBox<String>();
        fillComboBox(weapons, 15, 21);
        weapons.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(weapons);

        sendRumorBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(sendRumorBTN);
        sendRumorBTN.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                // Gets the index of the rumored room.
                if (finalRumor == true) {

                    finalRumorRoomIdx = Build.cardDeckMap.size() - weapons.getItemCount() -
                            (rooms.getItemCount() - rooms.getSelectedIndex());

                    // Gets the index of the rumored characters.
                    finalRumorCharacterIdx = characters.getSelectedIndex();
                    // Gets the index of the rumored characters.
                    finalRumorWeaponIdx = Build.cardDeckMap.size() -
                            (weapons.getItemCount() - weapons.getSelectedIndex());
                    checkSecretFolder();
                } else {

                    // Gets the index of the rumored characters.
                    rumorCharacterIdx = characters.getSelectedIndex();
                    // Gets the index of the rumored characters.
                    rumorWeaponIdx = Build.cardDeckMap.size() -
                            (weapons.getItemCount() - weapons.getSelectedIndex());

                }
                if (finalRumor == true) {
                    finalFrame.dispose();

                } else {
                    rumorframe.getContentPane().removeAll();
                    rumorframe.dispose();
                }

            }

        });

        // Cancel button.
        cancelRumorBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(cancelRumorBTN);
        cancelRumorBTN.addActionListener(new ActionListener() {

            // Verifies if the cancel button is activated.
            public void actionPerformed(ActionEvent e) {

                if (finalRumor == true) {
                    finalFrame.getContentPane().removeAll();
                    finalFrame.dispose();
                } else {
                    rumorframe.getContentPane().removeAll();
                    rumorframe.dispose();
                }

            }

        });

    }

    /**
     * Fills the combo box with the information from cardDeckMap.
     * 
     * @param cBox  - combo box.
     * @param start - Starting index sent.
     * @param end   - Ending index sent.
     */
    public static void fillComboBox(JComboBox<String> cBox, int start, int end) {

        for (int i = start; i < end; i++) {
            cBox.addItem(Build.cardDeckMap.get(i));
        }

    }

    /**
     * Creates the frame for the dispute rumor.
     */
    static void disputeRumor() {

        disputeFrame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
        // disputeFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // disputeFrame.setResizable(false);
        addDisputeButtons(disputeFrame.getContentPane(), Build.playerCards);

        disputeFrame.pack();
        disputeFrame.setLocationRelativeTo(null);
        disputeFrame.setVisible(true);

    }

    /**
     * Adds the cards that the player has that matches with the indexes
     * sent from starting a regular rumor and creates a button for
     * each one of them.
     * 
     * @param pane        - For the JFrame.
     * @param playerCards - Gets the specified client's cards.
     */
    public static void addDisputeButtons(Container pane, ArrayList<Integer> playerCards) {

        disputeFrame.getContentPane().removeAll();

        pane.setLayout(new GridLayout(1, 3, 2, 2));

        for (int i = 0; i < playerCards.size(); i++) {

            if (playerCards.get(i) == rumorCharacterIdx || playerCards.get(i) == rumorRoomIdx
                    || playerCards.get(i) == rumorWeaponIdx) {
                count++;
                relevantDisputeCards[count] = playerCards.get(i);

                // Creates the buttons for the relevant cards.
                Build.cardButtons[playerCards.get(i)].setVisible(true);

                pane.add(Build.cardButtons[playerCards.get(i)]);

            }

        }

        // Checks which button the user pressed.
        if (relevantDisputeCards[0] != -1)
            Build.cardButtons[relevantDisputeCards[0]].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    cardDisputed = relevantDisputeCards[0];
                    System.out.println("Disputed Card: " + cardDisputed);
                    disputeFrame.getContentPane().removeAll();
                    disputeFrame.dispose();

                }

            });
        if (relevantDisputeCards[1] != -1)
            Build.cardButtons[relevantDisputeCards[1]].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    cardDisputed = relevantDisputeCards[1];
                    System.out.println("Disputed Card: " + cardDisputed);
                    disputeFrame.getContentPane().removeAll();
                    disputeFrame.dispose();

                }

            });

        if (relevantDisputeCards[2] != -1)
            Build.cardButtons[relevantDisputeCards[2]].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    cardDisputed = relevantDisputeCards[2];
                    System.out.println("Disputed Card: " + cardDisputed);
                    disputeFrame.getContentPane().removeAll();
                    disputeFrame.dispose();

                }

            });

        // If the user doesn't have any cards to dispute, the following will be sent.
        if (count == -1) {
            JLabel label = new JLabel("You do not have any relevant cards at this moment.");
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            pane.add(label);

            disputeFrame.dispose();
            // Use this to move to next player. Use bool value.
            cardDisputed = -1;
        }

    }

    public static void reset() {
        // Starting index for the character.
        rumorCharacterIdx = -1;
        // Starting index for the weapon.
        rumorWeaponIdx = -1;
        // Starting index for the room.
        rumorRoomIdx = -1;
        // Starting index for the card disputed.
        cardDisputed = -2;
        // Starting array index for the disputed cards.
        Arrays.fill(relevantDisputeCards, -1);

        count = -1;

    }
}