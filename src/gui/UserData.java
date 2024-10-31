package gui;

public class UserData {
    private static String username;

    public static void setUsername(String username) {
        UserData.username = username;
    }

    public static String getUsername() {
        return username;
    }
}
