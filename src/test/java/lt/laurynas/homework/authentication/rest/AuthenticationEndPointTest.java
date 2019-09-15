package lt.laurynas.homework.authentication.rest;

import lt.laurynas.homework.account.AccountService;
import lt.laurynas.homework.account.view.AccountView;
import lt.laurynas.homework.authentication.AuthenticationService;
import lt.laurynas.homework.authentication.entity.User;
import lt.laurynas.homework.authentication.rest.request.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:clear-h2.sql", "classpath:schema-h2.sql", "classpath:data-h2.sql"})
public class AuthenticationEndPointTest {

    @Value("${endpoint.authentication}")
    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AccountService accountService;

    @Test
    public void createUser() {
        CreateUserRequest request = new CreateUserRequest("test4@test.com", "password");

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/register", request, String.class);
        User user = authenticationService.findUser("test4@test.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(user.getEmail()).isEqualTo("test4@test.com");
    }

    @Test
    public void createUser__thenCreateAccount() {
        CreateUserRequest request = new CreateUserRequest("test4@test.com", "password");
        restTemplate.postForEntity(url + "/register", request, String.class);

        AccountView account = accountService.getAccount("test4@test.com");

        assertThat(account.getEmail()).isEqualTo("test4@test.com");
        assertThat(account.getBalance().doubleValue()).isEqualTo(0);
    }

    @Test
    public void createUser__whenUserExists__thenReturn400() {
        CreateUserRequest request = new CreateUserRequest("test@test.com", "password");

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Bad user email");
    }

    @Test
    public void createUser__whenInvalidInput__return400() {
        CreateUserRequest request = new CreateUserRequest("bademail", "password");

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
