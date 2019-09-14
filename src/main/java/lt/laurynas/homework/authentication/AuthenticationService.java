package lt.laurynas.homework.authentication;

import lt.laurynas.homework.account.AccountService;
import lt.laurynas.homework.authentication.entity.User;
import lt.laurynas.homework.authentication.rest.request.CreateUserRequest;
import lt.laurynas.homework.exception.ApiException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private final AuthenticationRepo repo;
    private final AccountService accountService;

    public AuthenticationService(AuthenticationRepo repo, AccountService accountService) {
        this.repo = repo;
        this.accountService = accountService;
    }

    @Transactional
    public String register(CreateUserRequest request) {
        Optional<User> maybeUser = repo.findUser(request.getEmail());
        if (maybeUser.isPresent()) {
            throw ApiException.badRequest("Bad user email");
        }

        String encodedPassword = encoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword);
        repo.persist(user);
        accountService.createNewAccount(user.getEmail());
        return encodedPassword;
    }

    public User findUser(String email) {
        return repo.findUser(email)
                .orElseThrow(() -> ApiException.notFound("User does not exist"));
    }
}
