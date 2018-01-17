package online.omnia.MGID;

/**
 * Created by lollipop on 28.10.2017.
 */
public class JsonTokenEntity {
    private String token;
    private String refreshToken;
    private String idAuth;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getIdAuth() {
        return idAuth;
    }

    public void setIdAuth(String idAuth) {
        this.idAuth = idAuth;
    }
}
