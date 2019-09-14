package lt.laurynas.homework.configuration;

import lt.laurynas.homework.authentication.AuthenticationService;
import lt.laurynas.homework.interceptors.BasicAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private AuthenticationService authenticationService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public WebConfig(AuthenticationService authenticationService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authenticationService = authenticationService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthenticationInterceptor(authenticationService, bCryptPasswordEncoder))
                .addPathPatterns("/account/*");
    }
}