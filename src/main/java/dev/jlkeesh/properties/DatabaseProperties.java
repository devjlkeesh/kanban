package dev.jlkeesh.properties;

import java.util.ResourceBundle;

public class DatabaseProperties {
    private final static ResourceBundle settings;
    public final static String url;
    public final static String username;
    public final static String password;

    static {
        settings = ResourceBundle.getBundle("settings");
        url = settings.getString("database.url");
        username = settings.getString("database.username");
        password = settings.getString("database.password");
    }
}
