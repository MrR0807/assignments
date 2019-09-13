package lt.laurynas.uzduotis.configuration;

import lt.laurynas.uzduotis.authentication.AuthenticationService;
import lt.laurynas.uzduotis.interceptors.BasicAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private AuthenticationService authenticationService;

    @Autowired
    public WebConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasicAuthenticationInterceptor(authenticationService)).addPathPatterns("/account/*");
    }
}