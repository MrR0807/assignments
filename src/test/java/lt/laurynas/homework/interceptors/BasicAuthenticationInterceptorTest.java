package lt.laurynas.homework.interceptors;

import lt.laurynas.homework.authentication.AuthenticationService;
import lt.laurynas.homework.authentication.entity.User;
import lt.laurynas.homework.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BasicAuthenticationInterceptorTest {

    @Mock
    private AuthenticationService authenticationService;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4);

    @InjectMocks
    private BasicAuthenticationInterceptor interceptor;

    private final static User USER = new User("test@test.com", "$2a$04$ShL0W16R0.bKg1r/Ku.JKOJb3T8gzeWtY4BlNIZ2Gi15Qe15NzvLa");
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @Test
    public void preHandle() throws Exception {
        when(authenticationService.findUser(anyString())).thenReturn(USER);

        request.addHeader("Auth", "test@test.com:s");

        assertThat(interceptor.preHandle(request, response, null)).isTrue();
    }

    @Test
    public void preHandle__whenAuthHeaderIsNull() {
        assertThatThrownBy(() -> interceptor.preHandle(request, response, null)).isInstanceOf(ApiException.class)
                .hasMessageContaining("Unauthenticated user");
    }

    @Test
    public void preHandle__whenAuthHeaderContainsOnlyEmail() {
        request.addHeader("Auth", "test@test.com");

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null)).isInstanceOf(ApiException.class)
                .hasMessageContaining("Unauthenticated user");
    }

    @Test
    public void preHandle__whenAuthHeaderContainsOnlyPassword() {
        request.addHeader("Auth", ":s");

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null)).isInstanceOf(ApiException.class)
                .hasMessageContaining("Unauthenticated user");
    }

    @Test
    public void preHandle__whenAuthHeaderContainsEmptySpaces() throws Exception {
        when(authenticationService.findUser(anyString())).thenReturn(USER);

        request.addHeader("Auth", "    test@test.com   :   s   ");

        assertThat(interceptor.preHandle(request, response, null)).isTrue();
    }

    @Test
    public void preHandle__whenPasswordDoNotMatch() {
        when(authenticationService.findUser(anyString())).thenReturn(USER);

        request.addHeader("Auth", "test@test.com:doesnotmatch");

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null)).isInstanceOf(ApiException.class)
                .hasMessageContaining("Unauthenticated user");
    }

    @Test
    public void preHandle__whenEmailExceeds255Chars() {
        String authWithLongEmail = StringUtils.repeat("test", 70)
                .concat("@test.com:s");

        request.addHeader("Auth", authWithLongEmail);

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null)).isInstanceOf(ApiException.class)
                .hasMessageContaining("Unauthenticated user");
    }

    @Test
    public void preHandle__whenPasswordExceeds255Chars() {
        String longPassword =   StringUtils.repeat("password", 35);
        String authWithLongPassword = "test@test.com:".concat(longPassword);

        request.addHeader("Auth", authWithLongPassword);

        assertThatThrownBy(() -> interceptor.preHandle(request, response, null)).isInstanceOf(ApiException.class)
                .hasMessageContaining("Unauthenticated user");
    }
}