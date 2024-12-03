package domain;

public class UserData {
    private static String username;
    private static String name;
    private static String email;
    private static String phoneNumber;
    private static String address;
    private static String postalCode;
    private static String team;

    // Métodos getter y setter para cada atributo

    public static void setUsername(String username) {
        UserData.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static void setName(String name) {
        UserData.name = name;
    }

    public static String getName() {
        return name;
    }

    public static void setEmail(String email) {
        UserData.email = email;
    }

    public static String getEmail() {
        return email;
    }

    public static void setPhoneNumber(String phoneNumber) {
        UserData.phoneNumber = phoneNumber;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setAddress(String address) {
        UserData.address = address;
    }

    public static String getAddress() {
        return address;
    }

    public static void setPostalCode(String postalCode) {
        UserData.postalCode = postalCode;
    }

    public static String getPostalCode() {
        return postalCode;
    }

    public static void setTeam(String team) {
        UserData.team = team;
    }

    public static String getTeam() {
        return team;
    }
}
