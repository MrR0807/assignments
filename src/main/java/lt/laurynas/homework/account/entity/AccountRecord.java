package lt.laurynas.homework.account.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class AccountRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private Action action;
    @Column(precision = 19, scale = 4)
    private BigDecimal amount;
    @ManyToOne
    @JoinColumn(name = "email")
    private Account account;

    public AccountRecord() {
    }

    public AccountRecord(Action action, BigDecimal amount, Account account) {
        this.action = action;
        this.amount = amount;
        this.account = account;
    }

    @PrePersist
    private void initCreated() {
        this.created = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}