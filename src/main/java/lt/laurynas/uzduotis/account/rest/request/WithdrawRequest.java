package lt.laurynas.uzduotis.account.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class WithdrawRequest {

    @NotNull
    @Min(0)
    private final BigDecimal withdraw;

    @JsonCreator
    public WithdrawRequest(BigDecimal withdraw) {
        this.withdraw = withdraw;
    }

    public BigDecimal getWithdraw() {
        return withdraw;
    }

    @Override
    public String toString() {
        return "WithdrawRequest{" +
                "withdraw=" + withdraw +
                '}';
    }
}
