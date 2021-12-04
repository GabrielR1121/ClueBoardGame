package Clue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import javax.swing.BoxLayout;

public class Rumor {

    public volatile static int rumorCharacterIdx = -1;
    public volatile static int rumorWeaponIdx = -1;
    public volatile static int rumorRoomIdx = -1;
    public volatile static int cardDisputed = -1; // card that the user will dispute
    public static int relevantDisputeCards[] = { -1, -1, -1 };
    public static int count = -1;
    public static JFrame frame = new JFrame("Rumor");
    public static JFrame disputeFrame = new JFrame("Dispute Rumor");
    // public volatile static boolean test = false;

    public static void startRumor(String room) {
        // test= true;
        for (Map.Entry<Integer, String> entry : Build.cardDeckMap.entrySet()) {

            if (room == "DecisionRoom")
                System.out.println("AQUI VA FINAL RUMOR");

            if (entry.getValue() == room) {
                rumorRoomIdx = entry.getKey();
                startRegularRumor(room);
            }

        }

    }

    static void disputeRumor() {

        disputeFrame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
        // disputeFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // disputeFrame.setResizable(false);
        addDisputeButtons(disputeFrame.getContentPane(), Build.playerCards);

        disputeFrame.pack();
        disputeFrame.setLocationRelativeTo(null);
        disputeFrame.setVisible(true);

    }

    public static void addDisputeButtons(Container pane, ArrayList<Integer> playerCards) {

        pane.setLayout(new GridLayout(1, 3, 2, 2));

        for (int i = 0; i < playerCards.size(); i++) {

            if (playerCards.get(i) == rumorCharacterIdx || playerCards.get(i) == rumorRoomIdx
                    || playerCards.get(i) == rumorWeaponIdx) {
                count++;
                relevantDisputeCards[count] = playerCards.get(i);

                Build.cardButtons[playerCards.get(i)].setVisible(true);

                pane.add(Build.cardButtons[playerCards.get(i)]);
                System.out.println("Count: " + count);
            }

        }
        System.out.println("relevant disputed cards " + Arrays.toString(relevantDisputeCards));

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

        if (count == 0) {
            JLabel label = new JLabel("You do not have any relevant cards at this moment.");
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            pane.add(label);
            disputeFrame.getContentPane().removeAll();
            disputeFrame.dispose();
            // Use this to move to next player. Use bool value.
        }
    }

    public static void startRegularRumor(String room) {

        frame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);

        rumorCheckBox(frame.getContentPane(), room);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void rumorCheckBox(Container pane, String room) {

        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

        JButton sendRumorBTN = new JButton("Send Rumor");
        JButton cancelRumorBTN = new JButton("Cancel Rumor");

        JLabel rumorLabel = new JLabel(" The supposed killer is and used the "
                + "following to commit the murder in the: " + room + " ");
        rumorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(rumorLabel);

        JComboBox<String> characters = new JComboBox<String>();
        fillComboBox(characters, 0, 6);
        characters.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(characters);
        JComboBox<String> weapons = new JComboBox<String>();
        fillComboBox(weapons, 15, 21);
        weapons.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(weapons);

        sendRumorBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(sendRumorBTN);
        sendRumorBTN.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                rumorCharacterIdx = characters.getSelectedIndex();
                rumorWeaponIdx = Build.cardDeckMap.size() -
                        (weapons.getItemCount() - weapons.getSelectedIndex());

                Object rumorCharacterName = characters.getSelectedItem();
                Object rumorWeaponName = weapons.getSelectedItem();

                System.out.println(rumorCharacterIdx + "(" + rumorCharacterName + ") with " +
                        rumorWeaponIdx + "(" + rumorWeaponName + ") in " + room);
                // test =false;
                frame.getContentPane().removeAll();
                frame.dispose();

                // To be removed.
                // disputeRumor();

            }

        });

        cancelRumorBTN.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.add(cancelRumorBTN);
        cancelRumorBTN.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                frame.getContentPane().removeAll();
                frame.dispose();

            }

        });

    }

    public static void fillComboBox(JComboBox<String> cBox, int start, int end) {

        for (int i = start; i < end; i++) {
            cBox.addItem(Build.cardDeckMap.get(i));
        }

    }

}