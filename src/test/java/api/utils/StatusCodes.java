package api.utils;


/**
 * Константы HTTP-статусов, используемых в API Stellar Burgers
 * Вместо «магических чисел» в тестах используем говорящие имена
 */
public final class StatusCodes {
    private StatusCodes() {

    }

    public static final int OK = 200;
    public static final int CREATED = 201; // код в апи пока не используется
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
}
