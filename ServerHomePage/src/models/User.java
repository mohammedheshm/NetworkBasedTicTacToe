package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty email;
    private final SimpleIntegerProperty score;
    private final SimpleStringProperty status;


    // Constructor
    public User(String username, String password, String email, int score, boolean status) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.email = new SimpleStringProperty(email);
        this.score = new SimpleIntegerProperty(score);
        this.status = new SimpleStringProperty(status ? "Online" : "Offline");
    }

    // Getter and Setter for username
    public String getUsername() {
        return username.get();
    }
    public String getUseremail() {
        return email.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // Getter and Setter for score
    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public SimpleIntegerProperty scoreProperty() {
        return score;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status.get();
    }

    public void setStatus(boolean status) {
        this.status.set(status ? "Online" : "Offline");
    }

    public StringProperty statusProperty() {
        return status;
    }
}

