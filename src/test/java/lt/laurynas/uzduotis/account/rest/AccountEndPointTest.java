package lt.laurynas.uzduotis.account.rest;

import lt.laurynas.uzduotis.account.rest.request.DepositRequest;
import lt.laurynas.uzduotis.account.rest.request.WithdrawRequest;
import lt.laurynas.uzduotis.account.view.AccountView;
import lt.laurynas.uzduotis.account.view.RecordView;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static lt.laurynas.uzduotis.account.entity.Action.DEPOSIT;
import static lt.laurynas.uzduotis.account.entity.Action.WITHDRAW;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"classpath:clear-h2.sql", "classpath:schema-h2.sql", "classpath:data-h2.sql"})
public class AccountEndPointTest {

    @Value("${endpoint.account}")
    private String url;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static HttpHeaders HEADERS = new HttpHeaders();

    @BeforeClass
    public static void setUp() {
        HEADERS.add("Auth", "test@test.com:$2a$04$ShL0W16R0.bKg1r/Ku.JKOJb3T8gzeWtY4BlNIZ2Gi15Qe15NzvLa");
    }

    @Test
    public void depositMoney() {
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(10000));
        HttpEntity<DepositRequest> requestHttpEntity = new HttpEntity<>(request, HEADERS);

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/deposit", requestHttpEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    /**
     * In database user: test@test.com has 100 in his bank account
     */
    @Test
    public void deposit__whenSuccessful__thenChangeAccountBalance() {
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(10000));
        HttpEntity<DepositRequest> depositMoneyRequest = new HttpEntity<>(request, HEADERS);
        HttpEntity<String> getBalanceRequest = new HttpEntity<>(null, HEADERS);

        restTemplate.postForEntity(url + "/deposit", depositMoneyRequest, String.class);
        ResponseEntity<AccountView> response = restTemplate
                .exchange(url + "/balance", HttpMethod.GET, getBalanceRequest, AccountView.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getBalance().doubleValue()).isEqualTo(10100);
    }

    @Test
    public void deposit__whenUnauthenticatedUser__then403() {
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(10000));
        ResponseEntity<String> response = restTemplate.postForEntity(url + "/deposit", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("Unauthenticated user");
    }

    /**
     * In database user: test@test.com has 100 in his bank account
     */
    @Test
    public void withdraw() {
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(50));
        HttpEntity<WithdrawRequest> withdrawMoneyRequest = new HttpEntity<>(request, HEADERS);
        HttpEntity<String> getBalanceRequest = new HttpEntity<>(null, HEADERS);

        restTemplate.postForEntity(url + "/withdraw", withdrawMoneyRequest, String.class);

        ResponseEntity<AccountView> response = restTemplate
                .exchange(url + "/balance", HttpMethod.GET, getBalanceRequest, AccountView.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getBalance().doubleValue()).isEqualTo(50);
    }

    /**
     * In database user: test@test.com has 100 in his bank account
     */
    @Test
    public void withdraw__whenWithdrawingMoreThanThereIsInAccount__then400() {
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(10000));
        HttpEntity<WithdrawRequest> withdrawMoneyRequest = new HttpEntity<>(request, HEADERS);
        ResponseEntity<String> response = restTemplate.postForEntity(url + "/withdraw", withdrawMoneyRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Invalid withdraw request");
    }

    @Test
    public void withdraw__whenUnauthenticatedUser__then403() {
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(10000));
        ResponseEntity<String> response = restTemplate.postForEntity(url + "/withdraw", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("Unauthenticated user");
    }

    /**
     * In database user: test@test.com has 100 in his bank account
     */
    @Test
    public void getBalance() {
        HttpEntity<DepositRequest> requestHttpEntity = new HttpEntity<>(null, HEADERS);
        ResponseEntity<AccountView> response = restTemplate
                .exchange(url + "/balance", HttpMethod.GET, requestHttpEntity, AccountView.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getBalance().doubleValue()).isEqualTo(100);
    }

    @Test
    public void getBalance__whenUnauthenticatedUser__then403() {
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/balance", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("Unauthenticated user");
    }

    @Test
    public void getStatement() {
        HttpEntity<DepositRequest> requestHttpEntity = new HttpEntity<>(null, HEADERS);

        ResponseEntity<List<RecordView>> response = restTemplate
                .exchange(url + "/statement", HttpMethod.GET, requestHttpEntity, new ParameterizedTypeReference<List<RecordView>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).extracting("action").containsExactly(DEPOSIT, WITHDRAW);
    }

    @Test
    public void getStatement__whenMoreThan20Records__thenShowLast20() {
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(100));
        HttpEntity<DepositRequest> depositMoneyRequest = new HttpEntity<>(request, HEADERS);
        for (int i = 0; i < 20; i++) {
            restTemplate.postForEntity(url + "/deposit", depositMoneyRequest, String.class);
        }

        HttpEntity<DepositRequest> requestHttpEntity = new HttpEntity<>(null, HEADERS);
        ResponseEntity<List<RecordView>> response = restTemplate
                .exchange(url + "/statement", HttpMethod.GET, requestHttpEntity, new ParameterizedTypeReference<List<RecordView>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(20);
        assertThat(response.getBody()).extracting("action").containsOnly(DEPOSIT);
    }

    @Test
    public void getStatement__whenWithdraw__thenLastStatementWithdraw() {
        WithdrawRequest request = new WithdrawRequest(BigDecimal.valueOf(50));
        HttpEntity<WithdrawRequest> withdrawMoneyRequest = new HttpEntity<>(request, HEADERS);
        restTemplate.postForEntity(url + "/withdraw", withdrawMoneyRequest, String.class);

        HttpEntity<DepositRequest> requestHttpEntity = new HttpEntity<>(null, HEADERS);
        ResponseEntity<List<RecordView>> response = restTemplate
                .exchange(url + "/statement", HttpMethod.GET, requestHttpEntity, new ParameterizedTypeReference<List<RecordView>>() {});
        RecordView lastRecord = response.getBody().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(3);
        assertThat(lastRecord.getId()).isEqualTo(3L);
        assertThat(lastRecord.getAction()).isEqualTo(WITHDRAW);
        assertThat(lastRecord.getAmount().doubleValue()).isEqualTo(50);
    }

    @Test
    public void getStatement__whenDeposit__thenLastStatementDeposit() {
        DepositRequest request = new DepositRequest(BigDecimal.valueOf(10000));
        HttpEntity<DepositRequest> depositMoneyRequest = new HttpEntity<>(request, HEADERS);
        restTemplate.postForEntity(url + "/deposit", depositMoneyRequest, String.class);

        HttpEntity<DepositRequest> requestHttpEntity = new HttpEntity<>(null, HEADERS);
        ResponseEntity<List<RecordView>> response = restTemplate
                .exchange(url + "/statement", HttpMethod.GET, requestHttpEntity, new ParameterizedTypeReference<List<RecordView>>() {});
        RecordView lastRecord = response.getBody().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(3);
        assertThat(lastRecord.getId()).isEqualTo(3L);
        assertThat(lastRecord.getAction()).isEqualTo(DEPOSIT);
        assertThat(lastRecord.getAmount().doubleValue()).isEqualTo(10000);
    }

    @Test
    public void getStatement__whenUnauthenticatedUser__then403() {
        ResponseEntity<String> response = restTemplate.getForEntity(url + "/statement", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("Unauthenticated user");
    }
}