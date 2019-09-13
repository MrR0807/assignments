package lt.laurynas.uzduotis.authentication;

import io.swagger.annotations.Api;
import lt.laurynas.uzduotis.authentication.rest.request.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "${endpoint.authentication}", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEndPoint.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationEndPoint(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody @Valid CreateUserRequest request) {
        LOGGER.info("Register new user request: {}", request);

        authenticationService.register(request);
    }
}