package lt.laurynas.uzduotis.account.view;

import io.swagger.annotations.ApiModel;
import lt.laurynas.uzduotis.account.entity.Action;

import java.math.BigDecimal;
import java.util.Objects;

@ApiModel
public class RecordView {

    private final Long id;
    private final Action action;
    private final BigDecimal amount;

    public RecordView(Long id, Action action, BigDecimal amount) {
        this.id = id;
        this.action = action;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordView that = (RecordView) o;
        return Objects.equals(id, that.id) &&
                action == that.action &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, action, amount);
    }

    @Override
    public String toString() {
        return "RecordView{" +
                "id=" + id +
                ", action=" + action +
                ", amount=" + amount +
                '}';
    }
}
