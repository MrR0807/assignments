package lt.laurynas.uzduotis.authentication;

import lt.laurynas.uzduotis.authentication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthenticationRepo {

    private final EntityManager em;

    @Autowired
    public AuthenticationRepo(EntityManager em) {
        this.em = em;
    }

    public Optional<User> findUser(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    /**
     * Getting list of users to avoid handling exception when using getSingleResult()
     *
     * @see <a href="For more information">http://sysout.be/2011/03/09/why-you-should-never-use-getsingleresult-in-jpa/</a>
     */
    public Optional<User> findUser(String email) {
        List<User> result = em.createQuery("" +
                "SELECT u from User u " +
                "WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return Optional.ofNullable(result.isEmpty() ? null : result.get(0));
    }

    public void persist(User user) {
        em.persist(user);
    }
}
