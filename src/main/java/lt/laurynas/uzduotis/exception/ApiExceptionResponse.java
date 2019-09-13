package lt.laurynas.uzduotis.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiExceptionResponse {

    public int statusCode;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime timestamp;
    public String reason;
    public List<ApiExceptionDetails> exceptions;

    public ApiExceptionResponse() {
    }

    private ApiExceptionResponse(int statusCode, LocalDateTime timestamp, String reason, List<ApiExceptionDetails> exceptions) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.reason = reason;
        this.exceptions = exceptions;
    }

    public static ApiExceptionResponse of(int statusCode, LocalDateTime timestamp,
                                          String reason, List<ApiExceptionDetails> exceptions) {
        return new ApiExceptionResponse(statusCode, timestamp, reason, exceptions);
    }

    public static ApiExceptionResponse ofBadRequest(String reason, List<ApiExceptionDetails> exceptions) {
        return new ApiExceptionResponse(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(Clock.systemUTC()), reason, exceptions);
    }

    public static ApiExceptionResponse ofBadRequest(String reason) {
        return ofBadRequest(reason, Collections.emptyList());
    }

    public static ApiExceptionResponse ofApiException(ApiException apiException) {
        return new ApiExceptionResponse(apiException.getStatusCode(), apiException.getTimestamp(), apiException.getReason(), apiException.getExceptions());
    }

    public ResponseEntity<ApiExceptionResponse> asResponseEntity() {
        return new ResponseEntity<>(this, HttpStatus.valueOf(this.statusCode));
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class ApiExceptionDetails {
        public String code;
        public String message;
        public String fieldName;

        public ApiExceptionDetails() {
        }

        public ApiExceptionDetails(String code, String message, String fieldName) {
            this.code = code;
            this.message = message;
            this.fieldName = fieldName;
        }
    }

}