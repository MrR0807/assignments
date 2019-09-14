package lt.laurynas.homework.account.view;

import io.swagger.annotations.ApiModel;
import lt.laurynas.homework.account.entity.Action;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@ApiModel
public class RecordView {

    private final Long id;
    private final LocalDateTime created;
    private final Action action;
    private final BigDecimal amount;

    public RecordView(Long id, LocalDateTime created, Action action, BigDecimal amount) {
        this.id = id;
        this.created = created;
        this.action = action;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
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
                Objects.equals(created, that.created) &&
                action == that.action &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, created, action, amount);
    }

    @Override
    public String toString() {
        return "RecordView{" +
                "id=" + id +
                ", created=" + created +
                ", action=" + action +
                ", amount=" + amount +
                '}';
    }
}
