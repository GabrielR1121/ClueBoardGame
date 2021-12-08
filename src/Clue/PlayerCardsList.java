package Clue;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.GridLayout;

public class PlayerCardsList {

    public static JFrame cardFrame = new JFrame("Your Deck");


    public PlayerCardsList() {

        //ImageIcon
	    ImageIcon logo = new ImageIcon(".\\ClueBoardGame\\Assets\\GameBoard\\iconImage.png");
        cardFrame.setIconImage(logo.getImage());
        showPlayerCards();

    }

    public static void showPlayerCards() {

        cardFrame.setSize(Build.SCREEN_WIDTH, Build.SCREEN_HEIGHT);
        cardFrame.getContentPane().setLayout(new GridLayout(2, Build.playerCards.size(), 2, 2));

        for (int i = 0; i < Build.playerCards.size(); i++) {
            Build.cardButtons[Build.playerCards.get(i)].setVisible(true);
            cardFrame.getContentPane().add(Build.cardButtons[Build.playerCards.get(i)]);
        }

        cardFrame.pack();
        cardFrame.setLocationRelativeTo(null);
        cardFrame.setVisible(true);

    }
}