package api.utils;

import api.model.User;

import java.util.UUID;

public class UserGenerator {
    public static User randomUser() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return new User(
                "test_" + uuid + "@example.com",
                "p" + uuid,
                "puh" + uuid
        );
    }
}
