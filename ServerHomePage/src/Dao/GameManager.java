package Dao;

import models.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    // Method to insert a new game into the database
    public static void insertGame(Game game) {
        String sql = "INSERT INTO Games (player1_id, player2_id, winner_id, dateTime, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, game.getPlayer1Id());
            preparedStatement.setInt(2, game.getPlayer2Id());
            preparedStatement.setObject(3, game.getWinnerId());
            preparedStatement.setTimestamp(4, game.getDateTime());
            preparedStatement.setString(5, game.getStatus());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a game's status and winner
    public static void updateGame(int gameId, Integer winnerId, String status) {
        String sql = "UPDATE Games SET winner_id = ?, status = ? WHERE game_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, winnerId);
            preparedStatement.setString(2, status);
            preparedStatement.setInt(3, gameId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all games from the database
    public static List<Game> getAllGames() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                Game game = new Game(
                        resultSet.getInt("game_id"),
                        resultSet.getInt("player1_id"),
                        resultSet.getInt("player2_id"),
                        resultSet.getObject("winner_id", Integer.class),
                        resultSet.getTimestamp("dateTime"),
                        resultSet.getString("status")
                );
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }
}
