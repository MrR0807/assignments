package lt.laurynas.uzduotis.exception;

import org.springframework.http.HttpStatus;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 6844855207281735148L;
    private final int statusCode;
    private final LocalDateTime timestamp;
    private final String reason;
    private final List<ApiExceptionResponse.ApiExceptionDetails> exceptions;

    private ApiException(int statusCode, LocalDateTime timestamp, String reason, List<ApiExceptionResponse.ApiExceptionDetails> exceptions) {
        super(reason);
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.reason = reason;
        this.exceptions = exceptions;
    }

    public static ApiException ofExceptions(HttpStatus status, String reason, ApiExceptionResponse.ApiExceptionDetails... exceptions) {
        return new ApiException(status.value(), LocalDateTime.now(Clock.systemUTC()), reason, Arrays.asList(exceptions));
    }

    public static ApiException badRequest(String reason, ApiExceptionResponse.ApiExceptionDetails... exceptions) {
        return new ApiException(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(Clock.systemUTC()), reason, Arrays.asList(exceptions));
    }

    public static ApiException notFound(String reason, ApiExceptionResponse.ApiExceptionDetails... exceptions) {
        return new ApiException(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(Clock.systemUTC()), reason, Arrays.asList(exceptions));
    }

    public int getStatusCode() {
        return statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getReason() {
        return reason;
    }

    public List<ApiExceptionResponse.ApiExceptionDetails> getExceptions() {
        return exceptions;
    }
}