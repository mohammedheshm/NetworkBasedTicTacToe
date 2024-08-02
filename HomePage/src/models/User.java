package models;

public class User {

    private String username;
    private int score;
    private boolean status;
    private String email;

    public User(String data) {
        String[] fields = data.split("&");
        this.username = fields[0];
        this.score = Integer.parseInt(fields[1]);
        this.status = "Online".equals(fields[2]);
        this.email = fields[3];
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public boolean getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return username + "&" + score + "&" + (status ? "Online" : "Offline") + "&" + email;
    }
}
