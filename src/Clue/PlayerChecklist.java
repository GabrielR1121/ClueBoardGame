package Clue;

import java.awt.*;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;

/**
 * This class is used for the player checklist. In clue each player receives a
 * checklist where they can rule out posibilities until they can narrow down
 * who, where and what.
 */
public class PlayerChecklist extends JPanel {

    /**
     * Creates a new instance of checklist each time it is called but stores players
     * choices for future use. All player cards are automatically checked if they
     * are in their posesion.
     * Each checklist is designed specifically for each player.
     * 
     * @param playerCards  The cards in the player posesion.
     * @param playerNumber The players assigned number
     * @param assumptions  This arraylist will be filled with 21 false options. Once
     *                     the Player starts eliminating options through diduction
     *                     or assumptions this variable will store that change for
     *                     future use.
     */
    public PlayerChecklist(ArrayList<Integer> playerCards, int playerNumber, ArrayList<Boolean> assumptions) {
        JFrame frame = new JFrame("Player " + (playerNumber + 1) + " Checklist");

        // Sorts the player cards to make it easier search through.
        Collections.sort(playerCards);

        List<Checkbox> characterCheckboxes = new ArrayList<Checkbox>();
        List<Checkbox> roomCheckboxes = new ArrayList<Checkbox>();
        List<Checkbox> weaponCheckboxes = new ArrayList<Checkbox>();

        for (int i = 0; i < 21; i++) {
            // Checks the box if the index is found OR the
            // value is true in playerAssumptions.
            Checkbox checkbox = new Checkbox(Build.cardDeckMap.get(i), playerCards.contains(i) || assumptions.get(i));
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

        this.add(characterSlot);

        ///////////////////////////////////////

        // Setting up panel for rooms
        JPanel roomSlot = new JPanel();

        JPanel roomTitle = new JPanel();

        JLabel r = new JLabel("Rooms");
        r.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 35));
        roomTitle.add(r);
        roomSlot.add(roomTitle);

        for (int i = 0; i < roomCheckboxes.size(); i++)
            roomSlot.add(roomCheckboxes.get(i));

        this.add(roomSlot);

        ////////////////////////////////////

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
        ///////////////////////////////////

        // Adds action listener to save new checks before window closes.
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                // Clears assumtions in order to save new choices made.
                Client.playerAssumptions.clear();

                for (int i = 0; i < characterCheckboxes.size(); i++)
                    Client.playerAssumptions.add(characterCheckboxes.get(i).getState());

                for (int i = 0; i < roomCheckboxes.size(); i++)
                    Client.playerAssumptions.add(roomCheckboxes.get(i).getState());

                for (int i = 0; i < weaponCheckboxes.size(); i++)
                    Client.playerAssumptions.add(weaponCheckboxes.get(i).getState());

            }
        });

        this.setPreferredSize(new Dimension(500, 500));
        this.setFocusable(true);
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

}
