package serverconnection;

import auth.FXMLLoginController;
import auth.FXMLSignUpController;
import gamewindowonline.FXMLGameWindowOnlineController;
import invitetoplay.FXMLInviteController;
import ipaddresspage.FXMLIpAddressController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import pagemanager.Navigation;

public class ServerConnect {

    private static int[] move;
    private static String SERVER_ADDRESS = "localhost"; // IP 
    private static final int SERVER_PORT = 1529;
    private static Socket socket;
    private static PrintStream out;
    private static BufferedReader in;
    private static volatile boolean running = false;

    public static FXMLInviteController fXMLInviteController;
    public static FXMLGameWindowOnlineController fXMLGameWindowOnlineController;
    public static FXMLSignUpController fXMLSignUpController;

    public static void makeConnectionWithServer() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintStream(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;
            receiveMessageLogic();
        }
    }

    public static void setSERVER_ADDRESS(String SERVER_ADDRESS) {
        ServerConnect.SERVER_ADDRESS = SERVER_ADDRESS;
    }

    public static void sendMessage(String message) {
        out.println(message);
    }

    public static String receiveMessage() throws IOException {
        return in.readLine();
    }

    public static void closeConnection() throws IOException {
        running = false;
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
        }

    }

    private static void receiveMessageLogic() {

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        String msg = in.readLine();
                        String[] parts = msg.split(":");
                        switch (parts[0]) {

                            case "LOGIN_SUCCESS": {
                                Platform.runLater(() -> {
                                    FXMLLoginController.userEmail = parts[1];
                                    Navigation.nextPage("/invitetoplay/FXMLInvite.fxml");
                                    ServerConnect.sendMessage("GETPLAYERSSTATUS:" + FXMLLoginController.userEmail);

                                });
                                break;
                            }
                            case "LOGIN_FAIL": {

                                break;
                            }
                            case "ALLUSERS": {
                                Platform.runLater(() -> {
                                    fXMLInviteController.updateView(parts);
                                });
                                break;
                            }
                            case "INVITE_REQUEST": {
                                Platform.runLater(() -> {
                                    fXMLInviteController.showAlert("Invitation", parts[1]);
                                });
                                break;
                            }
                            case "ACCEPT_INVITE": {
                                Platform.runLater(() -> {

                                    fXMLInviteController.navigateToBoard();
                                    FXMLGameWindowOnlineController.opponnent = parts[1];
                                    FXMLGameWindowOnlineController.isUserTurn = true;
                                    FXMLGameWindowOnlineController.playerSymbol = "X";
                                });
                                break;
                            }
                            case "MOVE": {
                                Platform.runLater(() -> {
                                    FXMLGameWindowOnlineController.isUserTurn = !FXMLGameWindowOnlineController.isUserTurn;
                                    fXMLGameWindowOnlineController.makeMove(Integer.valueOf(parts[1].charAt(0) - '0'), Integer.valueOf(parts[1].charAt(1) - '0'), true);

                                });

                                break;
                            }
                            case "DECLINE_INVITE": {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Invitation");
                                    alert.setHeaderText("Refused");
                                    alert.setContentText("Request refused.");
                                    alert.show();
                                });

                                break;
                            }
                            case "RESET": {
                                Platform.runLater(() -> {
                                    fXMLGameWindowOnlineController.resetGame();
                                });

                                break;

                            }

                            case "USER_EXIST": {
                                Platform.runLater(() -> {
                                    Image noWinner = new Image(getClass().getResourceAsStream("/resources/response.png"));
                                    Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", "An account with this email already exists.", "Error", noWinner);

                                });
                                break;
                            }
                            case "REGISTER_SUCCESS": {
                                Platform.runLater(() -> {
                                    Image noWinner = new Image(getClass().getResourceAsStream("/resources/response.png"));
                                    Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", "Account created successfully!", "Success", noWinner);

                                });
                                break;
                            }

                        }

                    } catch (IOException ex) {
                        Logger.getLogger(ServerConnect.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }.start();
    }

    public static void setMove(String part) {
        int i = Integer.valueOf(part.charAt(0)) - '0';
        int j = Integer.valueOf(part.charAt(1)) - '0';
        int[] arr = {i, j};
        move = arr;
    }

    public static int[] getMove() {
        return move;
    }

    public static boolean isConnection() throws IOException {
        return running;
    }
}
