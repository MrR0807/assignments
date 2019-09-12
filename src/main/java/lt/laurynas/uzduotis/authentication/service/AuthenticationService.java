package lt.laurynas.uzduotis.authentication.service;

import lt.laurynas.uzduotis.authentication.entity.User;
import lt.laurynas.uzduotis.authentication.repo.AuthenticationRepo;
import lt.laurynas.uzduotis.authentication.rest.request.CreateUserRequest;
import lt.laurynas.uzduotis.exception.ApiException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private final AuthenticationRepo repo;

    public AuthenticationService(AuthenticationRepo repo) {
        this.repo = repo;
    }

    @Transactional
    public void register(CreateUserRequest request) {
        Optional<User> maybeUser = repo.findUser(request.getEmail());
        if (maybeUser.isPresent()) {
            throw ApiException.badRequest("Bad user name");
        }

        String encodedPassword = encoder.encode(request.getPassword());
        User user = new User(request.getEmail(), encodedPassword);
        repo.persist(user);
    }
}
