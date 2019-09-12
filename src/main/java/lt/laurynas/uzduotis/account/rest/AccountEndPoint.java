package lt.laurynas.uzduotis.account.rest;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "${endpoint.account}", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountEndPoint {


}
