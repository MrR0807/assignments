package lt.laurynas.homework.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionAdvice.class);

    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiExceptionResponse> handle(ApiException exception) {
        LOGGER.error("ApiException: {}", exception.toString());

        return ApiExceptionResponse
                .ofApiException(exception)
                .asResponseEntity();
    }
}