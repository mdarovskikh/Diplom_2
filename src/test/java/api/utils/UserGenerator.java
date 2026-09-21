package API.utils;

import API.model.User;

import java.util.UUID;

public class UserGenerator {
    public static User randomUser() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return new User(
                "test_" + uuid + "@puh.ru",
                "p" + uuid,
                "puh" + uuid
        );
    }
}
