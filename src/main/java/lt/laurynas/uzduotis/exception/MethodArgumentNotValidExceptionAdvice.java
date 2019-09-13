package lt.laurynas.uzduotis.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MethodArgumentNotValidExceptionAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodArgumentNotValidExceptionAdvice.class);

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiExceptionResponse> handle(MethodArgumentNotValidException ex) {
        LOGGER.error("Bind exception has occurred. Description {}", ex.toString());

        List<ApiExceptionResponse.ApiExceptionDetails> listOfErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ApiExceptionResponse.ApiExceptionDetails(error.getCode(), error.getDefaultMessage(), error.getField()))
                .collect(Collectors.toList());

        return ApiExceptionResponse
                .ofBadRequest("One of the input fields contains invalid value", listOfErrors)
                .asResponseEntity();
    }
}
