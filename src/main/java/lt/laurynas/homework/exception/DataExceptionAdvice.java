package lt.laurynas.homework.exception;

import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DataExceptionAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataExceptionAdvice.class);

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ApiExceptionResponse> handle(DataException e) {
        LOGGER.error("Data exception occurred: {}", e.toString());

        return ApiExceptionResponse
                .ofBadRequest("Invalid request")
                .asResponseEntity();
    }
}
