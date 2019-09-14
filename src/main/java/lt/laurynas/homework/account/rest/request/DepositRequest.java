package lt.laurynas.homework.account.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel
public class DepositRequest {

    @NotNull
    @Min(0)
    private final BigDecimal deposit;

    @JsonCreator
    public DepositRequest(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    @Override
    public String toString() {
        return "DepositRequest{" +
                "deposit=" + deposit +
                '}';
    }
}
