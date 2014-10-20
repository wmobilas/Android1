package com.agcy.eatwithme.Api.Errors;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class ApiError extends Exception {
    // коды ошибок я тебе потом ещё дам
    // этот код ошибки будет, если при логине что-то сделали не так.
    public static final int BADCREDITS = 401;

    private final String response;
    private int code;

    public ApiError(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public ApiError(String response) {
        this.response = response;
    }

    @Override
    public String getMessage() {
        return "Api error: " + response;
    }

    public int getCode() {
        return code;
    }
}
