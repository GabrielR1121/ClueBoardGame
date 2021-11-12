package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

//Runs the actual program. In order to understand what needs to be done here i need to understand how the remote server works (ask Christian).
public class ThreadServer implements Runnable {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean isPlayerTurn;
    private LinkedList<Socket> users = new LinkedList<Socket>();
    private int turn;

    public ThreadServer(Socket soc, LinkedList users, int turno) {

        this.users = users;
        socket = soc;
        this.turn = turno;

    }
    @Override
    public void run(){

        System.out.println("Starting ThreadServer...");
        
        try {

            in = new DataInputStream(socket.getInputStream());
            
            out = new DataOutputStream(socket.getOutputStream());
            
            String msg = "Your turn: " + turn;

            out.writeUTF(msg);
    
        } catch (Exception e) {
            //TODO: handle exception
        }
       
    }

}
