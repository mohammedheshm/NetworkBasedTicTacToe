package server;

import Dao.UserManager;
import models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread{

    private Socket clientSocket;
    private TicTacToeServer server;
    private PrintStream out;
    private BufferedReader in;
    String useremail;
    
    public static Vector<ClientHandler> clients;
    
    static 
    {
        clients = new Vector<>();
    }

    public ClientHandler(Socket socket, TicTacToeServer server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            out = new PrintStream(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        clients.add(this);
        start();
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                handleClientMessage(message);
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void handleClientMessage(String message) {
        String[] parts = message.split(":");

        switch (parts[0]) {
            case "LOGIN":
                handleLogin(parts[1], parts[2]);
                break;
            case "ISEXIST":
                handleISEXIST(parts[1],parts[2],parts[3]);
                break;
            case "GETPLAYERSSTATUS":
                handleGetAll(parts[1]);
                break;
            case "INVITE":
                handleInvite(parts[1], parts[2]);
                break;
            case "ACCEPT_INVITE":
                handleAcceptInvite(parts[2] , parts[1]);
                break;

            case "DECLINE_INVITE":
                handleDeclineInvite(parts[2], parts[1]);
                break;
            case "MOVE":
            {
                setMove(parts[1],parts[2]);
                break;
            }
            case "RESET" : 
            {
                resetGame(parts[1]);
            }
            default:
                System.out.println("Unknown message type: " + parts[0]);
        }
    }
    
    private void resetGame(String op)
    {
        for (ClientHandler client : clients) {
            if (client.getUseremail() != null && client.getUseremail().equals(op)) {
                client.sendMessage("RESET:");
            }
        }
    }
    
    
    private void setMove(String op,String move)
    {
         for (ClientHandler client : clients) {
            if (client.getUseremail() != null && client.getUseremail().equals(op)) {
                client.sendMessage("MOVE:" + move);
            }
        }
    }

    private void handleInvite(String invitedUserEmail, String userEmail) {
        for (ClientHandler client : clients) {
            if (client.getUseremail() != null && client.getUseremail().equals(invitedUserEmail)) {
                client.sendMessage("INVITE_REQUEST:" + userEmail);
            }
        }
    }

    private void handleDeclineInvite(String invitingUser, String invitedUser) {
        for (ClientHandler client : clients) {
            if (client.getUseremail().equals(invitingUser)) {
                client.sendMessage("DECLINE_INVITE:" + invitedUser);
                return;
            }
        }
        sendMessage("INVITE_NOT_FOUND");
    }

    private void handleLogin(String useremail, String password) {
        try {
            if (UserManager.validateUser(useremail, password)) {
                User user = UserManager.getUserByUseremail(useremail);
                if (user != null) {
                    user.setStatus(true);
                    UserManager.updateUserStatus(user.getUseremail(), true);
                    this.useremail = user.getUseremail();
                    sendMessage("LOGIN_SUCCESS:" + useremail);
                } else {
                    sendMessage("LOGIN_FAIL");
                }
            } else {
                sendMessage("LOGIN_FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("LOGIN_ERROR");
        }
    }


    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUseremail() {
        return useremail;
    }

 
    private void handleISEXIST(String username,String password,String email) {
        boolean isExist = UserManager.isExist(email);
        if (isExist) {
            sendMessage("USER_EXIST");
        } else {
            boolean success = UserManager.registerUser(username, password, email);
            if (success) {
                sendMessage("REGISTER_SUCCESS");
            } else {
                sendMessage("REGISTER_FAIL");
            }
        }
    }

    private void handleGetAll(String email) {
        List<User> users = UserManager.getAllUsers();
        StringBuilder msg = new StringBuilder();
        for (User u : users) {
            if (!u.getUseremail().equals(email)) {
                msg.append(u.getUsername()).append("&")
                        .append(u.getScore()).append("&")
                        .append(u.getStatus()).append("&")
                        .append(u.getUseremail()).append(":");
            }
        }
        if (msg.length() > 0) {
            sendMessage("ALLUSERS:" + msg.toString());
        } else {
            sendMessage("NO_USERS_TO_SHOW");
        }
    }

    private void handleAcceptInvite(String invitedEmail , String invitingEmail) {

        boolean inviteResponse = false;
        for (ClientHandler client : clients) {
            if (client.getUseremail() != null && client.getUseremail().equals(invitedEmail)) {
                client.sendMessage("ACCEPT_INVITE:" + invitingEmail);
                inviteResponse = true;
            }
        }
        if (inviteResponse) {
            sendMessage("INVITE_RESPONSE_SENT");
        } else {
            sendMessage("INVITE_RESPONSE_FAIL");
        }
    }










private void sendGameResult(String winnerEmail, String loserEmail) {
    for (ClientHandler client : clients) {
        if (client.getUseremail().equals(winnerEmail)) {
            client.sendMessage("GAME_RESULT:WINNER");
        } else if (client.getUseremail().equals(loserEmail)) {
            client.sendMessage("GAME_RESULT:LOSER");
        }
    }
}



}
