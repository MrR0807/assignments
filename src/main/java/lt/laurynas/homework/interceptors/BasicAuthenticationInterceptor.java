package lt.laurynas.homework.interceptors;

import lt.laurynas.homework.authentication.entity.User;
import lt.laurynas.homework.authentication.AuthenticationService;
import lt.laurynas.homework.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final AuthenticationService authenticationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String UNAUTHENTICATED_MESSAGE = "Unauthenticated user";

    public BasicAuthenticationInterceptor(AuthenticationService authenticationService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationService = authenticationService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authHeader = request.getHeader("Auth");

        if (authHeader == null) {
            throw ApiException.ofExceptions(HttpStatus.FORBIDDEN, UNAUTHENTICATED_MESSAGE);
        }

        String[] emailPassword = authHeader.split(":");
        if (emailPassword.length != 2) {
            throw ApiException.ofExceptions(HttpStatus.FORBIDDEN, UNAUTHENTICATED_MESSAGE);
        }

        String email = StringUtils.strip(emailPassword[0]); //Java 11 String.strip()
        String password = StringUtils.strip(emailPassword[1]); //Java 11 String.strip()

        if (notValid(email) || notValid(password)) {
            throw ApiException.ofExceptions(HttpStatus.FORBIDDEN, UNAUTHENTICATED_MESSAGE);
        }

        User user = authenticationService.findUser(email);
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw ApiException.ofExceptions(HttpStatus.FORBIDDEN, UNAUTHENTICATED_MESSAGE);
        }

        request.setAttribute("userEmail", user.getEmail());
        return super.preHandle(request, response, handler);
    }

    private boolean notValid(String string) {
        return StringUtils.isEmpty(string) || string.length() > 255;
    }
}