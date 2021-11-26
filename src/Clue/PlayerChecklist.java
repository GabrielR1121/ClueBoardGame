package Clue;

import java.awt.*;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;

public class PlayerChecklist extends JPanel {

    // Has the string info of all of the cards on the cardDeck array.
    static HashMap<Integer, String> cardDeckMap = new HashMap<Integer, String>();

    public PlayerChecklist(int playerCards[], int playerNumber, ArrayList<Boolean> assumptions) {
        JFrame frame = new JFrame("Player " + (playerNumber + 1) + " Checklist");

        Arrays.sort(playerCards);

        List<Checkbox> characterCheckboxes = new ArrayList<Checkbox>();
        List<Checkbox> roomCheckboxes = new ArrayList<Checkbox>();
        List<Checkbox> weaponCheckboxes = new ArrayList<Checkbox>();

        for (int i = 0; i < 21; i++) {
            // This part here checks the box if the index is found with binary search OR the
            // value is true in playerAssumptions.
            Checkbox checkbox = new Checkbox(cardDeckMap.get(i),
                    Arrays.binarySearch(playerCards, i) >= 0 || assumptions.get(i));
            checkbox.setFont(new Font("TimesRoman", Font.BOLD, 23));
            if (i >= 0 && i < 6)
                characterCheckboxes.add(checkbox);
            else if (i >= 6 && i < 15)
                roomCheckboxes.add(checkbox);
            else
                weaponCheckboxes.add(checkbox);

        }
        // Setting up panel for characters
        JPanel characterSlot = new JPanel();

        JPanel characterTitle = new JPanel();

        JLabel c = new JLabel("Characters");
        c.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 32));
        characterTitle.add(c);
        characterSlot.add(characterTitle);

        for (int i = 0; i < characterCheckboxes.size(); i++)
            characterSlot.add(characterCheckboxes.get(i));
        // chararcterSlot.add(chararcterBoxes);
        this.add(characterSlot);

        // Setting up panel for characters
        JPanel roomSlot = new JPanel();

        JPanel roomTitle = new JPanel();

        JLabel r = new JLabel("Rooms");
        r.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 35));
        roomTitle.add(r);
        roomSlot.add(roomTitle);

        for (int i = 0; i < roomCheckboxes.size(); i++)
            roomSlot.add(roomCheckboxes.get(i));

        this.add(roomSlot);

        // Setting up panel for weapons
        JPanel weaponSlot = new JPanel();
        JLabel w = new JLabel("Weapons");
        w.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 35));
        JPanel weaponTitle = new JPanel();
        weaponTitle.add(w);
        weaponSlot.add(weaponTitle);

        for (int i = 0; i < weaponCheckboxes.size(); i++)
            weaponSlot.add(weaponCheckboxes.get(i));

        this.add(weaponSlot);

        this.setLayout(new GridLayout());

        // Adds action listener to save new checks before window closes

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                // for (int i = 0; i < characterCheckboxes.size(); i++)
                // Client.playerAssumptions.add(characterCheckboxes.get(i).getState());

                // for (int i = 0; i < roomCheckboxes.size(); i++)
                // Client.playerAssumptions.add(roomCheckboxes.get(i).getState());

                // for (int i = 0; i < weaponCheckboxes.size(); i++)
                // Client.playerAssumptions.add(weaponCheckboxes.get(i).getState());

                // System.out.println(Client.playerAssumptions);
            }
        });

        this.setPreferredSize(new Dimension(500, 500));
        this.setFocusable(true);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    // For this code to work in its entirety a Boolean arraylist must be added to
    // client and fill it with 21 false values,
    // The card distribution in server needs to be fixed and an arraylist called
    // playerAssumptions needs to be created in Client.
    public static void main(String[] args) {
        // Hashmap of the info on the cardDeck elements (dudas)
        // Try and refrences this from build or client so we dont repeat code.
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

        // In order for this to work a default value for amount of players was set in
        // server in the meantime.
        new Server();
        // int test[] = new int[Server.playerCards.get(0).size()];

        // for (int i = 0; i < Server.playerCards.get(0).size(); i++) {
        // test[i] = Server.playerCards.get(0).get(i);
        // }
        // System.out.println(Arrays.toString(test));

        // new PlayerChecklist(test, 0, Client.playerAssumptions);
    }

}
