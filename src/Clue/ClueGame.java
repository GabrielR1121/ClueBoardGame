package Clue;

//Main for the whole game. This should be the main start so we are able to add the main menu in the future;
public class ClueGame {

    public static void main(String[] args) {

        Client client = new Client();
        Thread thread = new Thread(client);
        thread.start();
        
        new Build();
    }
}
