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

    public PlayerChecklist(ArrayList<Integer> playerCards, int playerNumber, ArrayList<Boolean> assumptions) {
        JFrame frame = new JFrame("Player " + (playerNumber + 1) + " Checklist");
        Collections.sort(playerCards);

        List<Checkbox> characterCheckboxes = new ArrayList<Checkbox>();
        List<Checkbox> roomCheckboxes = new ArrayList<Checkbox>();
        List<Checkbox> weaponCheckboxes = new ArrayList<Checkbox>();

        for (int i = 0; i < 21; i++) {
            // This part here checks the box if the index is found with binary search OR the
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
                Client.playerAssumptions.clear();
                System.out.println("Hello");

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
