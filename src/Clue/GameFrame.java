package Clue;

import javax.swing.JFrame;

public class GameFrame extends JFrame {
    // This class could be multi-use for the start, dispute and final rumor methods
    // for their respective windows.
    GameFrame() {

        this.add(new Server());
        this.setTitle("Clue");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
