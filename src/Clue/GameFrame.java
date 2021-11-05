package Clue;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame() {

        this.add(new Server());
        this.setTitle("Clue");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new GameFrame();
    }

}
