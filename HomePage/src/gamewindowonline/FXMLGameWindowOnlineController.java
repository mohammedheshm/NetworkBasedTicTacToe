package gamewindowonline;

import alert.FXMLLoserController;
import alert.FXMLWinnerController;
import alert.PlayerNameDialogController;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pagemanager.Navigation;
import serverconnection.ServerConnect;

public class FXMLGameWindowOnlineController implements Initializable {

    private String[][] board = new String[3][3];
    public static boolean isUserTurn = true;
    private int userScore = 0;
    private int computerScore = 0;
    private boolean gameWon = false;
    private Random random = new Random();
    private String difficulty;

    Image winImage = new Image(getClass().getResourceAsStream("/resources/win.png"));
    Image noWinner = new Image(getClass().getResourceAsStream("/resources/response.png"));

    private PlayerNameDialogController controller;

    public FXMLGameWindowOnlineController() {
        ServerConnect.fXMLGameWindowOnlineController = this;
    }

    @FXML
    private Text userScoreText;
    @FXML
    private Text computerNameText;
    @FXML
    private Text computerScoreText;

    @FXML
    private Button button00;
    @FXML
    private Button button01;
    @FXML
    private Button button02;
    @FXML
    private Button button10;
    @FXML
    private Button button11;
    @FXML
    private Button button12;
    @FXML
    private Button button20;
    @FXML
    private Button button21;
    @FXML
    private Button button22;
    @FXML
    private ImageView imageView;
    @FXML
    private MediaView mediaView;

    Thread th;
    private final Image xImage = new Image(getClass().getResourceAsStream("/resources/x.png"));
    private final Image oImage = new Image(getClass().getResourceAsStream("/resources/o.png"));
    //private final Image player2Image = new Image(getClass().getResourceAsStream("/resources/Bot.png"));
    public static String opponnent;
    public static String playerSymbol;

    @FXML
    public void handleBackHButton(ActionEvent event) {
        Navigation.nextPage("/localpage/FXMLLocal.fxml");
    }


    @FXML
    public void handleResetButton(ActionEvent event) {
        boolean okClicked = Navigation.showQuitAlert(event, "You Will Lose The Game", "Ok", "Cancel", "/alert/FXMLPlayAgainDialog.fxml");

        if (okClicked) {
            resetGame(); // Reset the game state
            ServerConnect.sendMessage("RESET:" + opponnent); // Notify server of the reset

        }
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (gameWon) {
            return;
        }

        //System.out.println("make new move");
        Button clickedButton = (Button) event.getSource();
        int row = GridPane.getRowIndex(clickedButton) == null ? 0 : GridPane.getRowIndex(clickedButton);
        int col = GridPane.getColumnIndex(clickedButton) == null ? 0 : GridPane.getColumnIndex(clickedButton);

        String i = String.valueOf(row);
        String j = String.valueOf(col);

        if (board[row][col] == null && isUserTurn) {
            makeMove(row, col, false);
            ServerConnect.sendMessage("MOVE:" + opponnent + ":" + i + j);
            if (!gameWon && !isBoardFull()) {
                isUserTurn = !isUserTurn;
            }
        }
    }

    public void makeMove(int row, int col, boolean isOnline) {
        String player = playerSymbol;
        if (isOnline) {
            player = (playerSymbol == "X") ? "O" : "X";
        }
        board[row][col] = player;
        Image image = (player == "X") ? xImage : oImage;
        ImageView imageView = new ImageView(image);
        Button button = getButton(row, col);
        button.setGraphic(imageView);

        if (checkWin()) {
            highlightWinningButtons();
            gameWon = true;
            if (player.equals("X")) {
                userScore++;
                userScoreText.setText(String.valueOf(userScore));
                showGameResult("X");
            } else {
                computerScore++;
                computerScoreText.setText(String.valueOf(computerScore));
                showGameResult("O");
            }
        } else if (isBoardFull()) {
            showGameResult("tie");
            //resetGame();
        }
    }

    private Button getButton(int i, int j) {
        if (i == 0 && j == 0) {
            return button00;
        }
        if (i == 0 && j == 1) {
            return button01;
        }
        if (i == 0 && j == 2) {
            return button02;
        }
        if (i == 1 && j == 0) {
            return button10;
        }
        if (i == 1 && j == 1) {
            return button11;
        }
        if (i == 1 && j == 2) {
            return button12;
        }
        if (i == 2 && j == 0) {
            return button20;
        }
        if (i == 2 && j == 1) {
            return button21;
        } else {
            return button22;
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                return true;
            }
            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                return true;
            }
        }
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            return true;
        }
        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            return true;
        }
        return false;
    }

    private void highlightWinningButtons() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2])) {
                highlightButton(i, 0);
                highlightButton(i, 1);
                highlightButton(i, 2);
            }
            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[1][i].equals(board[2][i])) {
                highlightButton(0, i);
                highlightButton(1, i);
                highlightButton(2, i);
            }
        }
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[1][1].equals(board[2][2])) {
            highlightButton(0, 0);
            highlightButton(1, 1);
            highlightButton(2, 2);
        }
        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[1][1].equals(board[2][0])) {
            highlightButton(0, 2);
            highlightButton(1, 1);
            highlightButton(2, 0);
        }
    }

    private void highlightButton(int row, int col) {
        Button button = getButtonByRowCol(row, col);
        if (button != null) {
            button.setStyle("-fx-background-color: #9D886B;");
        }
    }

    private Button getButtonByRowCol(int row, int col) {
        switch (row) {
            case 0:
                switch (col) {
                    case 0:
                        return button00;
                    case 1:
                        return button01;
                    case 2:
                        return button02;
                }
                break;
            case 1:
                switch (col) {
                    case 0:
                        return button10;
                    case 1:
                        return button11;
                    case 2:
                        return button12;
                }
                break;
            case 2:
                switch (col) {
                    case 0:
                        return button20;
                    case 1:
                        return button21;
                    case 2:
                        return button22;
                }
                break;
        }
        return null;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame() {
        board = new String[3][3];
        resetButtonStyle(button00);
        resetButtonStyle(button01);
        resetButtonStyle(button02);
        resetButtonStyle(button10);
        resetButtonStyle(button11);
        resetButtonStyle(button12);
        resetButtonStyle(button20);
        resetButtonStyle(button21);
        resetButtonStyle(button22);
        if (playerSymbol == "X") {
            isUserTurn = true;
        } else {
            isUserTurn = false;
        }
        gameWon = false;
    }

    private void resetButtonStyle(Button button) {
        button.setGraphic(null);
        button.setStyle("-fx-background-color: #DFD3C3; -fx-background-radius: 15;");
    }

    public void showGameResult(String result) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader;
                String mediaPath;
                MediaView mediaView;
                MediaPlayer mediaPlayer;

                // Load the appropriate FXML and media based on the result
                if (result.equals("X")) {
                    // Player X wins, so Player O loses
                    loader = new FXMLLoader(getClass().getResource("/alert/FXMLWinner.fxml"));
                    mediaPath = "/resources/winner.mp4";
                } else if (result.equals("O")) {
                    // Player O wins, so Player X loses
                    loader = new FXMLLoader(getClass().getResource("/alert/FXMLLoser.fxml"));
                    mediaPath = "/resources/loser.mp4";
                } else {
                    // It's a tie
                    Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml",
                            "No Winner", "Game Over", noWinner);
                    return;
                }

                Parent root = loader.load();

                // Get MediaView from the loaded controller
                if (result.equals("X")) {
                    FXMLWinnerController controllerw = loader.getController();
                    mediaView = controllerw.getMediaView();
                } else {
                    FXMLLoserController controllerl = loader.getController();
                    mediaView = controllerl.getMediaView();
                }

                // Set up and play the media
                Media media = new Media(getClass().getResource(mediaPath).toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaView.setFitWidth(820);
                mediaView.setFitHeight(450);
                mediaPlayer.play();

                // Create and show the result stage
                Stage stage = new Stage();
                stage.setTitle("Game Over");
                stage.setResizable(false);
                Scene scene = new Scene(root);
                stage.setScene(scene);

                // Stop the media player if the stage is closed
                stage.setOnCloseRequest(event -> {
                    if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                        mediaPlayer.stop();
                    }
                });

                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(FXMLGameWindowOnlineController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userScoreText.setText(String.valueOf(userScore));
        computerScoreText.setText(String.valueOf(computerScore));

        computerNameText.setText("Player2");

    }
}
