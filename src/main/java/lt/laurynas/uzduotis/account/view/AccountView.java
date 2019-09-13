package lt.laurynas.uzduotis.account.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lt.laurynas.uzduotis.account.entity.Account;

import java.math.BigDecimal;

@ApiModel
public class AccountView {

    private final String email;
    private final BigDecimal balance;

    public AccountView(String email, BigDecimal balance) {
        this.email = email;
        this.balance = balance;
    }

    public static AccountView mapFromEntity(Account account) {
        return new AccountView(account.getEmail(), account.getBalance());
    }

    @JsonIgnore
    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}