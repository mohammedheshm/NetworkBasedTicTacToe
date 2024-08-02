package gamewindow;

import alert.FXMLLoserController;
import alert.FXMLWinnerController;
import alert.PlayerNameDialogController;
import gamewindowonline.FXMLGameWindowOnlineController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import localpage.FXMLLocalController;
import pagemanager.Navigation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import local_modes.Local_ModesController;

public class FXMLGameWindowController implements Initializable {

    private String[][] board = new String[3][3];
    private boolean isUserTurn = true;
    private int userScore = 0;
    private int computerScore = 0;
    private boolean gameWon = false;
    private Random random = new Random();
    private String difficulty;
    private PlayerNameDialogController controller;
    private List<String> moveHistory = new ArrayList<>();

    @FXML
    private Text userScoreText;
    @FXML
    private Text computerNameText;
    @FXML
    private Text computerScoreText;

    @FXML
    private Text userNameText;

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

    private final Image xImage = new Image(getClass().getResourceAsStream("/resources/x.png"));
    private final Image oImage = new Image(getClass().getResourceAsStream("/resources/o.png"));
    private final Image player2Image = new Image(getClass().getResourceAsStream("/resources/humanavatar1.png"));
    Image winImage = new Image(getClass().getResourceAsStream("/resources/win.png"));
    Image noWinner = new Image(getClass().getResourceAsStream("/resources/response.png"));

    @FXML
    public void handleBackHButton(ActionEvent event) {
        if (Local_ModesController.isTwoPlayers) {
            Navigation.nextPage("/local_modes/Local_Modes.fxml");
        } else {
            if (!gameWon) {
                boolean okClicked = Navigation.showQuitAlert(event, "Do You Want to Quit The Game", "Yes", "Cancel", "/alert/FXMLPlayAgainDialog.fxml");
                if (okClicked) {
                    Navigation.nextPage("/localpage/FXMLLocal.fxml");

                }

            } else {
                Navigation.nextPage("/localpage/FXMLLocal.fxml");

            }
        }
    }

    @FXML
    public void handleScreenRecordButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game Record File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            saveGameState(file);
        }
    }

    private void saveGameState(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("User: " + userNameText.getText() + "\n");
            writer.write("Computer: " + computerNameText.getText() + "\n");
            writer.write("User Score: " + userScore + "\n");
            writer.write("Computer Score: " + computerScore + "\n");

            int moveCount = 0;
            for (String move : moveHistory) {
                writer.write(moveCount + " " + move + "\n");
                moveCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void recordMove(String player, int row, int col) {
        String move = player + " " + row + "," + col;
        moveHistory.add(move);
    }

    @FXML
    public void handleResetButton(ActionEvent event) {

        if (Local_ModesController.isTwoPlayers) {
            resetGame();
        } else {
            if (!gameWon) {
                boolean okClicked = Navigation.showQuitAlert(event, "You Will Lose The Game", "Ok", "Cancel", "/alert/FXMLPlayAgainDialog.fxml");
                if (okClicked) {
                    computerScore++;
                    computerScoreText.setText(String.valueOf(computerScore));
                    resetGame();
                } else if (okClicked) {
                    resetGame();
                }
            } else {
                resetGame();
            }
        }
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        if (gameWon) {
            return;
        }

        Button clickedButton = (Button) event.getSource();
        int row = GridPane.getRowIndex(clickedButton) == null ? 0 : GridPane.getRowIndex(clickedButton);
        int col = GridPane.getColumnIndex(clickedButton) == null ? 0 : GridPane.getColumnIndex(clickedButton);

        if (board[row][col] == null) {
            String currentPlayer = isUserTurn ? "X" : "O";
            Image currentImage = isUserTurn ? xImage : oImage;
            makeMove(clickedButton, row, col, currentPlayer, currentImage);

            if (!gameWon && !isBoardFull()) {
                if (Local_ModesController.isTwoPlayers) {
                    isUserTurn = !isUserTurn;
                } else {
                    isUserTurn = false;
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(event1 -> computerMove());
                    pause.play();
                }
            }
        }
    }

    private void makeMove(Button button, int row, int col, String player, Image image) {
        board[row][col] = player;
        ImageView imageView = new ImageView(image);
        button.setGraphic(imageView);

        // Record the move
        recordMove(player, row, col);

        if (checkWin()) {
            highlightWinningButtons();
            gameWon = true;
            if (player.equals("X")) {
                userScore++;
                userScoreText.setText(String.valueOf(userScore));
                showGameResult("win");
            } else {
                computerScore++;
                computerScoreText.setText(String.valueOf(computerScore));
                showGameResult("lose");
            }
        } else if (isBoardFull()) {
            showGameResult("tie");
            resetGame();
        }
    }

    private void computerMove() {
        switch (difficulty) {
            case "easy":
                easyMove();
                break;
            case "medium":
                mediumMove();
                break;
            case "hard":
                hardMove();
                break;
        }

        if (checkWin()) {
            highlightWinningButtons();
            gameWon = true;
        } else if (isBoardFull()) {
            showGameResult("tie");
            resetGame();
        } else {
            isUserTurn = true;
        }

    }

    //Handle Easy Mode With Computer
    private void easyMove() {
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != null);

        Button button = getButtonByRowCol(row, col);
        if (button != null) {
            makeMove(button, row, col, "O", oImage);
        }
    }

    //Handle Medium Mode With Computer
    private void mediumMove() {
        if (!blockUserWin()) {
            easyMove();
        }
    }

    // Block User
    private boolean blockUserWin() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) {
                    board[i][j] = "X";
                    if (checkWin()) {
                        board[i][j] = "O";
                        Button button = getButtonByRowCol(i, j);
                        if (button != null) {
                            makeMove(button, i, j, "O", oImage);
                        }
                        return true;
                    }
                    board[i][j] = null;
                }
            }
        }
        return false;
    }

    // Handel Hard Mode With Computer
    private void hardMove() {
        int[] bestMove = minimax(board, true);
        int row = bestMove[0];
        int col = bestMove[1];
        Button button = getButtonByRowCol(row, col);
        if (button != null) {
            makeMove(button, row, col, "O", oImage);
        }
    }

    // MinMax Algorithm
    private int[] minimax(String[][] board, boolean isMaximizing) {
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = {-1, -1, isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE};

        if (checkWin()) {
            return new int[]{-1, -1, isMaximizing ? -1 : 1};
        }

        if (isBoardFull()) {
            return new int[]{-1, -1, 0};
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) {
                    board[i][j] = isMaximizing ? "O" : "X";
                    int[] move = minimax(board, !isMaximizing);
                    board[i][j] = null;
                    int score = move[2];

                    if (isMaximizing && score > bestScore || !isMaximizing && score < bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestMove[2] = score;
                    }
                }
            }
        }

        return bestMove;
    }

    // Check Win 
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

    // Highlight Winer Buttons
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

    private void resetGame() {
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
        isUserTurn = true;
        gameWon = false;
    }

    private void resetButtonStyle(Button button) {
        button.setGraphic(null);
        button.setStyle("-fx-background-color: #DFD3C3; -fx-background-radius: 15;");
    }

    private void showGameResult(String result) {
        Platform.runLater(() -> {
            try {
                if (Local_ModesController.isTwoPlayers) {
                    switch (result) {
                        case "win":
                            String playerName1 = controller.getPlayer1Name();
                            String player1Message = "Congratulations! " + (!playerName1.isEmpty() ? playerName1 : "Player 1") + " wins.";
                            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", player1Message, "Game Over", winImage);
                            break;
                        case "lose":
                            String playerName2 = controller.getPlayer2Name();
                            String player2Message = "Congratulations! " + (!playerName2.isEmpty() ? playerName2 : "Player 2") + " wins.";
                            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml", player2Message, "Game Over", winImage);
                            break;
                        case "tie":
                            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml",
                                    "No Winner", "Game Over", noWinner);
                            break;
                    }

                } else {
                    FXMLLoader loader;
                    switch (result) {
                        case "win":
                            loader = new FXMLLoader(getClass().getResource("/alert/FXMLWinner.fxml"));
                            break;
                        case "lose":
                            loader = new FXMLLoader(getClass().getResource("/alert/FXMLLoser.fxml"));
                            break;
                        case "tie":
                            Navigation.showAlert(null, "/alert/FXMLSingleButtonAlert.fxml",
                                    "No Winner", "Game Over", noWinner);
                            return;
                        default:
                            return;
                    }

                    Parent root = loader.load();
                    MediaView mediaView;
                    final MediaPlayer mediaPlayer;

                    if (result.equals("win")) {
                        FXMLWinnerController controller = loader.getController();
                        mediaView = controller.getMediaView();
                        Media winMedia = new Media(getClass().getResource("/resources/winner.mp4").toExternalForm());
                        mediaPlayer = new MediaPlayer(winMedia);
                    } else {
                        FXMLLoserController controller = loader.getController();
                        mediaView = controller.getMediaView();
                        Media loseMedia = new Media(getClass().getResource("/resources/loser.mp4").toExternalForm());
                        mediaPlayer = new MediaPlayer(loseMedia);
                    }

                    mediaView.setMediaPlayer(mediaPlayer);
                    mediaView.setFitWidth(820);
                    mediaView.setFitHeight(450);
                    mediaPlayer.play();

                    Stage stage = new Stage();
                    stage.setTitle("Game Over");
                    stage.setResizable(false);
                    Scene scene = new Scene(root);
                    stage.setScene(scene);

                    stage.setOnCloseRequest(event -> {
                        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                            mediaPlayer.stop();
                        }
                    });

                    stage.showAndWait();
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLGameWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        userScoreText.setText(String.valueOf(userScore));
        computerScoreText.setText(String.valueOf(computerScore));
        difficulty = FXMLLocalController.getDifficulty();

        if (Local_ModesController.isTwoPlayers) {
            showPlayerNameDialog();
            imageView.setImage(player2Image);
        } else {
            computerNameText.setText("Computer");
        }

    }

    private void showPlayerNameDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/alert/FXMLPlayerNameDialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Player Names");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                String player1Name = controller.getPlayer1Name();
                String player2Name = controller.getPlayer2Name();
                userNameText.setText(player1Name);
                computerNameText.setText(player2Name);
            } else {
                userNameText.setText("Player1");
                computerNameText.setText("Player2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReplayButton(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game Record File");
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            replayMoves(file);
        }
    }

    private void replayMoves(File file) {
        List<String> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("User:") || line.startsWith("Computer:") || line.startsWith("User Score:") || line.startsWith("Computer Score:")) {
                    continue;
                }
                moves.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        resetGame();
        Timeline timeline = new Timeline();
        int moveIndex = 0;

        for (String move : moves) {
            String[] parts = move.split(" ");
            String player = parts[1];
            String[] coords = parts[2].split(",");
            int row = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);

            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(moveIndex * 1), e -> {
                Button button = getButtonByRowCol(row, col);
                if (button != null) {
                    Image currentImage = player.equals("X") ? xImage : oImage;
                    makeMove(button, row, col, player, currentImage);
                }
            }));

            moveIndex++;
        }

        timeline.play();
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

}
