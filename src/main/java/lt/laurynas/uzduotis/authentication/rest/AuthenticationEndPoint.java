package lt.laurynas.uzduotis.authentication.rest;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "${endpoint.authentication}", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationEndPoint {



}