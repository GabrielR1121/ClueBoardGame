package Clue;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

//Runs the actual program. In order to understand what needs to be done here i need to understand how the remote server works (ask Christian).
public class ThreadServer {

    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean isPlayerTurn;
    private LinkedList<Socket> users = new LinkedList<Socket>();

    public ThreadServer(Socket soc, LinkedList users) {

        this.users = users;
        socket = soc;

    }

}
