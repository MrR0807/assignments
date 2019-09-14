package lt.laurynas.uzduotis.account;

import lt.laurynas.uzduotis.account.entity.Account;
import lt.laurynas.uzduotis.account.entity.AccountRecord;
import lt.laurynas.uzduotis.account.view.RecordView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountRepository {

    private EntityManager em;

    @Value("${account.record.pagesize}")
    private Integer pageSize;

    @Autowired
    public AccountRepository(EntityManager em) {
        this.em = em;
    }

    public void saveAccount(Account account) {
        em.persist(account);
    }

    public Optional<Account> getBalance(String userEmail) {
        List<Account> result = em.createQuery("" +
                "SELECT a FROM Account a " +
                "WHERE a.email = :email", Account.class)
                .setParameter("email", userEmail)
                .getResultList();

        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    public List<RecordView> getRecords(String userEmail) {
        return em.createQuery("" +
                "SELECT new lt.laurynas.uzduotis.account.view.RecordView(r.id, r.created, r.action, r.amount) " +
                "FROM AccountRecord r " +
                "WHERE r.account.email = :email " +
                "ORDER BY r.id DESC", RecordView.class)
                .setParameter("email", userEmail)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public void saveRecord(AccountRecord record) {
        em.persist(record);
    }
}
