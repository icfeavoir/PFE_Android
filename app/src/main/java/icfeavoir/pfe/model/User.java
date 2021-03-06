package icfeavoir.pfe.model;

public class User extends Model {

    private static User instance;

    private String username;
    private String password;
    private String token;

    private User() {}

    synchronized public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
