package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeServer {

    private ServerSocket serverSocket;
    
    private static final int PORT = 1529;
    private volatile boolean running = false;



    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        running = true;
        System.out.println("Server started on port: " + PORT);

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, this);
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
                break;
            }
        }
    }

    public void stop() throws IOException {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        for (ClientHandler client : ClientHandler.clients) {
            client.stop();
        }
        System.out.println("Server stopped.");
    }

}
