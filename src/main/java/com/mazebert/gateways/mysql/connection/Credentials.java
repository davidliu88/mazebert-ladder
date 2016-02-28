package com.mazebert.gateways.mysql.connection;

public class Credentials {
    private final String user;
    private final String password;
    private final String url;

    public Credentials(String user, String password, String url) {
        this.user = user;
        this.password = password;
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
}
