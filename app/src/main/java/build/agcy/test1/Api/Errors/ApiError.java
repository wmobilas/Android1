package build.agcy.test1.Api.Errors;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class ApiError extends Exception {
    private final String response;
    private final int code;

    public ApiError(int code, String response) {
        this.code = code;
        this.response = response;
    }

    @Override
    public String getMessage() {
        return "Api error: " + response;
    }
}
