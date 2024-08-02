package models;

public class TicTacToeGame {
    private int gameId;
    private String player1;
    private String player2;
    private int[][] board;
    private int currentPlayer;

    public TicTacToeGame(int gameId, String player1, String player2) {
        this.gameId = gameId;
        this.player1 = player1;
        this.player2 = player2;
        this.board = new int[3][3]; // 3x3 board for Tic Tac Toe
        this.currentPlayer = 1; // Player 1 starts
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == 0) {
            board[row][col] = currentPlayer;
            currentPlayer = 3 - currentPlayer; // Switch player
            return true;
        }
        return false;
    }

    public int checkWinner() {
        // Check rows, columns, and diagonals for a winner
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return board[i][0];
            }
            if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return board[0][i];
            }
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        return 0; // No winner
    }

    public int getGameId() {
        return gameId;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int[][] getBoard() {
        return board;
    }
}
