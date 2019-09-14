package lt.laurynas.homework.exception;

import org.hibernate.exception.DataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DataExceptionAdvice {

    @ExceptionHandler(DataException.class)
    public ResponseEntity<ApiExceptionResponse> handle(DataException e) {
        return ApiExceptionResponse
                .ofBadRequest("Invalid request")
                .asResponseEntity();
    }
}
