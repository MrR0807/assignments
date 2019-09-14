package lt.laurynas.homework.account;

import io.swagger.annotations.Api;
import lt.laurynas.homework.account.rest.request.DepositRequest;
import lt.laurynas.homework.account.rest.request.WithdrawRequest;
import lt.laurynas.homework.account.view.AccountView;
import lt.laurynas.homework.account.view.RecordView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * To use these endpoints you have to add Header - Auth : email:password
 * To test it out with currently created users use this information:
 * Auth : test@test.com:$2a$04$ShL0W16R0.bKg1r/Ku.JKOJb3T8gzeWtY4BlNIZ2Gi15Qe15NzvLa
 */

@Api
@RestController
@RequestMapping(value = "${endpoint.account}", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountEndPoint {

    private final Logger LOGGER = LoggerFactory.getLogger(AccountEndPoint.class);

    private final AccountService accountService;

    @Autowired
    public AccountEndPoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deposit(@RequestBody @Valid DepositRequest depositRequest,
                        @ApiIgnore @RequestAttribute("userEmail") String userEmail) {
        LOGGER.info("Deposit request. Body: {}; User: {}", depositRequest, userEmail);
        accountService.deposit(depositRequest, userEmail);
    }

    @PostMapping("/withdraw")
    public void withdraw(@RequestBody @Valid WithdrawRequest withdrawRequest,
                         @ApiIgnore @RequestAttribute("userEmail") String userEmail) {
        LOGGER.info("Withdraw request. Body: {}; User {}", withdrawRequest, userEmail);
        accountService.withdraw(withdrawRequest, userEmail);
    }

    @GetMapping("/balance")
    public AccountView getBalance(@ApiIgnore @RequestAttribute("userEmail") String userEmail) {
        LOGGER.info("Get balance request. User: {}", userEmail);
        return accountService.getAccount(userEmail);
    }

    @GetMapping("/statement")
    public List<RecordView> getStatement(@ApiIgnore @RequestAttribute("userEmail") String userEmail) {
        LOGGER.info("Get statement. User: {}", userEmail);

        return accountService.getStatement(userEmail);
    }
}