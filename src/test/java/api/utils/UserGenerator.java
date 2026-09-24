package api.utils;

import api.model.User;

import java.util.UUID;

public final class UserGenerator {
    private UserGenerator() {

    }

    public static String uniqueEmail() {
        long timestamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "puh_" + timestamp + "_" + uuid + "@example.com";
    }

    public static String uniqueName() {
        return "Puh_" + System.currentTimeMillis();
    }

    public static String validPassword() {
        return "pass" + System.currentTimeMillis();
    }

    public static String invalidPassword() {
        return "666";
    }

    public static User randomUser() {
        return new User(uniqueEmail(), validPassword(), uniqueName());
    }
}
