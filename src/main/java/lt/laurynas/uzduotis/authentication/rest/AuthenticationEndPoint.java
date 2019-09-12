package lt.laurynas.uzduotis.authentication.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lt.laurynas.uzduotis.authentication.rest.request.CreateUserRequest;
import lt.laurynas.uzduotis.authentication.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api
@RestController
@RequestMapping(value = "${endpoint.authentication}", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class AuthenticationEndPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationEndPoint.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationEndPoint(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Register new user")
    public void registerUser(@RequestBody @Valid CreateUserRequest request) {
        LOGGER.info("Register new user request: {}", request);

        authenticationService.register(request);
    }
}