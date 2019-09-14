package lt.laurynas.uzduotis.account;

import lt.laurynas.uzduotis.account.entity.Account;
import lt.laurynas.uzduotis.account.entity.AccountRecord;
import lt.laurynas.uzduotis.account.entity.Action;
import lt.laurynas.uzduotis.account.rest.request.DepositRequest;
import lt.laurynas.uzduotis.account.rest.request.WithdrawRequest;
import lt.laurynas.uzduotis.account.view.AccountView;
import lt.laurynas.uzduotis.account.view.RecordView;
import lt.laurynas.uzduotis.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repo;

    @Autowired
    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public void createNewAccount(String userEmail) {
        Account account = new Account(userEmail, BigDecimal.ZERO);
        repo.saveAccount(account);
    }

    @Transactional
    public void deposit(DepositRequest depositRequest, String userEmail) {
        Account account = getAccountEntity(userEmail);
        account.deposit(depositRequest.getDeposit());
        repo.saveRecord(new AccountRecord(Action.DEPOSIT, depositRequest.getDeposit(), account));
    }

    private Account getAccountEntity(String userEmail) {
        return repo.getBalance(userEmail)
                .orElseThrow(() -> ApiException.notFound("User not found"));
    }

    public AccountView getAccount(String userEmail) {
        Account account = getAccountEntity(userEmail);

        return AccountView.mapFromEntity(account);
    }

    @Transactional
    public void withdraw(WithdrawRequest withdrawRequest, String userEmail) {
        Account account = getAccountEntity(userEmail);

        if (isWithdrawBiggerThanBalance(account, withdrawRequest)) {
            throw ApiException.badRequest("Invalid withdraw request");
        }

        account.withdraw(withdrawRequest.getWithdraw());
        repo.saveRecord(new AccountRecord(Action.WITHDRAW, withdrawRequest.getWithdraw(), account));
    }

    private boolean isWithdrawBiggerThanBalance(Account account, WithdrawRequest withdrawRequest) {
        return account.getBalance().compareTo(withdrawRequest.getWithdraw()) < 0;
    }

    public List<RecordView> getStatement(String userEmail) {
        return repo.getRecords(userEmail);
    }
}
