package lt.laurynas.uzduotis.authentication.rest;

import lt.laurynas.uzduotis.authentication.rest.request.CreateUserRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationEndPointTest {

    @Value("${endpoint.authentication}")
    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createUser__thenReturn200() {
        CreateUserRequest request = new CreateUserRequest("test4@test.com", "password");

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createUser__whenUserExists__thenReturn400() {
        CreateUserRequest request = new CreateUserRequest("test@test.com", "password");

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Bad user name");
    }

    @Test
    public void createUser__whenInvalidInput__return400() {
        CreateUserRequest request = new CreateUserRequest("bademail", "password");

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/register", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}