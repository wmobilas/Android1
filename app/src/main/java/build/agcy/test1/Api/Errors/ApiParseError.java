package build.agcy.test1.Api.Errors;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class ApiParseError extends ApiError {
    public ApiParseError() {
        super(-1, "Parsing error");
    }
}
