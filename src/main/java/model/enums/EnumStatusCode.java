package model.enums;

public enum EnumStatusCode {

    SUCCESS(200, "OK", ""),
    BAD_REQUEST(400, "Bad Request", "<html><body><h1>400 - Bad Request</h1></body></html>"),
    NOT_IDENTIFIED(401, "Unauthorized", "<html><body><h1>401 - Unauthorized</h1></body></html>"),
    NOT_FOUND(404, "Not Found", "<html><body><h1>404 - Not Found</h1></body></html>"),
    SERVER_ERROR(500, "Internal Server Error", "<html><body><h1>500 - Internal Server Error</h1></body></html>");

    /* ATTRIBUTES ========================================================== */

    public final int code;
    public final String name;
    public final String message;

    /* CONSTRUCTOR ========================================================= */

    EnumStatusCode(int code, String name, String message) {
        this.code = code;
        this.name = name;
        this.message = message;
    }

    /* PUBLIC STATIC METHODS =============================================== */

    public static EnumStatusCode findMethodByValue(int value) {
        for (EnumStatusCode status : EnumStatusCode.values()) {
            if (status.code == value) {
                return status;
            }
        }
        return SERVER_ERROR;
    }

    public static String findNameByValue(int value) {
        for (EnumStatusCode status : EnumStatusCode.values()) {
            if (status.code == value) {
                return status.name;
            }
        }
        return SERVER_ERROR.name;
    }

    public static String findMessageByValue(int value) {
        for (EnumStatusCode status : EnumStatusCode.values()) {
            if (status.code == value) {
                return status.message;
            }
        }
        return SERVER_ERROR.message;
    }
}
